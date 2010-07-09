/*
 * DataServiceHandler.java
 *
 * Created on February 26, 2009, 10:31 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.interfaces;

import java.util.List;
import java.util.Map;

public interface DataServiceLocal {
    
    Object getSchema( String statement, String datasource );
    Object getData(String statement, Object params, String datasource);
    Object postData(String statement, Object params, String datasource ); 
    void executeBatch(String statement, List params, String datasource , int batchSize);

    Object execute(String name, Map params );
    
    void fetchData( String statement, Object params, String datasource, IFetchHandler handler );
    
}
