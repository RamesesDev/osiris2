/*
 * CONSTANTS.java
 *
 * Created on July 19, 2010, 12:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

/**
 *
 * @author elmo
 */
public interface CONSTANTS {
    
    public final static String CACHE_SERVICE = "CacheService";
    public final static String RESOURCE_SERVICE = "ResourceService";
    public final static String SCRIPT_SERVICE_LOCAL = "ScriptService/local";
    public final static String SCRIPT_SERVICE = "ScriptService";
    
    public final static String RESPONSE_SERVICE = "ResponseService";
    public final static String RESPONSE_SERVICE_LOCAL = "ResponseService/local";
    
    
    public final static String SCRIPT_MGMT = "ScriptMgmt";
    public final static String PERSISTENCE_MGMT = "PersistenceMgmt";
    public final static String SCHEMA_MGMT = "SchemaMgmt";
    public final static String CONF_CACHE = "CONF";
    
    public final static String SYSTEM_NOTIFIER = "SystemNotifier";
    
    //these are keywords used by Async from the client. These are carried inside env.
    public final static String HEADER_MACHINE_KEY = "machinekey";
    public final static String HEADER_REPLY_TO = "replyto";
    
    public static final String SQLMGMT_CACHE = "sqlcache";
    
}
