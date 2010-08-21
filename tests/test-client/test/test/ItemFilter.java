/*
 * ItemFilter.java
 *
 * Created on August 17, 2010, 6:00 PM
 * @author jaycverg
 */

package test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ItemFilter {
    
    private BigDecimal limit = new BigDecimal("200");
    private BigDecimal runningBalance = new BigDecimal("0");
    private List<Item> remainders = new ArrayList();
    private List<Item> flushed = new ArrayList();
    
    
    public boolean accept(Item item) {
        runningBalance = runningBalance.add(item.getAmount());
        if ( runningBalance.compareTo(limit) < 0 ) {
            flushed.add(item);
        } else {
//            RemainderItem item = new RemainderItem("txntype", "amount");
//            offset.add( item );
//            flushed.addAll( buffer );
//            buffer.clear();
        }
        return true;
    }
    
    
}
