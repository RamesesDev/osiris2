/*
 * SqlMgmtMBean.java
 *
 * Created on July 24, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.data.server;

import com.rameses.sql.SqlManager;
import javax.sql.DataSource;

/**
 *
 * @author elmo
 */
public interface SqlMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    SqlManager createSqlManager(String dataSource);
    SqlManager createSqlManager(DataSource dataSource);
    
    void flushAll();
    
}
