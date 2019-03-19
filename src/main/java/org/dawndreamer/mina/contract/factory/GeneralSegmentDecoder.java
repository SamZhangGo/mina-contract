package org.dawndreamer.mina.contract.factory;

import org.dawndreamer.mina.contract.element.data.ByteContractSegmentData;
import org.dawndreamer.mina.contract.element.protocol.ByteContractElement;
import org.dawndreamer.mina.contract.element.protocol.ByteContractProtocolField;
import org.dawndreamer.mina.contract.element.protocol.ByteContractProtocolSegment;
import org.dawndreamer.mina.contract.exception.LoopSegmentErrorDefineException;
import org.dawndreamer.mina.contract.exception.NormalFieldNextErrorDefineException;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangsiming
 */
public class GeneralSegmentDecoder {

    private static final Logger logger = LoggerFactory.getLogger(GeneralSegmentDecoder.class);

    public ByteContractSegmentData decodeSegment(IoBuffer in, ByteContractProtocolSegment segment) {
        ByteContractSegmentData contractData = new ByteContractSegmentData();
        int loopCount = 0;
        boolean isLoop = false;
        String loopName = null;
        for (ByteContractElement ele : segment.getContent()) {
            if (isLoop) {
                if (ele instanceof ByteContractProtocolField) {
                    logger.error("loop segment should be ByteContractProtocolSegment, current<{}>", ele);
                    throw new LoopSegmentErrorDefineException();
                }
                while (loopCount > 0) {
                    Object elementValue = decodeSegment(in, (ByteContractProtocolSegment) ele);
                    contractData.addArrayElement(loopName, elementValue);
                    loopCount--;
                }
                loopName = null;
                isLoop = false;
            } else {
                if (ele instanceof ByteContractProtocolSegment) {
                    logger.error("Normal field next should be Field!, current<{}>", ele);
                    throw new NormalFieldNextErrorDefineException();
                }
                Object elementValue = decodeField(in, (ByteContractProtocolField) ele);
                isLoop = ((ByteContractProtocolField) ele).isNextLoop();
                if (isLoop) {
                    loopCount = Integer.parseInt(elementValue + "");
                    loopName = ele.getName();
                }
                if (!isLoop) {
                    contractData.put(ele.getName(), elementValue);
                }
            }
        }
        return contractData;
    }

//    private Object decodeElement(ByteContractElement ele, IoBuffer in) {
//        if (ele instanceof ByteContractProtocolField) {
//            ByteContractProtocolField field = (ByteContractProtocolField) ele;
//            return decodeField(in, field);
//        } else if (ele instanceof ByteContractProtocolSegment) {
//            ByteContractProtocolSegment childSegment = (ByteContractProtocolSegment) ele;
//            return decodeSegment(in, childSegment);
//        }
//        logger.error("byteContractElement<{}> not supported!", ele);
//        return null;
//    }

    private Object decodeField(IoBuffer in, ByteContractProtocolField field) {
        Object value = null;

        switch (field.getType()) {
            case Short:
                value = in.getShort();
                break;
            case Int:
                value = in.getInt();
                break;
            case Long:
                value = in.getLong();
                break;
            case ByteArray:
                byte[] byteArrayValue = new byte[field.getSize()];
                in.get(byteArrayValue);
                value = new String(byteArrayValue).trim();
                break;
            default:
                logger.error("ByteContractProtocolField type<{}> not supported!", field.getType());
                break;
        }
        return value;
    }
}
