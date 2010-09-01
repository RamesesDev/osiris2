/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package persistence.tests;

import com.rameses.persistence.UpdatePersistenceHandler;
import com.rameses.schema.Schema;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.util.Map;
import java.util.Queue;
import junit.framework.*;
import test.schema.*;

/**
 *
 * @author elmo
 */
public class EntityTest extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    
    public EntityTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        SchemaConf conf = mgr.getConf();
        conf.setPropertyResolver( new BeanResolver() );
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testCreate() throws Exception {
        
        SqlManager sq = SqlManager.getInstance();
        sq.getConf().getExtensions().put( SchemaManager.class, mgr);

        Schema schema = mgr.getSchema( "test1" );
        assertNotNull(schema);
        
        UpdatePersistenceHandler handler = new UpdatePersistenceHandler(mgr,sq.createContext());
        SchemaScanner sc = mgr.newScanner();
        Map m = mgr.createMap(schema, null);
        
        //this is null if 
        
        //assertEquals( m.get("id")+"", "TESTID" );
        m.put("id", "p1");
        m.put("name1", "name1");
        m.put("test2_name2", "name2");
        m.put("test3_name3", "name3");
        
        sc.scan(  schema, m ,handler);
        
        
        SqlExecutor se = null;
        Queue q= handler.getQueue();
        assertEquals(q.size(),3);
        while(!q.isEmpty()) {
            se=(SqlExecutor )q.remove();
            System.out.println(se.getStatement());
            int i = 0;
            for(String s: se.getParameterNames()) {
                System.out.println(s + "=" + se.getParameterValues().get(i));
                i++;
            }
        }
        
    }    
     

    
    
}
