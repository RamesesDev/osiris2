/*
 * JasperServerCache.java
 *
 * Created on August 4, 2009, 11:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.reports.jasper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import org.jboss.annotation.ejb.Management;
import org.jboss.annotation.ejb.Service;


@Service(objectName="rameses:jasper=JasperServiceCache")
@Management(JasperReportCacheMBean.class)
public class JasperReportCache implements JasperReportCacheMBean {
    
    private Map<String, Map> map = new Hashtable<String, Map>(); 
    
    public JasperReportCache() {
    }

    public Map getReport(String name) {
        if( !map.containsKey(name)) {
            InputStream is = null;
            try {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream( "META-INF/reports/"+name +".rpt/report.conf" );
                if( is == null )
                    throw new Exception("resource " + name + " not found ");
                Properties props = new Properties();
                props.load(is);
                map.put( name, pack(name, props) );
            }
            catch(Exception ign){
                throw new IllegalStateException(ign);
            }
            finally{ try { is.close(); } catch(Exception e){;} }
        }
        return map.get(name);
    }
    
    
    private Map pack( String reportName, Properties props ) {
        Map m = new HashMap();
        for(Map.Entry me: props.entrySet()) {
            String fileName = me.getValue()+"";
            if( fileName.endsWith(".jrxml")) {
                InputStream is = null;
                try{
                    is = Thread.currentThread().getContextClassLoader().getResourceAsStream( "META-INF/reports/"+reportName +".rpt/" + fileName );
                    JasperReport jr = JasperCompileManager.compileReport(is);
                    m.put(me.getKey(), jr);
                }
                catch(Exception ex){
                    System.out.println(ex.getMessage());
                }
                finally {
                    try {is.close(); } catch(Exception ign){;}
                }
            }
            else {
                m.put(me.getKey(), me.getValue());
            }
        }
        return m;
    }

    public void start() {
        System.out.println("STARTING JASPER SERVICE CACHE");
    }

    public void stop() {
        System.out.println("STOPPING JASPER SERVICE CACHE");
    }

    public void flushAll() {
        map.clear();
    }

    public void flush(String name) {
        map.remove(name);
    }
    
}
