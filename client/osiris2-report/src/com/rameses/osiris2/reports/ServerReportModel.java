/*
 * ServerReportModel.java
 *
 * Created on February 28, 2011, 1:01 PM
 * @author jaycverg
 */

package com.rameses.osiris2.reports;

import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public abstract class ServerReportModel extends ReportModel {
    
    private JasperPrint jasperPrint;
    private JasperReport report;
    private Map reportConf;
    
    public JasperPrint getReport() {
        return jasperPrint;
    }
    
    public String viewReport() {
        jasperPrint = createReport();
        return "report";
    }
    
    private JasperPrint createReport() {
        try {
            if(report==null) {
                reportConf = ServerReportUtil.getReportConf(getReportName());
                report = (JasperReport) reportConf.remove("main");
            }
            
            Map conf = new HashMap(reportConf);            
            Map params = getParameters();
            if(params!=null) {
                conf.putAll(params);
            }
            
            JRDataSource ds = null;
            Object data = getReportData();
            if( data!=null ) {
                ds = new ReportDataSource(data);
            } else {
                ds = new JREmptyDataSource();
            }
            return JasperFillManager.fillReport(report,conf,ds);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
    
}
