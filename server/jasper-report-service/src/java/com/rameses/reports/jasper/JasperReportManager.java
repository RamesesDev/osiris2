package com.rameses.reports.jasper;

import com.rameses.util.BusinessException;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class JasperReportManager {
    
    private static JasperReportManager instance;
    
    public static synchronized JasperReportManager getInstance() {
        if( instance == null ) {
            instance = new JasperReportManager();
        }
        
        return instance;
    }
    
    
    
    private static final String REPORT_DIR = "META-INF/reports/";
    
    private static final String KEY_REPORT_VERSION = "report.version";
    private static final String KEY_REPORT_IMAGES = "report.images";
    
    private static final String SERVER_RES_PREFIX = "server_res:";    
    private static final Pattern IMG_PATTERN = Pattern.compile(".*\\.(jpg|jpeg|gif|png|bmp)$", Pattern.CASE_INSENSITIVE);
    
    private Map<String, Map> cache;
    
    
    public JasperReportManager() {
        cache = new Hashtable();
    }
    
    public void flush(String name) {
        cache.remove(name);
    }
    
    public void flushAll() {
        cache.clear();
    }
    
    public synchronized Map getReport(String name, Double version) {
        Map report = cache.get(name);
        if( report == null ) {
            InputStream is = null;
            try {
                is = getReportResource( name +".rpt/report.conf" );
                if( is == null )
                    throw new BusinessException("404", "resource " + name + " not found ");
                
                Properties props = new Properties();
                props.load(is);
                
                //check report version
                //do not compile or load if client version is up to date
                if( clientIsUpToDate(version, props) )
                    return null;
                
                report = pack(name, props);
                cache.put( name, report );
                
            } catch(Exception ign){
                throw new RuntimeException(ign);
            } finally{
                try { is.close(); } catch(Exception e){;}
            }
        }
        
        //check report version
        //if client version is up to date just return null
        //to speed up response travel time
        if( clientIsUpToDate(version, report) )
            return null;
        
        Map images = (Map) report.get(KEY_REPORT_IMAGES);
        if( images != null ) {
            report = loadImages(report, images);
        }
        
        return report;
    }
    
    public Map getReport(String name) {
        return getReport(name, null);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private boolean clientIsUpToDate(Double clientVersion, Map report) {
        if(clientVersion == null) return false;
        
        Object rptVersion = report.get(KEY_REPORT_VERSION);
        if( rptVersion == null ) return false;
        
        try {
            double serverVersion = 0.00;
            if( rptVersion instanceof Double )
                serverVersion = (Double) rptVersion;
            else
                serverVersion = Double.parseDouble( rptVersion+"" );
            
            return clientVersion.doubleValue() >= serverVersion;
            
        } catch(Exception e) {;}
        
        return false;
    }
    
    private Map pack( String reportName, Properties props ) {
        Map m = new HashMap();
        Map imageIndex = null;
        for(Map.Entry me: props.entrySet()) {
            String key = me.getKey()+"";
            String value = me.getValue()+"";
            if( value.endsWith(".jrxml")) {
                InputStream is = null;
                try{
                    is = getReportResource( reportName +".rpt/" + value );
                    JasperReport jr = JasperCompileManager.compileReport(is);
                    m.put(key, jr);
                } catch(Exception ex){
                    System.out.println(ex.getMessage());
                } finally {
                    try {is.close(); } catch(Exception ign){;}
                }
            } else if( value.endsWith(".jasper")) {
                InputStream is = null;
                try{
                    is = getReportResource( reportName +".rpt/" + value );
                    JasperReport jr = (JasperReport) JRLoader.loadObject(is);
                    m.put(key, jr);
                } catch(Exception ex){
                    System.out.println(ex.getMessage());
                } finally {
                    try {is.close(); } catch(Exception ign){;}
                }
            } else if ( KEY_REPORT_VERSION.equals(key) ) {
                Object version = me.getValue();
                if( version == null ); //do nothing
                else if( !(version instanceof Double) ) {
                    try {
                        m.put(key, Double.parseDouble(version+""));
                    } catch(Exception e){}
                }
            } else if ( IMG_PATTERN.matcher(value).matches() ) {
                URL res = null;
                String fileLocation = null;
                if( value.startsWith("/")) {
                    res = getReportResourceURL( value );
                    fileLocation = value;
                } else {
                    res = getReportResourceURL( reportName +".rpt/" + value );
                    fileLocation = reportName + "/" + value;
                }
                
                //just index the resources that are available
                if( res != null ) {
                    if( imageIndex == null ) imageIndex = new HashMap();
                    imageIndex.put(fileLocation, res);
                    
                    m.put(key, SERVER_RES_PREFIX + fileLocation);
                    
                } else {
                    m.put(key, me.getValue());
                }
                
            } else {
                m.put(key, me.getValue());
            }
        }
        
        if( imageIndex != null )
            m.put(KEY_REPORT_IMAGES, imageIndex);
        
        return m;
    }
    
    private Map loadImages(Map report, Map imgIndex) {
        //create new map that will contain the actual image bytes instead of the URL's
        Map m = new HashMap(report);
        m.remove(KEY_REPORT_IMAGES);
        
        Map images = new HashMap();
        for(Map.Entry img : (Set<Map.Entry>)imgIndex.entrySet()) {
            URL u = (URL) img.getValue();
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            ByteArrayOutputStream bos = null;
            try {
                fis = new FileInputStream(new File(u.toURI()));
                bis = new BufferedInputStream(fis, 10240);
                
                byte[] buffer = new byte[10240];
                int len = -1;
                
                bos = new ByteArrayOutputStream();
                while( (len = bis.read(buffer)) != -1 ) {
                    bos.write(buffer);
                }
                
                images.put(img.getKey(), bos.toByteArray());
                
            } catch(Exception e) {
            } finally {
                try { fis.close(); }catch(Exception e){}
                try { bis.close(); }catch(Exception e){}
            }
            
        }
        
        if( images.size() > 0 )
            m.put(KEY_REPORT_IMAGES, images);
        
        return m;
    }
    
    private InputStream getReportResource(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream( REPORT_DIR + name );
    }
    
    private URL getReportResourceURL(String name) {
        return Thread.currentThread().getContextClassLoader().getResource( REPORT_DIR + name );
    }
    //</editor-fold>
    
}
