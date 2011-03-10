/*
 * ServerReportUtil.java
 *
 * Created on February 28, 2011, 1:12 PM
 * @author jaycverg
 */

package com.rameses.osiris2.reports;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.invoker.client.HttpInvokerClient;
import com.rameses.invoker.client.HttpScriptService;
import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.client.OsirisContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ServerReportUtil {
    
    private static final String REPORT_EXT = ".rpt";
    private static final String KEY_MAIN_REPORT = "main";
    private static final String KEY_VERSION = "report.version";
    private static final String KEY_REPORT_IMAGES = "report.images";
    private static final String SERVER_RES_PREFIX = "server_res:";
    private static final String SERVICE = "jasper/ReportService";
    
    private static HttpScriptService scriptSvc;
    
    
    private static Map<String, Map> reportIndex = new HashMap();
    
    public static synchronized Map getReportConf(String name) throws Exception {
        Map report = reportIndex.get(name);
        if( report != null )
            return report;
        
        Map localConf = getLocalReport(name);
        Double version = null;
        if( localConf != null ) {
            version = (Double) localConf.get(KEY_VERSION);
        }
        
        Map serverConf = getServerReportConf(name, version);
        
        //if the server returns null it means that
        //local report is up to date
        if( serverConf == null )
            return localConf;
        
        //saved updated report
        saveLocalReport(name, serverConf);
        
        //cache updated report
        reportIndex.put(name, serverConf);
        
        
        return serverConf;
    }
    
    private static Map getLocalReport(String name) {
        ObjectInputStream ois = null;
        try {
            String localPath = System.getProperty("user.dir") + "/reports/";
            File f = new File(localPath + name + REPORT_EXT);
            if( !f.exists() ) return null;
            
            ois = new ObjectInputStream(new FileInputStream(f));
            return (Map) ois.readObject();
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{ ois.close(); }catch(Exception e){}
        }
        
        return null;
    }
    
    private static void saveLocalReport(String name, Map conf) {
        ObjectOutputStream oos = null;
        try {
            String localPath = System.getProperty("user.dir") + "/reports/";
            File f = new File(localPath + name + REPORT_EXT);
            if( !f.getParentFile().exists() ) {
                f.getParentFile().mkdirs();
            }
            
            Map images = (Map) conf.remove(KEY_REPORT_IMAGES);
            if( images != null ) {
                for(Map.Entry me : (Set<Map.Entry>)images.entrySet()) {
                    String path = localPath + me.getKey();
                    saveImage(path, (byte[]) me.getValue());
                }
                
                for(Map.Entry me : (Set<Map.Entry>)conf.entrySet()) {
                    if( me.getValue() instanceof String ) {
                        String path = me.getValue()+"";
                        if( path.startsWith(SERVER_RES_PREFIX) ) {
                            path = path.replace(SERVER_RES_PREFIX, "");
                            if( path.startsWith("/") )
                                path = localPath + path.substring(1);
                            else
                                path = localPath + path;
                            
                            me.setValue(path);
                        }
                    }
                }
            }
            
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(conf);
            oos.flush();
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{ oos.close(); }catch(Exception e){}
        }
        
    }
    
    private static void saveImage(String name, byte[] bytes) {
        FileOutputStream fos = null;
        try {
            File f = new File(name);
            if( !f.getParentFile().exists() ) {
                f.getParentFile().mkdirs();
            }
            
            fos = new FileOutputStream(f);
            fos.write(bytes);
            fos.flush();
            
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            try{ fos.close(); }catch(Exception e){}
        }
    }
    
    private static Map getServerReportConf(String name, Double version) throws Exception {
        if( scriptSvc == null ) {
            Map env = AppContext.getInstance().getEnv();
            HttpInvokerClient client = HttpClientManager.getInstance().getService(env);
            scriptSvc = new HttpScriptService(client);
        }
        
        return (Map) scriptSvc.invoke(SERVICE, "getReport", new Object[]{name, version}, OsirisContext.getEnv());
    }
    
}
