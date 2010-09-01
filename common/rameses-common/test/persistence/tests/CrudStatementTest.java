/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package persistence.tests;

import com.rameses.persistence.CreatePersistenceHandler;
import com.rameses.persistence.DeletePersistenceHandler;
import com.rameses.persistence.ReadPersistenceHandler;
import com.rameses.persistence.UpdatePersistenceHandler;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import java.util.Map;
import junit.framework.*;
import test.schema.*;

/**
 *
 * This test references test1.xml. Do not delete the file.
 */
public class CrudStatementTest extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    private SqlContext sqlCtx;
    
    public CrudStatementTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        SchemaConf conf = mgr.getConf();
        conf.setPropertyResolver( new BeanResolver() );
        SqlManager sqlm = SqlManager.getInstance();
        sqlCtx = sqlm.createContext();
        //sqlm.getConf().getExtensions().put(SchemaManager.class, mgr);
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void showFields( Map map ) {
        for(Object o: map.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            Object value = me.getValue();
            System.out.println(me.getKey() + "=" +me.getValue());
        }
        System.out.println("******************************");
    }
    
    private final String TEST_SCHEMA = "test1";
    
    private Map createMap(SchemaElement element) {
        Map map = mgr.createMap(element);
        map.put("primaryid_type", "SSS");
        map.put("primaryid_number", "SSS00001");
        map.put("secondaryid_type", "DL");
        map.put("secondaryid_number", "DRIVERS LICENSE");
        return map;
    }
    
    public void testCreate() throws Exception {
        System.out.println("CREATE---->");
        SchemaElement element = mgr.getElement(TEST_SCHEMA);
        Map map = createMap(element);
        
        CreatePersistenceHandler handler = new CreatePersistenceHandler(mgr,sqlCtx);
        SchemaScanner s = mgr.newScanner();
        s.scan( element.getSchema(),element, map, handler );
        for(Object o : handler.getQueue() ) {
            SqlExecutor se = (SqlExecutor)o; 
            System.out.println(se.getStatement());
            for(int i=0; i<se.getParameterNames().size();i++) {
                System.out.println(se.getParameterNames().get(i) + "=" + se.getParameterValues().get(i));
            }
        }
        
    }
    
    public void testRead() throws Exception {
        System.out.println("READ---->");
        SchemaElement element = mgr.getElement(TEST_SCHEMA);
        Map map = createMap(element);

        ReadPersistenceHandler handler = new ReadPersistenceHandler(mgr,sqlCtx);
        SchemaScanner s = mgr.newScanner();
        s.scan( element.getSchema(),element, map, handler );
        for(Object o : handler.getQueue() ) {
            SqlQuery se = (SqlQuery)o;
            System.out.println(se.getStatement());
            for(int i=0; i<se.getParameterNames().size();i++) {
                System.out.println(se.getParameterNames().get(i) + "=" + se.getParameterValues().get(i));
            }
        }
        System.out.println("remove fields");
        for(String str: handler.getRemoveFields()) {
            System.out.println(str);
        }
    }
    
    public void xtestUpdate() throws Exception {
        System.out.println("UPDATE---->");
        SchemaElement element = mgr.getElement(TEST_SCHEMA);
        Map map = createMap(element);
        
        UpdatePersistenceHandler handler = new UpdatePersistenceHandler(mgr,sqlCtx);
        SchemaScanner s = mgr.newScanner();
        s.scan( element.getSchema(),element, map, handler );
        for(Object o: handler.getQueue() ) {
            SqlExecutor se = (SqlExecutor)o; 
            System.out.println(se.getStatement());
            for(int i=0; i<se.getParameterNames().size();i++) {
                System.out.println(se.getParameterNames().get(i) + "=" + se.getParameterValues().get(i));
            }
        }
    }
    
    public void testDelete() throws Exception {
        System.out.println("DELETE---->");        
        SchemaElement element = mgr.getElement(TEST_SCHEMA);
        Map map = createMap(element);
        
        DeletePersistenceHandler handler = new DeletePersistenceHandler(mgr,sqlCtx);
        SchemaScanner s = mgr.newScanner();
        s.scan( element.getSchema(),element, map, handler );
        for(Object o: handler.getQueue() ) {
            SqlExecutor se = (SqlExecutor)o;
            System.out.println(se.getStatement());
            for(int i=0; i<se.getParameterNames().size();i++) {
                System.out.println(se.getParameterNames().get(i) + "=" + se.getParameterValues().get(i));
            }
        }
        
    }
    
}
