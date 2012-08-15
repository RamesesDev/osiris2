/*
 * DefaultScriptService.java
 * Created on December 21, 2011, 9:51 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import com.rameses.service.ScriptServiceContext;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DBServiceImpl implements DBService {
    
    private Map conf;
    private String serviceName = "DBService";
    
   
    
    /** Creates a new instance of DefaultScriptService */
    public DBServiceImpl(Map c) {
        conf = c;
    }
    
    public DBServiceImpl(Map c, String serviceName) {
        conf = c;
        this.serviceName = serviceName;
    }
    
    public Map getResultSet(String statement, Object parameters) throws Exception {
        if( conf.get("readTimeout") == null ) {
            conf.put("readTimeout", "0");
        }
        if( conf.get("connectionTimeout") == null ) {
            conf.put("connectionTimeout", "0");
        }
        ScriptServiceContext ssc = new ScriptServiceContext(conf);
        DBService dbs = ssc.create(serviceName, DBService.class);
        return dbs.getResultSet(statement, parameters);
    }
    
}
