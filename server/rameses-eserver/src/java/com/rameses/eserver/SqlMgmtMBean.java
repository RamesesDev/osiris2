/*
 * SqlMgmtMBean.java
 *
 * Created on July 24, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.sql.SqlContext;
import javax.sql.DataSource;

/**
 *
 * @author elmo
 */
public interface SqlMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    SqlContext createSqlContext(String dataSource);
    SqlContext createSqlContext(DataSource dataSource);
    SqlContext createSqlContext();
    void flushAll();
    
    
}
