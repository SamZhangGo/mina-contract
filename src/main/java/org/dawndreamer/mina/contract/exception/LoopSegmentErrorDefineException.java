package org.dawndreamer.mina.contract.exception;

/**
 * <p>Title: LoopSegmentErrorDefineException</p>
 * <p>Description: Function Description</p>
 *
 * <p>@Author: zhangsiming</p>
 * <p>@Date: 2019/1/18 1:56</p>
 */
public class LoopSegmentErrorDefineException extends RuntimeException {

    public LoopSegmentErrorDefineException() {
        super("Loop segment must comply ByteContractProtocolSegment");
    }
}
