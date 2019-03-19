package org.dawndreamer.mina.contract.element.data;


import com.alibaba.fastjson.JSONObject;

/**
 * @author zhangsiming
 * {
 * header:[
 * {
 * name: version,
 * size: 32,
 * type: ByteArray,
 * func: Normal,
 * desc: 版本号
 * },
 * {
 * name: reserve,
 * size: 4,
 * type: ByteArray,
 * func: Normal,
 * desc: 保留字
 * },
 * {
 * name: handle,
 * size: 2,
 * type: Short,
 * func: Normal
 * },
 * {
 * name:contentLength,
 * size:4,
 * type:Int,
 * func:Normal
 * }],
 * body: [
 * ]
 * }
 */
public class ByteContract {

    private JSONObject header;
    private JSONObject body;

    public ByteContract(JSONObject header, JSONObject body) {
        super();
        this.header = header;
        this.body = body;
    }

    public JSONObject getHeader() {
        return header;
    }

    public void setHeader(JSONObject header) {
        this.header = header;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }
}
