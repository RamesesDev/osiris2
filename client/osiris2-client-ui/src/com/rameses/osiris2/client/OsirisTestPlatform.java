/*
 * OsirisTestPlatform.java
 *
 * Created on October 27, 2009, 5:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ClientContext;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class OsirisTestPlatform {
    
    public static void setEnv(Map env) {
        ClientContext.getCurrentContext().getHeaders().putAll(env);
    }
    
    public static void runTest(Map map) throws Exception {
        if ( map == null ) map = new HashMap();
        map.put("app.title", "Osiris Test Platform");
        
        OsirisAppLoader loader = new OsirisAppLoader();
        Platform platform = ClientContext.getCurrentContext().getPlatform();
        loader.load(Thread.currentThread().getContextClassLoader(), map, platform);
        platform.getMainWindow().show();
    }
    
    
}
