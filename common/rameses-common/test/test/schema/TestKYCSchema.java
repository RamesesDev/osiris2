/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package test.schema;

import com.rameses.schema.Schema;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaManager;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class TestKYCSchema extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    
    public TestKYCSchema(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        SchemaConf conf = mgr.getConf();
        conf.setPropertyResolver( new BeanResolver() );
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void showFields( Map map ) {
        for(Object o: map.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            Object value = me.getValue();
            System.out.println(me.getKey() + "=" +me.getValue());
        }
    }
    
    public void testKYCdata() throws Exception {
        Schema schema = mgr.getSchema("kyc");
        Map map = mgr.createMap(schema,schema.getRootElement());
        showFields(map);
    }
    
    
}
