/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package persistence.tests;

import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlUnit;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SqlUnitTest extends TestCase {
    
    private SqlManager sql = SqlManager.getInstance();
    
    public SqlUnitTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testSqlx() throws Exception {
        
        String a = "[getCustomer]";
        assertTrue(a.matches("\\[.*getCustomer.*\\]"));

        //SqlUnit su = sql.getNamedSqlUnit( "customer:deleteCustomer.sql" );
        //System.out.println(su.getStatement());

        SqlUnit su = sql.getNamedSqlUnit( "branchinfo:verifyBranchUserTerminal.sql" );
        System.out.println(su.getStatement());

         su = sql.getNamedSqlUnit( "branchinfo:getInfo.sql" );
        System.out.println(su.getStatement());
        
        System.out.println("cat".hashCode());
    }    
     

    
    
}
