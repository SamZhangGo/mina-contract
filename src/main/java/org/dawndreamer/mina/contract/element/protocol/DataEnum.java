package org.dawndreamer.mina.contract.element.protocol;

/**
 * <p>Title: DataEnum</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/10 17:56</p>
 */
public enum DataEnum {
    /**
     * Int类型
     */
    Int("Int"),
    /**
     * Short类型
     */
    Short("Short"),
    /**
     * Long类型
     */
    Long("Long"),
    /**
     * byte[]类型，最终转换为String
     */
    ByteArray("ByteArray");

    private String name;

    DataEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }}
