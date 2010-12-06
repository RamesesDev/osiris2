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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OsirisSubAppLoader implements AppLoader {
    
    public OsirisSubAppLoader() {
    }
    
    /**
     * if the env contains a key "permissions", the value will be added to the security provider's permissions
     * if the env contains a key "virtualEnv", the value will be added to the OsirisContext.env/ClientContext#headers
     */
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
            ctx.setAppEnv(env);
            
            Thread.currentThread().setContextClassLoader(loader);
            
            OsirisContext.setSession(startupApp);
            
            //if there are permissions specified from the calling platform
            Object perms = env.get("permissions");
            if ( perms != null && perms instanceof Collection ) {
                OsirisSecurityProvider sp = (OsirisSecurityProvider) OsirisContext.getSession().getSecurityProvider();
                sp.getPermissions().addAll( (Collection) perms );
            }
            
            //if there are virtualEnv specified from the calling platform
            Object virtualEnv = env.get("virtualEnv");
            if ( virtualEnv != null && virtualEnv instanceof Map ) {
                OsirisContext.getEnv().putAll( (Map) virtualEnv );
            }
            
            //load all loaders
            List loaders = startupApp.getInvokers("subloader", false);
            if( loaders.size() > 0 ) {
                //Collections.reverse(loaders);
                UIControllerPanel uip = new UIControllerPanel();
                UILoaderStack uls = new UILoaderStack();
                uls.setLoaders( loaders );
                uip.setControllers(uls);
                
                env.put("canclose", "false" );
                env.put("title", "Home");
                env.put("id", "loader_workunits");
                platform.showStartupWindow(null, uip, env);
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
