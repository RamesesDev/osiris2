/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package test.schema;

import com.rameses.persistence.CreatePersistenceHandler;
import com.rameses.persistence.ReadPersistenceHandler;
import com.rameses.schema.Schema;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.schema.SchemaValidationHandler;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import com.rameses.sql.SqlUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SchemaTest extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    
    public SchemaTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        SchemaConf conf = mgr.getConf();
        conf.setPropertyResolver( new BeanResolver() );
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void xtestRoot() throws Exception {
        Schema schema = mgr.getSchema( "customer" );
        assertEquals( schema.getRootElement(), schema.getElement("customer") );
    }
    
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestScanFields() throws Exception {
        Schema schema = mgr.getSchema( "customer" );
        assertNotNull(schema);
        SchemaScanner sc = mgr.newScanner();
        Map data = new HashMap();
        data.put("age",45);
        data.put("option1","hello option 1");
        data.put("firstname","elmo");
        System.out.println("start scanning fields--------");
        sc.scan(schema, data, new TestSchemaHandler());
        System.out.println("end scanning fields--------");
    }
    
    public void xtestCreateObject() throws Exception {
        Map map = mgr.createMap(  "sendout" );
        for(Object o: map.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            System.out.println(me.getKey()+"="+me.getValue());
        }
    }

    public void testPersistence() throws Exception {
        Schema schema = mgr.getSchema( "sendout" );
        SqlManager sq = SqlManager.getInstance();
        sq.getConf().getExtensions().put( SchemaManager.class, mgr);

        assertNotNull(schema);
        CreatePersistenceHandler handler = new CreatePersistenceHandler(mgr,sq.createContext());
        SchemaScanner sc = mgr.newScanner();

        Map map = mgr.createMap( "sendout" );
        map.put("remote_address2_city", "capitol city 2");
        map.put("remote_address1_city", "bacguio city 1");
        
        sc.scan(schema, map, handler );
        
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
        
        /*
        Map data = new HashMap();
        data.put("sender", "nazareno, elmo");
        data.put("receiver", "flores, worgie");
        data.put("objid", "SND0001");
        data.put("option_option1", "option1");
        data.put("option_option2", "option2");
        
        sc.scan(data);
        Queue<ExecutionUnit> q = handler.getQueue();
        while(! q.isEmpty() ) {
            ExecutionUnit eu = q.remove();
            CrudModel c = eu.getCrudModel();
            SqlUnit su = CrudSqlBuilder.getInstance().getCreateSqlUnit(c);        
            System.out.println("*******************");
            System.out.println("statement: " + su.getStatement());
            for(Object s: su.getParamNames()) {
                System.out.println("param->"+s+ " "+eu.getData().get(s));
            }
        }
         */
    }
    
    
    public void testRead() throws Exception {
        Schema schema = mgr.getSchema( "sendout" );
        SqlManager sq = SqlManager.getInstance();
        sq.getConf().getExtensions().put( SchemaManager.class, mgr);

        assertNotNull(schema);
        ReadPersistenceHandler handler = new ReadPersistenceHandler(mgr,sq.createContext());
        SchemaScanner sc = mgr.newScanner();

        Map map = mgr.createMap( "sendout" );
        map.put("remote_address2_city", "capitol city 2");
        map.put("remote_address1_city", "bacguio city 1");
        sc.scan(schema, map, handler );
        
        SqlQuery se = null;
        Queue<SqlQuery> q = handler.getQueue();
        while(!q.isEmpty()) {
            se=q.remove();
            System.out.println(se.getStatement());
            int i = 0;
            for(String s: se.getParameterNames()) {
                System.out.println(s + "=" + se.getParameterValues().get(i));
                i++;
            }
        }
        
    }    
     
    public void xtestCrudStatement2() throws Exception {
        SqlManager sm = SqlManager.getInstance();
        sm.getConf().getExtensions().put( SchemaManager.class, mgr );
        SqlContext ctx = sm.createContext();
        SqlQuery qry = ctx.createNamedQuery( "sendout_create.schema");
        System.out.println("*******************");
        System.out.println("statement: " + qry.getStatement());
        for(Object s: qry.getParameterNames() ) {
            System.out.println("param->"+s+ " ");
        }
    }
    
    public void xtestValidate() throws Exception {
        SchemaValidationHandler handler = new SchemaValidationHandler();
        Schema schema = mgr.getSchema( "customer" );
        assertNotNull(schema);
        SchemaScanner sc = mgr.newScanner();
        Map map = new HashMap();
        map.put("age", "24");
        sc.scan(schema, map, handler);
        if( handler.getResult().hasErrors() )
            throw new Exception(handler.getResult().toString());
    }
    
    public void xtestInnerElement() throws Exception {
        SqlManager sq = SqlManager.getInstance();
        sq.getConf().getExtensions().put( SchemaManager.class, mgr);
        SqlUnit su = sq.getNamedSqlUnit( "branch_agent:branch_create.schema" );
        assertNotNull(su);
    }
    
    
}
