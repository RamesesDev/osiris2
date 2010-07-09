/*
 * OsirisAppLoader.java
 *
 * Created on October 27, 2009, 3:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.AppContext;
import com.rameses.platform.interfaces.AppLoader;
import com.rameses.platform.interfaces.MainWindow;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ClientSecurityProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsirisAppLoader implements AppLoader {
    
    public OsirisAppLoader() {
    }
    
    public void load(ClassLoader loader, Map env, MainWindow m) {
        try {
            if( env ==null ) {
                env = new HashMap();
            }
            
            OsirisAppContext dac = new OsirisAppContext(loader);
            dac.setEnvMap( env );
            dac.load();
            
            AppContext.setInstance(dac);
            OsirisSessionContext startupApp = (OsirisSessionContext)dac.createSession();
            
            ClientSecurityProvider cs = (ClientSecurityProvider)startupApp.getSecurityProvider();
            ClientContext ctx = ClientContext.getCurrentContext();
            ctx.setSecurityProvider(cs);
            ctx.getPlatform().setMainWindow(m);
            ctx.setClassLoader(loader);
            ctx.setMethodResolver( new OsirisMethodResolver() );
            
            OsirisContext.setSession(startupApp);
            
            //set the URL StreamHandler
            
            //load all loaders
            List loaders = startupApp.getInvokers("loader", false);
            if( loaders.size() > 0 ) {
                //Collections.reverse(loaders);
//                UIControllerPanel uip = new UIControllerPanel();
//                UILoaderStack<UIControllerContext> uls = new UILoaderStack<UIControllerContext>();
//                uls.setLoaders( loaders );
//                uip.setStack( uls );
//                
//                uip.initStack();
//                env.put("canclose", "false" );
//                env.put("title", "Home");
//                m.addWindow("home", uip, env);
            }
            startupApp.load();
            ctx.getTaskManager().start();
            
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
}
