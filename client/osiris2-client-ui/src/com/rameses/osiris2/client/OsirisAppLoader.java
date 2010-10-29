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
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ClientSecurityProvider;
import com.rameses.rcp.framework.UIControllerPanel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsirisAppLoader implements AppLoader {
    
    public OsirisAppLoader() {
    }
    
    public void load(ClassLoader loader, Map env, Platform platform) {
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
            ctx.setPlatform(platform);
            ctx.setClassLoader(loader);
            ctx.setMethodResolver( new OsirisMethodResolver() );
            ctx.setAppEnv(env);
            
            OsirisContext.setSession(startupApp);

            //load all loaders
            List loaders = startupApp.getInvokers("loader", false);
            if( loaders.size() > 0 ) {
                //Collections.reverse(loaders);
                UIControllerPanel uip = new UIControllerPanel();
                UILoaderStack uls = new UILoaderStack();
                uls.setLoaders( loaders );
                uip.setControllers(uls);
                
                env.put("canclose", "false" );
                env.put("title", "Home");
                env.put("id", "loader_workunits");
                platform.showWindow(null, uip, env);
            }
            startupApp.load();
            ctx.getTaskManager().start();
            
            //attach Osiris2MainWindowListener
            platform.getMainWindow().setListener(OsirisContext.getMainWindowListener());
            
        } catch(Exception ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    public void restore() {
    }
    
}
