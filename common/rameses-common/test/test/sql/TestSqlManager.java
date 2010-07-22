/*
 * TestSqlManager.java
 *
 * Created on July 22, 2010, 10:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.sql;

import com.rameses.sql.SimpleDataSource;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;

/**
 *
 * @author elmo
 */
public class TestSqlManager extends SqlManager {
    
    public TestSqlManager() {
        dataSource = new SimpleDataSource("com.mysql.jdbc.Driver", 
                    "jdbc:mysql://localhost/taxpayerdb","root",null);
        
    }


    
}
