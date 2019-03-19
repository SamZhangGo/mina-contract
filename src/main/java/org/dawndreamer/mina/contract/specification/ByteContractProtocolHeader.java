package org.dawndreamer.mina.contract.specification;


import org.dawndreamer.mina.contract.constant.ContractConst.Header;
import org.dawndreamer.mina.contract.element.protocol.ByteContractProtocolField;
import org.dawndreamer.mina.contract.element.protocol.ByteContractProtocolSegment;
import org.dawndreamer.mina.contract.element.protocol.DataEnum;
import org.dawndreamer.mina.contract.element.protocol.FuncEnum;

/**
 * @author zhangsiming
 */
public class ByteContractProtocolHeader extends ByteContractProtocolSegment {

    public ByteContractProtocolHeader() {
        super();
        addElement(new ByteContractProtocolField(Header.VERSION, 32, DataEnum.ByteArray, FuncEnum.Normal));
        addElement(new ByteContractProtocolField(Header.RESERVE, 4, DataEnum.ByteArray, FuncEnum.Normal));
        addElement(new ByteContractProtocolField(Header.HANDLER, 2, DataEnum.Short, FuncEnum.Normal));
        addElement(new ByteContractProtocolField(Header.CONTENT_LENGTH, 4, DataEnum.Int, FuncEnum.Normal));
    }


}
