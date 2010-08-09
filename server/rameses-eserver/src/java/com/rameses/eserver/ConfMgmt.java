/*
 * ConfMgmt.java
 *
 * Created on August 5, 2010, 8:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Properties;
import javax.naming.InitialContext;

/**
 *
 * @author elmo
 */
public class ConfMgmt implements Serializable, ConfMgmtMBean {
    
    private String jndiName = "ConfMgmt";
    private ResourceServiceMBean resourceService;
    private CacheServiceMBean cacheService;
    
    public ConfMgmt() {
    }

    public void start() throws Exception {
        System.out.println("STARTING CONF MGMT");
        InitialContext ctx = new InitialContext();
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        cacheService = (CacheServiceMBean)ctx.lookup(CONSTANTS.CACHE_SERVICE);        
        JndiUtil.bind(ctx,jndiName,this);
        reload();
    }

    public void reload() throws Exception {
        Map cache = cacheService.getContext(CONSTANTS.CONF_CACHE);
        ConfResourceHandler h = new ConfResourceHandler();
        resourceService.scanResources( "conf://vars", h );
    }

    public void stop() throws Exception {
        System.out.println("STOPPING CONF MGMT");
        resourceService = null;
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx,jndiName);
    }

    public Object getProperty(Object key) {
        Map cache = cacheService.getContext(CONSTANTS.CONF_CACHE);
        return cache.get( key );
    }
    
    public Object getProperty(Object key, Object defaultValue) {
        Object o = getProperty( key );
        if( o == null ) return defaultValue;
        return o;
    }
    
    public Map getVars() {
        return cacheService.getContext(CONSTANTS.CONF_CACHE);
    }

    private class ConfResourceHandler implements MultiResourceHandler {
        private Properties props = new Properties();
        
        public void handle(InputStream is, String resName) throws Exception {
            Map cache = cacheService.getContext(CONSTANTS.CONF_CACHE);
            props.clear();
            props.load(is);
            cache.putAll( props );
        }
        
    }
    
}
