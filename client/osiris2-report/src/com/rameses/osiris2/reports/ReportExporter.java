package com.rameses.osiris2.reports;

import groovy.lang.Writable;
import groovy.text.GStringTemplateEngine;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * @description
 * cell height and width settings: new Column(name:"name", caption:"Column Title", properties:[width:150, height:15]);
 * height must not be greater than 22;
 */

public abstract class ReportExporter {
    
    public abstract JRExporter getExporter();
    public abstract OutputStream getOutputStream();
    public abstract Object getData();
    public abstract List getColumns();
    
    
    public final void export() throws Exception {
        Writable w = null;
        OutputStream os = null;
        try{
            Map binding = new HashMap();
            binding.put("columns", getColumns() );
            URL url = getClass().getClassLoader().getResource("com/rameses/osiris2/reports/templates/report-exporter.template");
            GStringTemplateEngine engine = new GStringTemplateEngine();
            w = engine.createTemplate(url).make(binding);
            
            JasperPrint jp = getReport( w );
            
            JRExporter exporter = getExporter();
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, (os = getOutputStream()) );
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.exportReport();
        }catch(Exception e){
            throw e;
        } finally{
            try{os.close();}catch(Exception ign){}
        }
    }
    
    private JasperPrint getReport( Writable w )throws Exception{
        ByteArrayInputStream bais = null;
        try{
            String template = w.toString();
            bais = new ByteArrayInputStream(template.getBytes("UTF-8"));
            
            if ( isDebug() ) System.out.println(template);
            
            bais.close();
            ReportDataSource ds = new ReportDataSource( getData() );
            JasperReport report = JasperCompileManager.compileReport(bais);
            return JasperFillManager.fillReport(report, new HashMap(), ds);
        }catch(Exception e){
            throw e;
        } finally{
            try{bais.close();}catch(Exception ign){}
        }
    }
    
    //overridable
    public boolean isDebug() { return false; }
    
}
