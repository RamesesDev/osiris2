package com.rameses.rcp.common;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.MessageUtil;

public final class MsgBox {
    
    private MsgBox() {}
    
    //another option
    public static void alert(Object msg) {
        ClientContext.getCurrentContext().getPlatform().showInfo(null, msg);
    }
    
    public static void err(Exception e) {
        ClientContext.getCurrentContext().getPlatform().showError(null, MessageUtil.getErrorMessage(e));
    }
    
    public static void err(Object message) {
        Exception e = new IllegalStateException(message.toString());
        ClientContext.getCurrentContext().getPlatform().showError(null, e);
    }
    
    public static void warn(Object msg) {
        ClientContext.getCurrentContext().getPlatform().showAlert(null, msg);
    }
    
    
    public static boolean confirm(Object msg) {
        return ClientContext.getCurrentContext().getPlatform().showConfirm(null, msg);
    }
    
    public static String prompt(Object msg) {
        return ""+ ClientContext.getCurrentContext().getPlatform().showInput(null, msg);
    }
    
}
