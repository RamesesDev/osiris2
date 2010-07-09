package com.rameses.osiris2.client;

/**
 *
 * @author elmo
 */
public final class ServiceUtil {
    
    public static Object lookup(String name, String key) throws Exception {
        return InvokerProxy.getInstance().create(name, key);
    } 
    
}
