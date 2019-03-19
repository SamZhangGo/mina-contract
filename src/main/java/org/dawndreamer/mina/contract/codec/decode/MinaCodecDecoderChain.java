package org.dawndreamer.mina.contract.codec.decode;

import org.dawndreamer.mina.contract.constant.ContractConst.Header;
import org.dawndreamer.mina.contract.element.data.ByteContractSegmentData;
import org.dawndreamer.mina.contract.factory.ByteContractFactory;
import java.util.ArrayList;
import java.util.List;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: MinaCodecDecoderChain</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 11:24</p>
 */
public class MinaCodecDecoderChain extends CumulativeProtocolDecoder {

    private static final Logger logger = LoggerFactory.getLogger(MinaCodecDecoderChain.class);

    List<MinaCodecBodyDecoder> decoderChain;

    public MinaCodecDecoderChain() {
        addDecoder(new ByteContractBodyDecoder());
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        if (in.remaining() < Header.LENGTH) {
            logger.error("not enough bytes for header<{}>, remaining<{}>", Header.LENGTH, in.remaining());
            in.reset();
            return false;
        }
        in.mark();
        ByteContractSegmentData headerData = ByteContractFactory.getFactory().assembleHeader(in);
        logger.info("header decode result: {}", headerData);
        short handler = headerData.getShort(Header.HANDLER);
        int contentLength = headerData.getIntValue(Header.CONTENT_LENGTH);
        if (in.remaining() < contentLength) {
            logger.error("not enough bytes for body<{}>, remaining<{}>", contentLength, in.remaining());
            in.reset();
            return false;
        }
        in.mark();
        for (MinaCodecBodyDecoder decoder : decoderChain) {
            Object bodyObj = decoder.doDecodeBody(session, handler, contentLength, in);
            if (bodyObj != null) {
                int markValue = in.markValue();
                int ps = in.position();
                //存在多余数据，进行跳过
                if (markValue + contentLength > ps) {
                    int skipValue = markValue + contentLength - ps;
                    logger.warn("handle :" + handler + " has more invaild data,try to skip,markValue : "
                        + markValue
                        + ", position :" + ps + ", length :" + contentLength + ", skipValue :" + skipValue);
                    in.skip(skipValue);
                }
                decoder.writeObjectToSession(session, out, headerData, bodyObj);
                return true;
            }
        }
        logger.error("no Command is match. handle<{}>, length<{}>, session<{}>", handler, contentLength,
            session.toString());
        //跳过长度，丢弃这部分数据
        in.skip(Math.min(contentLength, in.remaining()));
        return false;
    }

    public void addDecoder(MinaCodecBodyDecoder decoder) {
        if (decoderChain == null) {
            decoderChain = new ArrayList<>();
        }
        if (decoderChain.size() > 0) {
            for (MinaCodecBodyDecoder existDecoder : decoderChain) {
                if (existDecoder.getDecoderName().equals(decoder.getDecoderName())) {
                    return;
                }
            }
        }
        decoderChain.add(decoder);
    }
}
