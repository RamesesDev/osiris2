/*
 * SubReport.java
 *
 * Created on November 25, 2009, 2:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.reports;

import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author elmo
 */
public class SubReport {
    
    private String name;
    private String reportname;
    
    public SubReport(String name, String reportname) {
        this.name = name;
        this.reportname = reportname;
    }

    public SubReport() {
        
    }
    
    
    public String getName() {
        return name;
    }
    
    public String getReportname() {
        return reportname;
    }
    
    private JasperReport report;
    
    public JasperReport getReport() {
        if( report == null) {
            report = ReportUtil.getJasperReport(reportname);
        }
        return report;
    }


}
