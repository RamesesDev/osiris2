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
import com.rameses.sql.SqlManager;
import com.rameses.util.TemplateProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServerMgmt implements ServerMgmtMBean {
    
    public void start() throws Exception {
        System.out.println("STARTING SERVER MGMT:" + AppContext.getName() );

        System.out.println("      Loading Template Manager");
        TemplateProvider.setInstance(new TemplateProviderImpl());
        
        System.out.println("      Initializing Schema Manager");
        SchemaManager.setInstance( new SchemaManagerImpl() );
        
        //load datasources
        System.out.println("      Loading Datasources");
        DsLoader.getInstance().deploy();
        
        //load scripts
        System.out.println("      Loading ScriptManager");
        ScriptManager.setInstance(new ScriptManagerImpl());
        ScriptManager.getInstance().load();
        

    }

    public void stop() throws Exception {
        System.out.println("STOPPING SERVER MANAGER:" + AppContext.getName());

        System.out.println("      Unloading ScriptManager");
        ScriptManager.setInstance(null);
        
        System.out.println("      Unloading DataSources");
        DsLoader.getInstance().undeploy();

        System.out.println("      Unloading Schema Manager");
        SchemaManager.setInstance( null );

        System.out.println("      Unloading Template Provider");
        TemplateProvider.setInstance( null );
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

    public void reloadTemplates() {
        TemplateProvider.getInstance().clear(null);
    }

    public void reloadTemplate(String name) {
        TemplateProvider.getInstance().clear(name);
    }

    
    
    
}
