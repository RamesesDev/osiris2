/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package persistence.tests;

import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.ValidationResult;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
import test.schema.*;

/**
 *
 * This test references test1.xml. Do not delete the file.
 */
public class DynaSchemaTest extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    private SqlContext sqlCtx;
    
    public DynaSchemaTest(String testName) {
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
    
    private final String TEST_SCHEMA = "dynaschema";
    
    private Map createMap(SchemaElement element) {
        Map map = mgr.createMap(element);
        map.put("address_type", "address");
        
        Map addr1 = new HashMap();
        addr1.put("street", "street1");
        map.put("address1", addr1);
        return map;
    }
    
    public void testCreate() throws Exception {
        System.out.println("CREATE---->");
        SchemaElement element = mgr.getElement(TEST_SCHEMA);
        Map map = createMap(element);
        showFields(map);
        
        ValidationResult vr = mgr.validate(element, map);
        if(vr.hasErrors()) throw new Exception(vr.toString());
        
    }
    

}
