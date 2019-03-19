package org.dawndreamer.mina.contract.element.protocol;

/**
 * @author zhangsiming
 */
public abstract class AbstractByteContractProtocolElement implements ByteContractElement {
    protected String name;
    
    
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
