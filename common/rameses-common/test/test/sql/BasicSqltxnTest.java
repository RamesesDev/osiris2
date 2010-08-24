/*
 * TestSqlQuery.java
 * JUnit based test
 *
 * Created on August 19, 2010, 6:42 PM
 */

package test.sql;

import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 * this is a test for basic 
 */
public class BasicSqltxnTest extends TestCase {
    
    private SqlContext ctx;
    
    public BasicSqltxnTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        SqlManager sqm = SqlManager.getInstance();
        ctx = sqm.createContext();
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testQueryParams()  throws Exception {
        String sql = "select * from table where id = $P{name} and $P{value}";
        SqlQuery q = ctx.createQuery(sql);
        q.setParameter("value", "value");
        q.setParameter("name", "name");
        
        assertEquals(q.getParameterValues().get(0)+"","name");
        assertEquals(q.getParameterValues().get(1)+"","value");
        
        Map map = new HashMap();
        map.put("name", "new name");
        q.setParameters( map );
        
        assertEquals(q.getParameterValues().get(0)+"","new name");
        assertEquals(q.getParameterValues().get(1),null);
        
    }
    
      // TODO add test methods here. The name must begin with 'test'. For example:
    public void testExecParams()  throws Exception {
        String sql = "select * from table where id = $P{name} and $P{value} and ${cond}";
        SqlExecutor q = ctx.createExecutor(sql);
        Map map = new HashMap();
        map.put("cond", " $P{c1} and $P{c2}" );
        q.setVars( map );
        
        assertEquals(q.getParameterNames().size(), 4);
        assertEquals(q.getParameterValues().size(), 4);
        
        q.setParameter("value", "value");
        q.setParameter("name", "name");
        q.setParameter("c2", "c2");
        q.setParameter("c1", "c1");
        
        assertEquals(q.getParameterValues().get(0)+"","name");
        assertEquals(q.getParameterValues().get(1)+"","value");
        assertEquals(q.getParameterValues().get(2)+"","c1");
        assertEquals(q.getParameterValues().get(3)+"","c2");
    }
    

     // TODO add test methods here. The name must begin with 'test'. For example:
    public void testExecParams2()  throws Exception {
        String sql = "select * from table where id = $P{name} and $P{value}";
        SqlExecutor q = ctx.createExecutor(sql);
        q.setParameter("value", "value");
        q.setParameter("name", "name");
        
        assertEquals(q.getParameterValues().get(0)+"","name");
        assertEquals(q.getParameterValues().get(1)+"","value");
        
        Map map = new HashMap();
        map.put("name", "new name");
        q.setParameters( map );
        
        assertEquals(q.getParameterValues().get(0)+"","new name");
        assertEquals(q.getParameterValues().get(1),null);
        
        
    }
    
    
    public void testExecParams3()  throws Exception {
        String sql = "select * from table where id = ?";
        SqlExecutor q = ctx.createExecutor(sql);
        q.setParameter(1, "value");
        assertEquals(q.getParameterValues().get(0)+"","value");
    }

}
