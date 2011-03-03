/*
 * JasperServerCache.java
 *
 * Created on August 4, 2009, 11:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.reports.jasper;

public class JasperReportCache implements JasperReportCacheMBean {
    

    public JasperReportCache() {
    }
    
    public void start() {
        System.out.println("STARTING JASPER SERVICE CACHE");
    }
    
    public void stop() {
        System.out.println("STOPPING JASPER SERVICE CACHE");
    }
    
    public void flushAll() {
        JasperReportManager.getInstance().flushAll();
        System.out.println("All reports flushed.");
    }
    
    public void flush(String name) {
        JasperReportManager.getInstance().flush(name);
        System.out.println("Report " + name + " flushed.");
    }

}
