/*
 * SubSession.java
 *
 * Created on October 28, 2010, 5:07 PM
 * @author jaycverg
 */

import com.rameses.platform.interfaces.AppLoader;
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.workspace.WorkspaceWindow;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;


public class SubSession {
    
    public static void show(Map properties) {
        ClassLoader currentLoader = ClientContext.getCurrentContext().getClassLoader();
        if ( currentLoader instanceof URLClassLoader ) {
            try {
                URLClassLoader classLoader = (URLClassLoader) currentLoader;
                Map appEnv = ClientContext.getCurrentContext().getAppEnv();
                
                ClassLoader newLoader = new URLClassLoader( classLoader.getURLs(), classLoader.getParent() );
                
                AppLoader loader = (AppLoader) newLoader.loadClass( OsirisSubAppLoader.class.getName()  ).newInstance();
                Platform platform = new com.rameses.rcp.workspace.Workspace();
                
                Map env = new HashMap(appEnv);
                
                if ( properties.get("permissions") != null )
                    env.put("permissions", properties.get("permissions"));
                
                if ( properties.get("virtualEnv") != null )
                    env.put("virtualEnv", properties.get("virtualEnv"));
                
                loader.load( newLoader, env, platform);
                
                WorkspaceWindow mw = (WorkspaceWindow) platform.getMainWindow();
                mw.setTitle( properties.get("title")+"" );
                mw.setId( properties.get("id")+"" );
                mw.show();
                
            } catch(Exception e) {
                MsgBox.err(e);
            }
        }
    }
    
}
