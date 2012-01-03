/*
 * TestDBService.java
 * JUnit based test
 *
 * Created on December 28, 2011, 3:46 PM
 */

package jdbctest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestDBMetaDataService extends TestCase {
    
    public TestDBMetaDataService(String testName) {
        super(testName);
    }
    
    public void testDB() throws Exception {
        Class.forName( "com.rameses.service.jdbc.DBServiceDriver" );
        Connection c = DriverManager.getConnection("jdbc:rameses://localhost:8080/gazeebu-classroom");
        System.out.println(c);
        PreparedStatement ps = c.prepareStatement("select * from userprofile");
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String fname = rs.getString("firstname");
            String lname = rs.getString("lastname");
            System.out.println(fname + " " + lname);
        }
    }
    
}
