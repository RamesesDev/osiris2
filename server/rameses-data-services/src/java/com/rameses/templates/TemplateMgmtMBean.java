package com.rameses.templates;

public interface TemplateMgmtMBean {
    
    void start();
    void stop();
    void flushAll();
    void flush(String name);
    Object getTemplate(String name);
    
}
