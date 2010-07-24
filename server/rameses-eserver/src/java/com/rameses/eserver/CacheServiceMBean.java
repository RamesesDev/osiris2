package com.rameses.eserver;

import java.util.Map;

public interface CacheServiceMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    
    Map getContext(String key);
    void removeContext(String namespace);
    void removeAll();
    
}
