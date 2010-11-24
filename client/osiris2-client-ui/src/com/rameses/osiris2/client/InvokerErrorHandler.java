/*
 * InvokerErrorHandler.java
 *
 * Created on November 24, 2010, 3:15 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.util.ExceptionHandler;
import com.rameses.util.ExceptionManager;
import com.rameses.util.Warning;
import java.util.List;


public class InvokerErrorHandler extends ExceptionHandler {
    
    public boolean accept(Exception e) {
        e = ExceptionManager.getOriginal(e);
        if ( e instanceof Warning && e.getMessage() != null ) {
            List invs = InvokerUtil.lookup( e.getMessage() );
            if ( invs.size() > 0 ) {
                InvokerUtil.invoke( (Invoker) invs.get(0) );
                return true;
            }
        }
        return false;
    }
    
}
