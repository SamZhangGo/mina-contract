package org.dawndreamer.mina.contract.element.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dawndreamer.mina.contract.constant.ContractConst;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangsiming
 */
public class ByteContractProtocolSegment extends AbstractByteContractProtocolElement {

    private static final Logger logger = LoggerFactory.getLogger(ByteContractProtocolSegment.class);

    private int length;
    private List<ByteContractElement> content;

    public ByteContractProtocolSegment() {
        super();
    }

    public ByteContractProtocolSegment parseSegment(JSONArray array) {
        if (array != null && array.size() > 0) {
            array.forEach(item -> {
                ByteContractElement element = null;
                if (item instanceof JSONObject) {
                    JSONObject itemObj = (JSONObject) item;
                    element = JSON.toJavaObject(itemObj, ByteContractProtocolField.class);

                } else if (item instanceof JSONArray) {
                    JSONArray itemArray = (JSONArray) item;
                    ByteContractProtocolSegment segment = new ByteContractProtocolSegment();
                    segment.parseSegment(itemArray);
                    addElement(segment);
                } else {
                    logger.error("item class<> is not supported!!", item.getClass().getName());
                }
                if (element != null) {
                    addElement(element);
                }
            });
        }
        return this;
    }

    public void addElement(ByteContractElement ele) {
        if (content == null) {
            content = new ArrayList<>();
        }
        content.add(ele);
    }

    public List<ByteContractElement> getContent() {
        return content;
    }


    @Override
    public int getContentLength() {
        if (length > 0) {
            return length;
        }
        if (CollectionUtils.isEmpty(content)) {
            return length;
        }
        for (ByteContractElement element : content) {
            if (element instanceof ByteContractProtocolField) {
                if (((ByteContractProtocolField) element).isNextLoop()) {
                    length = ContractConst.BODY_CONTENT_LENGTH_VARIABLE;
                    return ContractConst.BODY_CONTENT_LENGTH_VARIABLE;
                }
            }
            length += element.getContentLength();
        }
        return length;
    }

    public boolean isLengthConst() {
        return length > 0 && length != ContractConst.BODY_CONTENT_LENGTH_VARIABLE;
    }

    public int getRealContentLength(JSONObject segmentData) {
        StringBuilder lengthSb = new StringBuilder();
        if (isLengthConst()) {
            return length;
        }
        length = 0;
        if (CollectionUtils.isEmpty(content)) {
            return length;
        }
        boolean isNextLoop = false;
        int loopCount = 0;
        for (ByteContractElement element : content) {
            if (element instanceof ByteContractProtocolField) {
                length += element.getContentLength();
                lengthSb.append(element.getContentLength()).append(" + ");
                if (((ByteContractProtocolField) element).isNextLoop()) {
                    isNextLoop = true;
                    loopCount = segmentData.getJSONArray(element.getName()).size();
                }
            } else if (element instanceof ByteContractProtocolSegment) {
                JSONObject childSegmentData = segmentData.getJSONObject(element.getName());
                int childSegmentRealLength = ((ByteContractProtocolSegment) element)
                    .getRealContentLength(childSegmentData);
                if (isNextLoop) {
                    length += loopCount * childSegmentRealLength;
                    lengthSb.append(loopCount).append(" * ").append(childSegmentData).append(" + ");
                    isNextLoop = false;
                    loopCount = 0;
                } else {
                    length += childSegmentRealLength;
                    lengthSb.append(childSegmentData).append(" + ");
                }
            }
        }
        lengthSb.delete(lengthSb.length() - 3, lengthSb.length());
        logger.info("length-><{}={}>", lengthSb, length);
        return length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n{\n");
        if (!CollectionUtils.isEmpty(content)) {
            for (ByteContractElement element : content) {
                sb.append(element.toString()).append("\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}
