package org.dawndreamer.mina.contract.codec.decode;

import org.dawndreamer.mina.contract.element.data.ByteContract;
import org.dawndreamer.mina.contract.element.data.ByteContractSegmentData;
import org.dawndreamer.mina.contract.factory.ByteContractFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * <p>Title: ByteContractBodyDecoder</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/14 11:26</p>
 */
public class ByteContractBodyDecoder implements MinaCodecBodyDecoder {

    @Override
    public Object doDecodeBody(IoSession session, short handler, int contentLength, IoBuffer in) {
        return ByteContractFactory.getFactory().assembleBody(handler, contentLength, in);
    }

    @Override
    public void writeObjectToSession(IoSession session, ProtocolDecoderOutput output,
        ByteContractSegmentData headerData, Object bodyData) {
        if (bodyData instanceof ByteContractSegmentData) {
            ByteContract contract = new ByteContract(headerData, (ByteContractSegmentData) bodyData);
            output.write(contract);
        }
    }

    @Override
    public String getDecoderName() {
        return "ByteContractBodyDecoder";
    }
}
