package com.rameses.reports.jasper;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRHtmlExporter;

@Stateless
@Local(JasperReportServiceLocal.class)
public class JasperReportService implements JasperReportServiceLocal {
    
    @EJB
    private JasperReportCacheMBean cache;
    
    public JasperReportService() {
    }

    public Map getReport(String name) {
        return cache.getReport(name);
    }

    public byte[] getReportOutput(String name, Object data, Map conf) {
        try {
            Map reportDef = getReport(name);
            JasperReport jr = (JasperReport)reportDef.get("main");
            ReportDataSource ds = new ReportDataSource(data);
            
            //check the type of report. The default is a jasper print
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            if( conf !=null) {
                JasperPrint jp = JasperFillManager.fillReport(jr,reportDef,ds);
                String outputType=(String)conf.get("OUTPUT");
                if(outputType.equalsIgnoreCase("pdf")) {
                    JasperExportManager.exportReportToPdfStream(jp, out);
                    return out.toByteArray();
                }
                else if( outputType.equalsIgnoreCase("html") ) {
                    JRHtmlExporter jhtml = new JRHtmlExporter();
                    jhtml.setParameter(JRExporterParameter.JASPER_PRINT, jp );
                    jhtml.setParameter(JRExporterParameter.OUTPUT_STREAM, out );
                    jhtml.exportReport();
                    return out.toByteArray();
                }
            }
            JasperFillManager.fillReportToStream( jr,out,reportDef, ds );
            return out.toByteArray();
        }
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void flush(String name) {
        cache.flush(name);
    }

    public void flushAll() {
        cache.flushAll();
    }
    
}
