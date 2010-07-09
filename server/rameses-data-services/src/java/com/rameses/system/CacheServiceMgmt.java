package com.rameses.system;

import com.rameses.interfaces.CacheProvider;
import org.jboss.annotation.ejb.Management;

@Management
public interface CacheServiceMgmt {
    
    void start();
    void stop();
    void flush(String namespace);
    void flushAll();
    void addProvider(CacheProvider p);
    
}
