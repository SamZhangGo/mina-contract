package org.dawndreamer.mina.contract.codec.decode;

import org.dawndreamer.mina.contract.element.data.ByteContractSegmentData;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * <p>Title: MinaCodecBodyDecoder</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 11:27</p>
 */
public interface MinaCodecBodyDecoder {

    /**
     * 解析协议体
     * @param session 当前连接session
     * @param handler 协议号
     * @param contentLength 协议体的长度
     * @param in mina的IoBuffer流
     * @return 返回协议体的解析结果
     */
    Object doDecodeBody(IoSession session, short handler, int contentLength, IoBuffer in);

    /**
     * 往output中写入协议解析完的对象
     * @param session 当前session连接
     * @param output 输出流
     * @param headerData 协议头数据
     * @param bodyData 协议体数据
     */
    void writeObjectToSession(IoSession session, ProtocolDecoderOutput output, ByteContractSegmentData headerData,
        Object bodyData);

    String getDecoderName();
}
