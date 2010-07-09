/*
 * ReportData.java
 *
 * Created on November 25, 2009, 2:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.reports;

import com.rameses.rcp.common.Action;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author elmo
 */
public abstract class ReportModel {
    
    public ReportModel() {
    }
    
    public abstract Object getReportData();
    public abstract String getReportName();
    
    public SubReport[] getSubReports() {
        return null;
    }
    
    public Map getParameters() {
        return null;
    }
    
    private JasperPrint reportOutput;
    
    private JasperReport mainReport;
    
    private JasperPrint createReport() {
        try {
            if(mainReport==null) {
                mainReport = ReportUtil.getJasperReport(getReportName());
            }
            
            Map conf = new HashMap();
            SubReport[] subReports = getSubReports();
            
            if( subReports !=null) {
                for(SubReport sr: subReports) {
                    conf.put( sr.getName(), sr.getReport() );
                }
            }
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
            return JasperFillManager.fillReport(mainReport,conf,ds);
        } catch (JRException ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }
    
    public String viewReport() {
        reportOutput = createReport();
        return "report";
    }
    
    public JasperPrint getReport() {
        return reportOutput;
    }
    
    
    public List getReportActions() {
        List list = new ArrayList();
        list.add( new Action("_close", "Close", null));
        return list;
    }
    
    
}
