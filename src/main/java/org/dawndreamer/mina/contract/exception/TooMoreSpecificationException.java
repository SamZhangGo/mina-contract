package org.dawndreamer.mina.contract.exception;

/**
 * <p>Title: TooMoreSpecificationException</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/17 23:50</p>
 */
public class TooMoreSpecificationException extends RuntimeException {

    public TooMoreSpecificationException() {
        super("can't define more than one specification if variable-length one has been defined.");
    }
}
