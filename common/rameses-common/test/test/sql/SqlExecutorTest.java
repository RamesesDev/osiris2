/*
 * SqlExecutorTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 11:49 AM
 */

package test.sql;

import com.rameses.sql.SimpleDataSource;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import javax.sql.DataSource;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SqlExecutorTest extends TestCase {
    
    
    private DataSource ds;
    private SqlManager factory = SqlManager.getInstance();
    
    protected void setUp() throws Exception {
        ds = new SimpleDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/taxpayer", "root", null);
    }
    
    public SqlExecutorTest(String testName) {
        super(testName);
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSimpleInsert() throws Exception {
        SqlContext sm = factory.createContext( ds  );
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
        SqlContext sm = factory.createContext( ds  );
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
