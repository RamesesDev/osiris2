/*
 * ScriptMgmt.java
 *
 * Created on October 16, 2010, 8:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;


import com.rameses.schema.SchemaManager;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptObject;
import com.rameses.server.common.AppContext;
import com.rameses.server.common.HtmlMap;
import com.rameses.server.common.JndiUtil;

import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;

public class ServerMgmt implements ServerMgmtMBean, Serializable {
    
    private final static String JNDI_NAME = "ServerMgmt";
    
    public void start() throws Exception {
        System.out.println("      Initializing Properties");
        AppContext.load();
        
        System.out.println("STARTING SERVER MGMT:" + AppContext.getName() );
        
        System.out.println("      Initializing Schema Manager");
        SchemaManager.setInstance( new SchemaManagerImpl() );
        
        //load datasources
        System.out.println("      Loading Datasources");
        DsLoader.getInstance().deploy();
        
        //load scripts
        System.out.println("      Initializing ScriptManager");
        ScriptManager.setInstance(new ScriptManagerImpl());
        ScriptManager.getInstance().load();
        
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, AppContext.getPath()+ ServerMgmt.class.getSimpleName(), this );
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING SERVER MANAGER:" + AppContext.getName());
        
        
        System.out.println("      Unloading ScriptManager");
        ScriptManager.getInstance().close();
        ScriptManager.setInstance(null);
        
        System.out.println("      Unloading DataSources");
        DsLoader.getInstance().undeploy();
        
        System.out.println("      Unloading Schema Manager");
        SchemaManager.setInstance( null );
        
        

        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, AppContext.getPath()+ ServerMgmt.class.getSimpleName() );

    }
    
    public void reloadScripts() {
        ScriptManager.getInstance().load();
    }
    
    public void reloadScript(String name) {
        ScriptManager.getInstance().reload(name);
    }
    
    public void reloadDataSources() throws Exception {
        DsLoader.getInstance().undeploy();
        DsLoader.getInstance().clearAll();
        DsLoader.getInstance().deploy();
    }
    
    public void reloadSchema() {
        SchemaManager.getInstance().getCache().clear();
        SqlManager.getInstance().getCache().clear();
    }
    
    public void reloadSchema(String name) {
        SchemaManager.getInstance().getCache().remove(name);
        //important to reload sql also.
        Map map = SqlManager.getInstance().getCache();
        synchronized(map) {
            List<String> list = new ArrayList();
            Iterator<String> iter = map.keySet().iterator();
            while(iter.hasNext()) {
                String s = iter.next();
                if(s.matches(name+".*")) list.add(s);
            }
            for(String s: list) map.remove(s);
        }
        
    }
    
    public String showAppProperties() {
        return new HtmlMap(AppContext.getProperties()).toString();
    }
    
    public String showScriptInfo(String name) {
        ScriptObject sm = ScriptManager.getInstance().getScriptObject(name);
        return  sm.toHtml();
    }
    
    
}
