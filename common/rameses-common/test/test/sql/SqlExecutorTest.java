/*
 * SqlExecutorTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 11:49 AM
 */

package test.sql;

import com.rameses.sql.SqlExecutor;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SqlExecutorTest extends TestCase {
    
    public SqlExecutorTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSimpleInsert() throws Exception {
        TestSqlManager sm = new TestSqlManager();
        String sql = "insert into sample (id,name) values ($P{id}, $P{name})";
        SqlExecutor se = sm.createExecutor(sql);
        se.setParameter("id", "id0001");
        se.setParameter("name","elmo");
        se.addBatch();
        
        se.setParameter("id", "id0002");
        se.setParameter("name","worgie");
        se.addBatch();
        
        se.setParameter("id", "id0003");
        se.setParameter("name","jessie");
        se.addBatch();
        
        se.execute();
    }
    
    public void testSimpleInsert2() throws Exception {
        TestSqlManager sm = new TestSqlManager();
        String sql = "insert into sample (id,name) values (?,?)";
        SqlExecutor se = sm.createExecutor(sql);
        se.setParameter(1, "id0004");
        se.setParameter(2,"elmo2");
        se.addBatch();
        
        se.setParameter(1, "id0005");
        se.setParameter(2,"worgie2");
        se.addBatch();
        
        se.setParameter(1, "id0006");
        se.setParameter(2,"jessie2");
        se.addBatch();
        
        se.execute();
    }
    
}
