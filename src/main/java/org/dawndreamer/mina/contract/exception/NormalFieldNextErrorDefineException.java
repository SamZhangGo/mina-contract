package org.dawndreamer.mina.contract.exception;

/**
 * <p>Title: NormalFieldNextErrorDefineException</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 10:21</p>
 */
public class NormalFieldNextErrorDefineException extends RuntimeException {

    public NormalFieldNextErrorDefineException() {
        super("Normal field next can't be Segment!");
    }
}
