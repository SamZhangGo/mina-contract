package org.dawndreamer.mina.contract.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dawndreamer.mina.contract.codec.MinaCodecFactory;
import org.dawndreamer.mina.contract.constant.ContractConst;
import org.dawndreamer.mina.contract.constant.ContractConst.Header;
import org.dawndreamer.mina.contract.element.data.ByteContract;
import org.dawndreamer.mina.contract.element.data.ByteContractSegmentData;
import org.dawndreamer.mina.contract.element.protocol.ProtocolEnum;
import org.dawndreamer.mina.contract.exception.TooMoreSpecificationException;
import org.dawndreamer.mina.contract.handler.BusinessHandler;
import org.dawndreamer.mina.contract.specification.ByteContractProtocolBody;
import org.dawndreamer.mina.contract.specification.ByteContractProtocolHeader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangsiming
 */
public class ByteContractFactory {

    private static final String HEX_PREFIX = "0x";

    private static final Logger logger = LoggerFactory.getLogger(ByteContractFactory.class);
    private static ByteContractFactory factory;
    private GeneralSegmentDecoder segmentDecoder;
    private GeneralSegmentEncoder segmentEncoder;
    private Map<Short, BusinessHandler> handlerMap = new HashMap<>();

    private final Map<Integer, JSONArray> bodyReceiveProtocolMap = new HashMap<>();
    private final Map<Integer, JSONArray> bodySendProtocolMap = new HashMap<>();

    private ByteContractFactory() {
        segmentDecoder = new GeneralSegmentDecoder();
        segmentEncoder = new GeneralSegmentEncoder();
        boolean reverse = MinaCodecFactory.getInstance().isReverseReceiveAndSendProtocol();
        String receiveProtocolPath = "mina/bodyReceiveProtocol.json";
        String sendProtocolPath = "mina/bodySendProtocol.json";
        if (reverse) {
            String tmp = receiveProtocolPath;
            receiveProtocolPath = sendProtocolPath;
            sendProtocolPath = tmp;
        }
        InputStream receiveInputStream = this.getClass().getClassLoader().getResourceAsStream(receiveProtocolPath);
        if (receiveInputStream != null) {
            readJsonFileToMap(receiveInputStream, bodyReceiveProtocolMap);
        } else {
            logger.error("receive protocol file{} is not exist!", receiveProtocolPath);
        }
        InputStream sendInputStream = this.getClass().getClassLoader().getResourceAsStream(sendProtocolPath);
        if (sendInputStream != null) {
            readJsonFileToMap(sendInputStream, bodySendProtocolMap);
        } else {
            logger.error("send protocol{} is not exist!", sendProtocolPath);
        }
        registerHandler();
    }

    private void readJsonFileToMap(InputStream jsonFile, Map<Integer, JSONArray> map) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(jsonFile));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            JSONObject protocolSetObj = JSON.parseObject(sb.toString());
            protocolSetObj.keySet().forEach(key -> {
                JSONArray array = protocolSetObj.getJSONArray(key);
                if (array == null || array.size() == 0) {
                    logger.warn("key<{}> has no protocols defined!", key);
                } else {
                    logger.info("");
                    try {
                        key = key.toLowerCase().trim();
                        if (key.startsWith(HEX_PREFIX)) {
                            key = key.substring(2);
                        }
                        int keyInt = Integer.valueOf(key, 16);
                        logger.info("register body protocol success according to key<{}>.", key);
                        map.put(keyInt, array);
                    } catch (NumberFormatException e) {
                        logger.error("key<{}> convert to Integer failed! ", key);
                    }
                }
            });
        } catch (FileNotFoundException e) {
            logger.error("File not found!: ", e);
        } catch (IOException e) {
            logger.error("IOException：", e);
        }
    }

    private void registerHandler() {

    }

    public static ByteContractFactory getFactory() {
        if (factory == null) {
            synchronized (ByteContractFactory.class) {
                if (factory == null) {
                    factory = new ByteContractFactory();
                }
            }
        }
        return factory;
    }

    public ByteContractSegmentData assembleHeader(IoBuffer in) {
        return segmentDecoder.decodeSegment(in, new ByteContractProtocolHeader());
    }

    public ByteContractSegmentData assembleBody(short handler, int contentLength, IoBuffer in) {
        ByteContractProtocolBody bodySpecification = pickBodySpecification(handler, contentLength,
                ProtocolEnum.RECEIVE);
        ByteContractSegmentData body = null;
        if (bodySpecification == null) {
            logger.error("can't find body specification for handler<{}>, contentLength<{}>",
                    handler, contentLength);
        } else {
            logger.info("body specification handle<{}>,length<{}>: {}", handler, bodySpecification.getContentLength(),
                    bodySpecification);
            body = segmentDecoder.decodeSegment(in, bodySpecification);
        }
        return body;
    }

