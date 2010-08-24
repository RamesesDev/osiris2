/*
 * SqlTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 9:59 AM
 */

package test.sql;

import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlQuery;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class TestSqlUnitStatements extends TestCase {
    
    private SqlManager factory = SqlManager.getInstance();
    
    
    public TestSqlUnitStatements(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    private void printQuery(String name) {
        SqlContext cp = factory.createContext();
        SqlQuery qry = cp.createNamedQuery( name );
        System.out.println("*******************************");
        System.out.println("PRINTING ... " +name);
        System.out.println(qry.getStatement());
        System.out.println("Parameters");
        for(String s: qry.getParameterNames()) {
            System.out.println("->"+s);
        }
    }
    
    private void printExecutor(String name) {
        SqlContext cp = factory.createContext();
        SqlExecutor ex = cp.createNamedExecutor( name );
        System.out.println("*******************************");
        System.out.println("PRINTING ... " +name);
        System.out.println(ex.getStatement());
        System.out.println("Parameters");
        for(String s: ex.getParameterNames()) {
            System.out.println("->"+s);
        }
    }
    
    public void testSimpleSql() throws Exception {
        printQuery( "customerlist.sql" );
    }
    
    public void testExtSql() throws Exception {
        printExecutor("customer_insert.sqlx");
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testCreateCrud() throws Exception {
        printExecutor("branch_user_create.crud");
    }
    
    public void testReadCrud() throws Exception {
        printQuery("branch_user_read.crud" );
    }
    
    public void testUpdateCrud() throws Exception {
        printExecutor("branch_user_update.crud" );
    }
    
    public void testDeleteCrud() throws Exception {
        printExecutor("branch_user_delete.crud");
    }
    
    public void testListCrud() throws Exception {
        printQuery("branch_user_list.crud");
    }
    
    
}
