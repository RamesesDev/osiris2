package com.rameses.eserver;

import com.rameses.scripting.ScriptServiceLocal;
import javax.naming.InitialContext;

public final class ScriptServiceDelegate {
    
    private static ScriptServiceLocal scriptService;
    
    public static ScriptServiceLocal getScriptService() throws Exception {
        if(scriptService==null) {
            InitialContext ctx = new InitialContext();
            scriptService = (ScriptServiceLocal)ctx.lookup( AppContext.getPath() + ScriptService.class.getSimpleName() + "/local" );
        }
        return scriptService;
    }
    
}