//    public ByteContract assembleContract(IoBuffer in) {
//        ByteContractSegmentData header = segmentDecoder.decodeSegment(in, new ByteContractProtocolHeader());
//        int contentLength = header.getIntValue(Header.CONTENT_LENGTH);
//        ByteContractProtocolBody bodySpecification = pickBodySpecification(header.getShort("handler"), contentLength,
//            ProtocolEnum.RECEIVE);
//        ByteContractSegmentData body = null;
//        if (bodySpecification == null) {
//            logger.error("can't find body specification for handler<{}>, contentLength<{}>",
//                header.getShort(Header.HANDLER), contentLength);
//        } else {
//            body = segmentDecoder.decodeSegment(in, bodySpecification);
//        }
//
//        return new ByteContract(header, body);
//    }

    public boolean writeContractToBuffer(IoBuffer buffer, ByteContract byteContract) {

        short handler = byteContract.getHeader().getShort(Header.HANDLER);
        int contentLength = byteContract.getHeader().getIntValue(Header.CONTENT_LENGTH);
        ByteContractProtocolBody bodySpecification = pickBodySpecification(handler, contentLength, ProtocolEnum.SEND);
        if (bodySpecification == null) {
            logger.error("can't find body specification for handler<{}>, contentLength<{}>",
                    handler, contentLength);
            return false;
        }
        if (!bodySpecification.isLengthConst()) {
            contentLength = bodySpecification.getRealContentLength(byteContract.getBody());
            byteContract.getHeader().put(Header.CONTENT_LENGTH, contentLength);
        }
        segmentEncoder.encodeSegment(buffer, byteContract.getHeader(), new ByteContractProtocolHeader());
        segmentEncoder.encodeSegment(buffer, byteContract.getBody(), bodySpecification);
        return true;
    }

    private ByteContractProtocolBody pickBodySpecification(short handler, int contentLength,
                                                           ProtocolEnum protocolType) {
        logger
                .info("look up for specification: handler<{}>, contentLength<{}>, protocolType<{}>", handler, contentLength,
                        protocolType);
        Map<Integer, JSONArray> map = null;
        switch (protocolType) {
            case RECEIVE:
                map = bodyReceiveProtocolMap;
                break;
            case SEND:
                map = bodySendProtocolMap;
                break;
            default:
                logger.error("protocolType<{}> not support!!", protocolType);
        }
        if (map == null) {
            return null;
        }
        Integer handlerKey = Integer.valueOf(handler + "");
        JSONArray array = map.get(handlerKey);
        if (array == null || array.size() == 0) {
            logger.error("handler<{}> has no body protocol set of type<{}>!!!", handler, protocolType);
            return null;
        }
        if (array.size() == 1) {
            JSONArray protocol = array.getJSONArray(0);
            ByteContractProtocolBody bodyProtocol = new ByteContractProtocolBody();
            bodyProtocol.getContentLength();
            bodyProtocol.parseSegment(protocol);
            return bodyProtocol;
        } else {
            ByteContractProtocolBody body = null;
            for (int i = 0; i < array.size(); i++) {
                JSONArray protocol = array.getJSONArray(i);
                ByteContractProtocolBody bodyProtocol = new ByteContractProtocolBody();
                bodyProtocol.parseSegment(protocol);
                int length = bodyProtocol.getContentLength();
                if (length == contentLength) {
                    body = bodyProtocol;
                    break;
                } else if (length == ContractConst.BODY_CONTENT_LENGTH_VARIABLE) {
                    logger.error("can't define more than one VARIABLE_LENGTH body specification.");
                    throw new TooMoreSpecificationException();
                }
            }
            if (body == null) {
                logger.error("handler<{}> has no body protocol set of type<{}> and length<{}>!!!", handler, protocolType, contentLength);
                return null;
            }
            logger.info("body specification found: {}", body);
            return body;
        }
    }
}
