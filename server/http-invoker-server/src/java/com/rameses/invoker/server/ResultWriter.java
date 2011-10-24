/*
 * ResultWriter.java
 * Created on September 20, 2011, 7:37 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.server;

import com.rameses.web.common.ServletUtils;
import com.rameses.util.ExceptionManager;
import com.rameses.util.SealedMessage;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class ResultWriter {
    
    public static void print(HttpServletResponse resp, Object result, boolean encrypt, String contentType ) {
        if(result == null) {
            result = "#NULL";
        } else if( result instanceof Exception ) {
            result = ExceptionManager.getOriginal((Exception)result);
        }
        
        //check if secured transfer. If secured, we must wrap it
        if( encrypt ) {
            result = new SealedMessage(result);
        }
        //prepare to write the output
        if((result instanceof String) || ( contentType!=null && contentType.indexOf("text")>=0)) {
            ServletUtils.writeText(resp, (String)result);
        } else {
            ServletUtils.writeObject(resp, result);
        }
    }
}
