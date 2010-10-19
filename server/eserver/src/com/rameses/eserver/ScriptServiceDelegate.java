package com.rameses.eserver;

import com.rameses.scripting.ScriptServiceLocal;
import com.rameses.eserver.AppContext;
import javax.naming.InitialContext;

public final class ScriptServiceDelegate {
    
    public static ScriptServiceLocal getScriptService() throws Exception {
        InitialContext ctx = new InitialContext();
        return (ScriptServiceLocal)ctx.lookup( AppContext.getName() + ScriptService.class.getSimpleName() + "/local" );
    }
    
}
