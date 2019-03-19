package org.dawndreamer.mina.contract.exception;

/**
 * <p>Title: FieldTypeWrongException</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 0:57</p>
 */
public class FieldTypeWrongException extends RuntimeException {

    public FieldTypeWrongException() {
        super("field type wrong");
    }
}
