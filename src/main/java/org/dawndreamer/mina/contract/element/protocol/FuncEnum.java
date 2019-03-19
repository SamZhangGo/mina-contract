package org.dawndreamer.mina.contract.element.protocol;

/**
 * <p>Title: FuncEnum</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/10 17:56</p>
 */
public enum FuncEnum {
    /**
     * 普通占位类型
     */
    Normal("Normal"),
    /**
     * 循环类型
     */
    Loop("Loop");

    private String name;

    FuncEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }}
