/*
 * JasperServerCacheMBean.java
 *
 * Created on August 4, 2009, 11:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.reports.jasper;

import java.io.Serializable;

public interface JasperReportCacheMBean extends Serializable {
    
    void start();
    void stop();
    void flushAll();
    void flush(String name);
    
    
}
