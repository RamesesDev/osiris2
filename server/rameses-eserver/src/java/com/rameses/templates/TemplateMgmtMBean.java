package com.rameses.templates;

public interface TemplateMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    void flushAll();
    void flush(String name);
    Template getTemplate(String name) throws Exception;
    
}
