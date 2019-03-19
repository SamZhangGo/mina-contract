package org.dawndreamer.mina.contract.constant;

/**
 * @author zhangsiming
 */
public class ContractConst {

    public static final int BYTE_LEN_SHORT = 2;
    public static final int BYTE_LEN_INT = 4;
    public static final int BYTE_LEN_LONG = 8;

    public static final int BYTE_LEN_VERSION = 32;
    public static final int BYTE_LEN_RESERVE = 4;

    public static final int BODY_CONTENT_LENGTH_VARIABLE = Integer.MAX_VALUE;

    public static class Header {
        public static final int LENGTH = 42;
        public static final String VERSION = "version";
        public static final String RESERVE = "reserve";
        public static final String HANDLER = "handler";
        public static final String CONTENT_LENGTH = "contentLength";
    }
}
