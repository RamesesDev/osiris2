/*
 * RemoteDelegate.java
 *
 * Created on July 27, 2010, 11:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.eserver.CONSTANTS;
import java.io.Serializable;
import java.util.Map;
import javax.naming.InitialContext;

/**
 *
 * @author elmo
 */
public class InvokerLookup implements Serializable {
    
    private String callerService;
    private Map env;
    
    public InvokerLookup(String callerService, Map env) {
        this.callerService = callerService;
        this.env = env;
    }
    
    public Object create(String scriptname) {
        return create(scriptname, null);
    }

    public Object create(String scriptname, String host) {
        try {
            InitialContext ctx = new InitialContext();
            ScriptMgmtMBean mbean = (ScriptMgmtMBean)ctx.lookup(CONSTANTS.SCRIPT_MGMT);
            if(host==null || host.trim().length()==0) {
                if(scriptname==null || scriptname.trim().length()==0) scriptname = callerService;
                return mbean.createLocalProxy(scriptname, env );
            }    
            else {
                if(scriptname==null || scriptname.trim().length()==0) 
                    throw new IllegalStateException("Please provide a remote service name value for @Service");
                return mbean.createRemoteProxy(scriptname, env, host );
            }
        }
        catch(Exception e) {
            return null;
        }
    }
    
    
    
}
