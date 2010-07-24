package com.rameses.interfaces;

import java.sql.Connection;
import java.util.List;
import java.util.Map;


public interface DBServiceLocal extends DataServiceLocal {
    
    Object getSchema( String statement, Connection conn );
    Object getData(String statement, Object params, Connection conn);
    Object postData(String statement, Object params, Connection conn ); 
    void executeBatch(String statement, List params, Connection conn, int batchSize);
    Object execute(String name, Map params, Map connMap);
    void fetchData( String statement, Object params, Connection conn, IFetchHandler handler );
    
    Connection getConnection(String dataSource);
    Map getConnectionMap(List datasources);
    void closeConnectionMap(Map connMap);
    Object execChain(List dbCode, List datasources, boolean transactional) throws Exception;
    
}
