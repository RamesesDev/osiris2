package com.rameses.osiris2.reports;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;

/**
 * @author jaycverg
 */
public abstract class ReportExporter {
    
    public static Properties index = new Properties();
    public static Map<String, ReportExporter> exporters = new HashMap();
    
    static {
        try {
            Enumeration e = ReportExporter.class.getClassLoader().getResources("META-INF/report-exporters.properties");
            while( e.hasMoreElements() ) {
                URL u = (URL) e.nextElement();
                index.load( u.openStream() );
            }
            
        }catch(Exception e) {;}
    }
    
    public static final ReportExporter get(String name) {
        if ( exporters.get(name) == null && index.containsKey(name) ) {
            try {
                ClassLoader loader = ReportExporter.class.getClassLoader();
                exporters.put(name, (ReportExporter) loader.loadClass(index.getProperty(name)).newInstance());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } 
        }
        return exporters.get(name);
    }
    
    
    public abstract JRExporter getExporter();
    
    public final void export(ReportModel model, String output) {
        try {
            export(model, new FileOutputStream(output));
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public final void export(ReportModel model, OutputStream output) {
        OutputStream os = null;
        try{
            JasperPrint jp = model.getReport();
            JRExporter exporter = getExporter();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (os = output) );
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.exportReport();
        }catch(Exception e){
            throw new RuntimeException(e);
        } finally{
            try{os.close();}catch(Exception ign){}
        }
    }
    
}
