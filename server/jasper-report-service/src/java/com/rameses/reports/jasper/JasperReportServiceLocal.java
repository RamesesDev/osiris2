package com.rameses.reports.jasper;

import java.util.Map;

public interface JasperReportServiceLocal {

    Map getReport(String name);
    byte[] getReportOutput(String name, Object data, Map conf);
    void flush(String name);
    void flushAll();
}
