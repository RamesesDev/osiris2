/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package test.schema;

import com.rameses.schema.Schema;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaField;
import com.rameses.schema.SchemaManager;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SchemaFindFieldTest extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    
    public SchemaFindFieldTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        SchemaConf conf = mgr.getConf();
        conf.setPropertyResolver( new BeanResolver() );
    }
    
    protected void tearDown() throws Exception {
    }
    
    
    public void testFindField() throws Exception {
        Schema schema = mgr.getSchema("sendout");
        assertNotNull(schema);
        SchemaField sf = schema.findField( "sendout/remote_objid" );
        assertEquals(sf.getName(), "objid");
        sf = schema.findField( "sendout/sender" );
        assertEquals(sf.getName(),"sender");
        sf = schema.findField( "sendout/remote/address2/city" );
        assertEquals(sf.getName(), "city");
        
    }
    
    
}
