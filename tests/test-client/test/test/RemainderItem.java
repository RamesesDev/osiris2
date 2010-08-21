/*
 * RemainderItem.java
 *
 * Created on August 17, 2010, 6:27 PM
 * @author jaycverg
 */

package test;

import java.math.BigDecimal;


public class RemainderItem implements Item {
    
    private BigDecimal amount;
    private String txntype;
    
    
    public RemainderItem(String txntype, BigDecimal amount) {
        this.txntype = txntype;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getTxntype() {
        return txntype;
    }

    public void setTxntype(String txntype) {
        this.txntype = txntype;
    }
    
}
