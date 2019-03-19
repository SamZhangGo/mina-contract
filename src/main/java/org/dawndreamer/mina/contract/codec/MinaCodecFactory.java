package org.dawndreamer.mina.contract.codec;

import org.dawndreamer.mina.contract.codec.decode.MinaCodecBodyDecoder;
import org.dawndreamer.mina.contract.codec.decode.MinaCodecDecoderChain;
import org.dawndreamer.mina.contract.codec.encode.MinaCodecEncoder;
import org.dawndreamer.mina.contract.codec.encode.MinaCodecEncoderChain;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: MinaCodecFactory</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/14 11:11</p>
 */
public class MinaCodecFactory implements ProtocolCodecFactory {

    private static final Logger logger = LoggerFactory.getLogger(MinaCodecFactory.class);

    private MinaCodecEncoderChain encoderChain;
    private MinaCodecDecoderChain decoderChain;
    private boolean reverseReceiveAndSendProtocol;
    private static MinaCodecFactory instance;

    private MinaCodecFactory(boolean reverse) {
        this.reverseReceiveAndSendProtocol = reverse;
        encoderChain = new MinaCodecEncoderChain();
        decoderChain = new MinaCodecDecoderChain();
    }

    public static MinaCodecFactory getInstance(boolean reverse) {
        if (instance == null) {
            synchronized (MinaCodecFactory.class) {
                if (instance == null) {
                    instance = new MinaCodecFactory(reverse);
                }
            }
        }
        return instance;
    }


    public static MinaCodecFactory getInstance() {
        if (instance == null) {
            logger.error("getInstance(boolean) should be invoke first");
            throw new RuntimeException("getInstance(boolean) should be invoke first");
        }
        return instance;
    }

    public void addDecoder(MinaCodecBodyDecoder decoder) {
        decoderChain.addDecoder(decoder);
    }

    public void addEncoder(MinaCodecEncoder encoder) {
        encoderChain.addEncoder(encoder);
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoderChain;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoderChain;
    }

    public boolean isReverseReceiveAndSendProtocol() {
        return reverseReceiveAndSendProtocol;
    }

}
