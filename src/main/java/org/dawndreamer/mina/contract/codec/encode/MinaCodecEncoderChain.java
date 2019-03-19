package org.dawndreamer.mina.contract.codec.encode;

import java.util.ArrayList;
import java.util.List;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * <p>Title: MinaCodecEncoderChain</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 14:05</p>
 */
public class MinaCodecEncoderChain extends ProtocolEncoderAdapter {

    List<MinaCodecEncoder> encoderChain;

    public MinaCodecEncoderChain() {
        addEncoder(new ByteContractEncoder());
    }

    @Override
    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(1024, false);
        buffer.setAutoExpand(true);
        for (MinaCodecEncoder encoder : encoderChain) {
            if (encoder.encode(buffer, message)) {
                break;
            }
        }
        buffer.flip();
        out.write(buffer);
    }

    public void addEncoder(MinaCodecEncoder encoder) {
        if (encoderChain == null) {
            encoderChain = new ArrayList<>();
        }
        if (encoderChain.size() > 0) {
            for (MinaCodecEncoder existEncoder : encoderChain) {
                if (existEncoder.getEncoderName().equals(encoder.getEncoderName())) {
                    return;
                }
            }
        }
        encoderChain.add(encoder);
    }
}
