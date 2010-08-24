/*
 * QueryExecutorTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 1:56 PM
 */

package test.sql;

import com.rameses.sql.QueryExecutor;
import com.rameses.sql.SimpleDataSource;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import javax.sql.DataSource;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class QueryExecutorChainTest extends TestCase {
    
    private DataSource ds;
    private SqlManager factory = SqlManager.getInstance();
    
    public QueryExecutorChainTest(String testName) {
        super(testName);
    }
    
    
    
    
    protected void setUp() throws Exception {
        ds = new SimpleDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/taxpayer", "root", null);
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testMigrate() throws Exception {
        SqlContext sm = factory.createContext( ds  );
        SqlQuery sq = sm.createQuery( "select * from taxpayer order by name asc");
        SqlExecutor se = sm.createExecutor("insert into sample (id,name) values ($P{taxpayerno},$P{name})");
        QueryExecutor c = new QueryExecutor(sq, se);
        c.setBatchSize(10);
        c.execute();
        System.out.println("processed rows:" + c.getRowsProcessed());
    }
    
    
}
