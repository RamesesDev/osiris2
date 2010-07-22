/*
 * QueryExecutorTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 1:56 PM
 */

package test.sql;

import com.rameses.sql.QueryExecutorChain;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class QueryExecutorChainTest extends TestCase {
    
    public QueryExecutorChainTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void testMigrate() throws Exception {
        SqlManager sm = new TestSqlManager();
        SqlQuery sq = sm.createQuery( "select * from taxpayer order by name asc");
        SqlExecutor se = sm.createExecutor("insert into sample (id,name) values ($P{taxpayerno},$P{name})");
        QueryExecutorChain c = new QueryExecutorChain(sq, se);
        c.setBatchSize(10);
        c.execute();
        System.out.println("processed rows:" + c.getRowsProcessed());
    }
    

}
