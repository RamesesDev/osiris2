package com.rameses.eserver;

import com.rameses.persistence.EntityManager;
import com.rameses.sql.SqlContext;
import javax.sql.DataSource;

/**
 *
 * @author elmo
 */
public interface PersistenceMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    SqlContext createSqlContext(String dataSource);
    SqlContext createSqlContext(DataSource dataSource);
    SqlContext createSqlContext();
    void flushAll();
    
    
    EntityManager createPersistenceContext(String datasource);
    
}
