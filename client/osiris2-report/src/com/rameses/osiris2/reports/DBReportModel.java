/*
 * ReportData.java
 *
 * Created on November 25, 2009, 2:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.reports;

import com.rameses.osiris2.client.OsirisContext;
import com.rameses.rcp.common.Action;
import com.rameses.sql.SimpleDataSource;
import com.rameses.sql.SqlUtil;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 *
 * @author elmo
 */
public abstract class DBReportModel {
    
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
            
//            for(JRParameter jrp : mainReport.getParameters()) {
//                if( !jrp.isSystemDefined() ) {
//                    //this is userdefined
//                }
//            }
            
            JRQuery jq = mainReport.getQuery();
            List paramList = new ArrayList();
            SqlUtil.parseStatement(jq.getText(), paramList);
            for(Object key : paramList) {
                if( conf.get(key)==null ) {
                    throw new Exception("Parameter " + key + " must be provided.");
                }
            }
            
            return JasperFillManager.fillReport(mainReport,conf,getConnection());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new IllegalStateException(ex);
        }
    }
    
    public Connection getConnection() throws Exception {
        Map appEnv = OsirisContext.getSession().getEnv();
        String url = "jdbc:rameses://" +appEnv.get("app.host")+ "/" +appEnv.get("app.context");
        return new SimpleDataSource("com.rameses.service.jdbc.DBServiceDriver", url, "", "").getConnection();
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
