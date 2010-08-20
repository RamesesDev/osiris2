/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package test.schema;

import com.rameses.persistence.CreatePersistenceHandler;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.util.Queue;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SchemaSendoutTest extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    
    public SchemaSendoutTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        SchemaConf conf = mgr.getConf();
        conf.setPropertyResolver( new BeanResolver() );
    }
    
    protected void tearDown() throws Exception {
    }
    
    
    public void testMap() throws Exception {
        //Map map = mgr.createMap( "sendout1");
        
        //for(Object o: map.entrySet()) {
        //    Map.Entry me = (Map.Entry)o;
        //    System.out.println(me.getKey()+"="+me.getValue());
        //}
         
        /*
        SqlManager sq = SqlManager.getInstance();
        Schema schema = sq.
        PersistenceHandler handler = new PersistenceHandler(mgr,sq.createContext());
        SchemaScanner scanner = mgr.newScanner();
        scanner.scan(  )
       
        
        sc.scan(map);
        
        SqlExecutor se = null;
        Queue<SqlExecutor> q = handler.getQueue();
        while(!q.isEmpty()) {
            se=q.remove();
            System.out.println(se.getStatement());
            int i = 0;
            for(String s: se.getParameterNames()) {
                System.out.println(s + "=" + se.getParameterValues().get(i));
                i++;
            }
        }
        
        */
    }
    
    
}
