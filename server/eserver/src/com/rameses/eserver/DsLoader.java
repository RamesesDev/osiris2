/*
 * DsLoader.java
 *
 * Created on October 17, 2010, 12:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 * utility for loading datasources
 */
public final class DsLoader {
    
    private static DsLoader instance;
    
    public static DsLoader getInstance() {
        if(instance==null) {
            instance = new DsLoader();
        }
        return instance;
    }
    
    private String deployPath;
    private List<Map> items = new ArrayList();
    
    public void load() throws Exception {
        String home = System.getProperty("jboss.server.home.dir");
        deployPath = System.getProperty("output.path");
        if( deployPath == null ) deployPath = "/output";
        if( !deployPath.startsWith("/")) deployPath = "/" + deployPath;
        if( !deployPath.endsWith("/")) deployPath = deployPath + "/";
        deployPath = home + deployPath + "/datasources/";
        
        System.out.println("STARTING DS LOADER. Deploy at " + deployPath );
        File dir = new File( deployPath );
        if(!dir.exists()) dir.mkdirs();
        
        DataSource ds = AppContext.getSystemDs();
        SqlQuery sq = SqlManager.getInstance().createContext(ds).createNamedQuery("eserver:datasources");
        items = sq.setParameter(1, AppContext.getName() ).getResultList();
    }
    
    public void clearAll() {
        items.clear();
    }
    
    public void deploy() throws Exception {
        if(items.size()==0) load();
        for(Map map : items ) {
            persist( map );        
        }
    }
    
    public void undeploy() throws Exception {
        if(items.size()==0) load();
        for(Map map : items ) {
            remove( map );        
        }
    }
    
    private String getFixedName(String name) {
        String appCtx = AppContext.getName();
        if(appCtx.trim().length()>0) appCtx = appCtx + "-";
        return deployPath + appCtx +  name + "-ds.xml";
    }
    
    private void persist(Map map) {
        InputStream is = null;
        FileOutputStream fos = null;
        String name = null;
        try {
            String content = (String)map.get("content");
            name = (String)map.get("name");
            is = new ByteArrayInputStream(content.getBytes());
            String fileName = getFixedName(name);
            File f = new File(fileName);
            fos = new FileOutputStream(f);
            int i = 0;
            while( (i=is.read())!=-1 ) {
                fos.write( i );
            }
            fos.flush();
        } 
        catch(Exception e) {
            System.out.println("Failed to deploy " + name + ".Reason:" + e.getMessage());
        } 
        finally {
            try { is.close(); } catch(Exception ign){;}
            try { fos.close(); } catch(Exception ign){;}
        }
    }    
    
    public void remove(Map map) {
        String name = (String)map.get("name");
        String fileName = getFixedName(name) ;
        File f = new File(fileName);
        f.delete();
    }
    
    
}
