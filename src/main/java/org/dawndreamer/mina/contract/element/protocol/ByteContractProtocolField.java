package org.dawndreamer.mina.contract.element.protocol;


import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author zhangsiming
 * {
 * "name":, //字段名
 * "size":, //字节大小
 * "type": ,//数据类型 int、short 、long、array
 * "func": //字段作用，1.正常占位normal 2.标识下一个元素的循环个数 loop
 * }
 */
public class ByteContractProtocolField extends AbstractByteContractProtocolElement {

    private int size;
    @JSONField(name = "type")
    private DataEnum type;
    private String desc;
    @JSONField(name = "func")
    private FuncEnum func;

    public ByteContractProtocolField() {
    }

    public ByteContractProtocolField(String name, int size, DataEnum type, FuncEnum func) {
        super();
        this.size = size;
        this.name = name;
        this.type = type;
        this.func = func;
    }

    public ByteContractProtocolField(String name, int size, DataEnum type, FuncEnum func, String desc) {
        super();
        this.size = size;
        this.name = name;
        this.type = type;
        this.desc = desc;
        this.func = func;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public DataEnum getType() {
        return type;
    }

    public void setType(DataEnum type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public FuncEnum getFunc() {
        return func;
    }

    public void setFunc(FuncEnum func) {
        this.func = func;
    }

    public boolean isNextLoop() {
        return func == FuncEnum.Loop;
    }

    @Override
    public int getContentLength() {
        return getSize();
    }

    @Override
    public String toString() {
        return "{" +
            "size=" + size +
            ", type=" + type +
            ", desc='" + desc + '\'' +
            ", func=" + func +
            ", name='" + name + '\'' +
            '}';
    }
}
