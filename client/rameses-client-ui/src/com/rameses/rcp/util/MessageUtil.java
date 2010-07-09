/*
 * MessageUtil.java
 *
 * Created on June 18, 2010, 3:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.util;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.BusinessException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author jaycverg
 */
public class MessageUtil {
    
    public static String getMessage(String key, String defaultMsg, String bundleName) {
        if ( bundleName == null ) return defaultMsg;
        
        try {
            Locale locale = ClientContext.getCurrentContext().getLocale();
            String message = null;
            if ( locale == null ) {
                message = ResourceBundle.getBundle(bundleName).getString(key);
            } else {
                message = ResourceBundle.getBundle(bundleName, locale).getString(key);
            }
            
            if ( message != null) return message;
            
        } catch(Exception e) {;}
        
        return defaultMsg;
    }
    
    public static Exception getErrorMessage(Exception e) {
        if ( e instanceof BusinessException ) {
            BusinessException be = (BusinessException) e;
            String errno = be.getErrno();
            String msg = getMessage(errno, be.getMessage(), "errors");
            return new Exception(msg, be);
        }
        return e;
    }
    
}
