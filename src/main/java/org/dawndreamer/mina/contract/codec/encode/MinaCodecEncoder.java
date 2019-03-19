package org.dawndreamer.mina.contract.codec.encode;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * <p>Title: MinaCodecEncoder</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 14:06</p>
 */
public interface MinaCodecEncoder {

    /**
     * 将object进行编码，写入buffer
     *
     * @param buffer 缓存buffer
     * @param object 写入对象
     * @return 是否编码
     */
    boolean encode(IoBuffer buffer, Object object);

    /**
     * @return 返回encoder的名字
     */
    String getEncoderName();
}
