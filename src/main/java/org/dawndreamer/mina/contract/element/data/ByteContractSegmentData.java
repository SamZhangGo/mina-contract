package org.dawndreamer.mina.contract.element.data;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * <p>Title: ByteContractSegmentData</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/14 14:55</p>
 */
public class ByteContractSegmentData extends JSONObject{

    public void addArrayElement(String name, Object object) {
        JSONArray array = this.getJSONArray(name);
        if(array == null) {
            array = new JSONArray();
            this.put(name, array);
        }
        array.add(object);
    }

}
