package com.rameses.reports.jasper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
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
    private static final String REPORT_VERSION = "report.version";
    
    private Map<String, Map> cache;
    
    
    public JasperReportManager() {
        cache = new Hashtable();
    }
    
    public synchronized Map getReport(String name, Double version) {
        Map report = cache.get(name);
        if( report == null ) {
            InputStream is = null;
            try {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream( REPORT_DIR + name +".rpt/report.conf" );
                if( is == null )
                    throw new Exception("resource " + name + " not found ");
                
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
        //do not compile or load if client version is up to date
        if( clientIsUpToDate(version, report) )
            return null;
        
        return report;
    }
    
    public Map getReport(String name) {
        return getReport(name, null);
    }
    
    private boolean clientIsUpToDate(Double clientVersion, Map report) {
        if(clientVersion == null) return false;
        
        Object rptVersion = report.get(REPORT_VERSION);
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
        for(Map.Entry me: props.entrySet()) {
            String key = me.getKey()+"";
            String fileName = me.getValue()+"";
            if( fileName.endsWith(".jrxml")) {
                InputStream is = null;
                try{
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream( "META-INF/reports/"+reportName +".rpt/" + fileName );
                    JasperReport jr = JasperCompileManager.compileReport(is);
                    m.put(key, jr);
                } catch(Exception ex){
                    System.out.println(ex.getMessage());
                } finally {
                    try {is.close(); } catch(Exception ign){;}
                }
            } else if( fileName.endsWith(".jasper")) {
                InputStream is = null;
                try{
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream( "META-INF/reports/"+reportName +".rpt/" + fileName );
                    JasperReport jr = (JasperReport) JRLoader.loadObject(is);
                    m.put(key, jr);
                } catch(Exception ex){
                    System.out.println(ex.getMessage());
                } finally {
                    try {is.close(); } catch(Exception ign){;}
                }
            } else if ( REPORT_VERSION.equals(key) ) {
                Object value = me.getValue();
                if( value == null ); //do nothing
                else if( !(value instanceof Double) ) {
                    try {
                        m.put(key, Double.parseDouble(value+""));
                    }
                    catch(Exception e){}
                }
            } else {
                m.put(key, me.getValue());
            }
        }
        return m;
    }
    
    public void flush(String name) {
        cache.remove(name);
    }
    
    public void flushAll() {
        cache.clear();
    }
    
}
