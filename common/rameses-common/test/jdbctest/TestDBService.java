/*
 * TestDBService.java
 * JUnit based test
 *
 * Created on December 28, 2011, 3:46 PM
 */

package jdbctest;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestDBService extends TestCase {
    
    public TestDBService(String testName) {
        super(testName);
    }
    
    public void testDBMetaData() throws Exception {
        Class.forName( "com.rameses.service.jdbc.DBServiceDriver" );
        Connection c = DriverManager.getConnection("jdbc:rameses://localhost:8080/gazeebu-classroom");
        DatabaseMetaData md = c.getMetaData();
        ResultSet rs = md.getTableTypes();
        while(rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
    
}
