/*
 * TestSqlManagerFunctions.java
 * JUnit based test
 *
 * Created on August 13, 2010, 3:42 PM
 */

package test.sql;

import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class TestSqlManagerFunctions extends TestCase {
    
    private SqlManager factory;
    
    public TestSqlManagerFunctions(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        factory = SqlManager.getInstance();
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    private void printSqlQuery(SqlQuery qry) {
        System.out.println("*******************************");
        System.out.println(qry.getStatement());
    }
     
    private void printSqlExecutor(SqlExecutor ex) {
        System.out.println("*******************************");
        System.out.println(ex.getStatement());
    }

    public void testQuery() throws Exception {
        SqlContext sm = factory.createContext();
        printSqlQuery(sm.createQuery("select * from tblcustomer") );       
        printSqlQuery(sm.createQuery("select * from tblcustomer") );   
        printSqlQuery(sm.createNamedQuery("customerlist.sql") );
        printSqlQuery(sm.createNamedQuery("branch_user_read.crud") );
        printSqlQuery(sm.createNamedQuery("branch_user_list.crud") );
        
    }

    public void testExecutor() throws Exception {
        SqlContext sm = factory.createContext();
        printSqlExecutor(sm.createExecutor("update table 1 set data = ?") );
        printSqlExecutor(sm.createExecutor("update table 1 set data = ?") );
        printSqlExecutor(sm.createNamedExecutor("branch_user_create.crud") );
        printSqlExecutor(sm.createNamedExecutor("branch_user_update.crud") );
        printSqlExecutor(sm.createNamedExecutor("branch_user_delete.crud") );
        printSqlExecutor(sm.createNamedExecutor("customer_insert.sqlx") );
    }
    
    
    
}
