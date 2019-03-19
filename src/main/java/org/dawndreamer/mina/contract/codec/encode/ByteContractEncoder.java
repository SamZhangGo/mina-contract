package org.dawndreamer.mina.contract.codec.encode;

import org.dawndreamer.mina.contract.element.data.ByteContract;
import org.dawndreamer.mina.contract.factory.ByteContractFactory;
import org.apache.mina.core.buffer.IoBuffer;

/**
 * <p>Title: ByteContractEncoder</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 14:13</p>
 */
public class ByteContractEncoder implements MinaCodecEncoder {

    @Override
    public boolean encode(IoBuffer buffer, Object object) {
        if (object instanceof ByteContract) {
            return ByteContractFactory.getFactory().writeContractToBuffer(buffer, (ByteContract) object);
        }
        return false;
    }

    @Override
    public String getEncoderName() {
        return "ByteContractEncoder";
    }
}
