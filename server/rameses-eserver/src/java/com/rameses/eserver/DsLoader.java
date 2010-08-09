/*
 * DsLoader.java
 *
 * Created on August 7, 2010, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.interfaces.ResourceHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.naming.InitialContext;

/**
 *
 * @author elmo
 */
public class DsLoader implements DsLoaderMBean {
    
    private String rootPath = System.getProperty("jboss.server.home.dir");
    private String deployPath = "system/datasources";
    private ResourceServiceMBean resourceService;
    private List<String> dataSources;
    
    public DsLoader() {
    }
    
    /*
     * reloads all Ds resources
     */
    public void start() throws Exception {
        System.out.println("STARTING DS LOADER. Deploy at " + deployPath );
        File dir = new File( rootPath + "/" + deployPath + "/"  );
        if(!dir.exists()) dir.mkdirs();
        InitialContext ctx = new InitialContext();
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        dataSources = new ArrayList();
        DsLoaderHandler handler = new DsLoaderHandler();
        resourceService.scanResources( "ds-loader://loaders", handler);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING DS LOADERS");
        for( String s: dataSources ) {
            File f = new File(s);
            f.delete();
        }
        dataSources = null;
    }
    
    public void deploy( String name ) throws Exception {
        InputStream is = null;
        try {
            DsLoaderHandler handler = new DsLoaderHandler();        
            is = resourceService.getResource( "ds-loader://"+name);
            handler.handle(is, name);
        }
        catch(Exception e) {
            throw e;
        }
        finally {
            try { is.close(); } catch(Exception ign){;}
        }
    }
    
    public void undeploy(String name) throws Exception {
        String fileName = getFixedName(name) ;
        File f = new File(fileName);
        f.delete();
        dataSources.remove(fileName);
    }
    
    private String getFixedName(String name) {
        return rootPath + "/" + deployPath + "/" + name + "-ds.xml";
    }
    
    private class DsLoaderHandler implements ResourceHandler {
        
        public void handle(InputStream is, String resName) throws Exception {
            FileOutputStream fos = null;
            try {
                String fileName = getFixedName(resName);
                File f = new File(fileName);
                fos = new FileOutputStream(f);
                int i = 0;
                while( (i=is.read())!=-1 ) {
                    fos.write( i );
                }
                fos.flush();
                dataSources.add( fileName );
            } 
            catch(Exception e) {
                throw e;
            } 
            finally {
                try { fos.close(); } catch(Exception ign){;}
            }
        }
        
    }

    public String getDeployPath() {
        return deployPath;
    }

    public void setDeployPath(String deployPath) {
        this.deployPath = deployPath;
    }

    
    
    
    
}
