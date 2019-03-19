package org.dawndreamer.mina.contract.factory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dawndreamer.mina.contract.element.protocol.ByteContractElement;
import org.dawndreamer.mina.contract.element.protocol.ByteContractProtocolField;
import org.dawndreamer.mina.contract.element.protocol.ByteContractProtocolSegment;
import org.dawndreamer.mina.contract.element.protocol.DataEnum;
import org.dawndreamer.mina.contract.exception.FieldTypeWrongException;
import org.dawndreamer.mina.contract.exception.LoopSegmentErrorDefineException;
import org.dawndreamer.mina.contract.exception.NormalFieldNextErrorDefineException;
import org.dawndreamer.mina.contract.utils.ByteContractUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangsiming
 */
public class GeneralSegmentEncoder {

    private static final Logger logger = LoggerFactory.getLogger(GeneralSegmentEncoder.class);

    public IoBuffer encodeSegment(IoBuffer buffer, JSONObject segmentData,
        ByteContractProtocolSegment protocolSegment) {
        if (CollectionUtils.isEmpty(protocolSegment.getContent())) {
            return buffer;
        }
        int loopCount = 0;
        boolean isLoop = false;
        String loopName = null;
        for (ByteContractElement element : protocolSegment.getContent()) {
            if (isLoop) {
                if (element instanceof ByteContractProtocolField) {
                    logger.error("loop segment should be ByteContractProtocolSegment, current<{}>", element);
                    throw new LoopSegmentErrorDefineException();
                }
                JSONArray jsonArray = segmentData.getJSONArray(loopName);
                int i = 0;
                while (loopCount > 0) {
                    JSONObject itemObj = jsonArray.getJSONObject(i);
                    if (element instanceof ByteContractProtocolSegment) {
                        encodeSegment(buffer, itemObj, (ByteContractProtocolSegment) element);
                        i++;
                        loopCount--;
                    }
                }
                isLoop = false;
                loopName = null;
            } else {
                if (element instanceof ByteContractProtocolSegment) {
                    logger.error("Normal field next should be Field!, current<{}>", element);
                    throw new NormalFieldNextErrorDefineException();
                }
                isLoop = ((ByteContractProtocolField) element).isNextLoop();
                if (isLoop) {
                    loopName = element.getName();
                    JSONArray loopJsonArray = segmentData.getJSONArray(element.getName());
                    if (loopJsonArray != null) {
                        loopCount = loopJsonArray.size();
                    }
                    Object loopCountOfType = makeLoopCountType(loopCount,
                        ((ByteContractProtocolField) element).getType());
                    encodeField(buffer, loopCountOfType, (ByteContractProtocolField) element);
                } else {
                    encodeField(buffer, segmentData.get(element.getName()), (ByteContractProtocolField) element);
                }
            }
        }
        return buffer;
    }

//    private IoBuffer encodeElement(IoBuffer buffer, Object obj, ByteContractElement protocolElement) {
//        if (protocolElement instanceof ByteContractProtocolField) {
//            ByteContractProtocolField field = (ByteContractProtocolField) protocolElement;
//            return encodeField(buffer, obj, field);
//        } else if (protocolElement instanceof ByteContractProtocolSegment) {
//            ByteContractProtocolSegment childSegment = (ByteContractProtocolSegment) protocolElement;
//            return encodeSegment(buffer, (JSONObject) obj, childSegment);
//        }
//        logger.error("byteContractElement<{}> not supported!", protocolElement);
//        return buffer;
//    }

    private IoBuffer encodeField(IoBuffer buffer, Object fieldObj, ByteContractProtocolField protocolField) {

        switch (protocolField.getType()) {
            case Short:
                buffer.putShort(Short.parseShort(fieldObj + ""));
                break;
            case Int:
                buffer.putInt(Integer.parseInt(fieldObj + ""));
                break;
            case Long:
                buffer.putLong(Long.parseLong(fieldObj + ""));
                break;
            case ByteArray:
                if (fieldObj != null && !(fieldObj instanceof String)) {
                    logger.error("fieldObj<{}> should be String type, current type<{}>", fieldObj,
                        fieldObj.getClass().getName());
                    throw new FieldTypeWrongException();
                }
                byte[] byteArrayValue = ByteContractUtil.str2bytes((String) fieldObj, protocolField.getSize());
                buffer.put(byteArrayValue);
                break;
            default:
                logger.error("ByteContractProtocolField type<{}> not supported!", protocolField.getType());
                break;
        }

        return buffer;
    }

    private Object makeLoopCountType(int loopCount, DataEnum dataType) {
        Object loopCountInType = loopCount;
        switch (dataType) {
            case Short:
                loopCountInType = Short.valueOf(loopCount + "");
                break;
            case Long:
                loopCountInType = Long.valueOf(loopCount + "");
                break;
            case Int:
                break;
            default:
                logger.error("loop count type error<{}>", dataType);
                throw new FieldTypeWrongException();
        }
        return loopCountInType;
    }
}
