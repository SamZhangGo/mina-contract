package org.dawndreamer.mina.contract.utils;

/**
 * <p>Title: ByteContractUtil</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 13:46</p>
 */
public class ByteContractUtil {

    public static byte[] str2bytes(String str, int len) {
        if (len <= 0) {
            return null;
        }
        byte[] array = new byte[len];
        if (str == null || "".equals(str)) {
            return array;
        }
        byte[] src = str.getBytes();
        System.arraycopy(src, 0, array, 0, Math.min(len, src.length));
        return array;
    }
}
