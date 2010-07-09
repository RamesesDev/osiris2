package com.rameses.data.server;

import com.rameses.interfaces.ScriptServiceLocal;
import javax.naming.InitialContext;

public final class ServiceUtil {
    
    public static Object lookup(String s) {
        try {
            InitialContext ctx = new InitialContext();
            return (ScriptServiceLocal) ctx.lookup( s );
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
}
