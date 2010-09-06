/*
 * TestSchemaCrudProvider.java
 * JUnit based test
 *
 * Created on August 13, 2010, 10:04 AM
 */

package persistence.tests;

import com.rameses.schema.SchemaManager;
import com.rameses.sql.SqlContext;
import java.math.BigDecimal;
import junit.framework.*;

/**
 *
 * This test references test1.xml. Do not delete the file.
 */
public class AggTest extends TestCase {
    
    private SchemaManager mgr = SchemaManager.getInstance();
    private SqlContext sqlCtx;
    
    public AggTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        //SchemaConf conf = mgr.getConf();
        //conf.setPropertyResolver( new BeanResolver() );
        //SqlManager sqlm = SqlManager.getInstance();
        //sqlCtx = sqlm.createContext();
        //sqlm.getConf().getExtensions().put(SchemaManager.class, mgr);
    }
    
    protected void tearDown() throws Exception {
    }
    
    private final String TEST_SCHEMA = "test1";
    
    public void testNumber() {
        Integer i = new Integer(1);
        Double d = new Double(25.24);
        
        BigDecimal bd1 = new BigDecimal(i+"");
        assertEquals( bd1.intValue(), 1 );

        BigDecimal bd2 = new BigDecimal(d+"");
        assertEquals( bd2.doubleValue(), 25.24 );
        
        BigDecimal nd = bd1.add( bd2 );
        assertEquals( nd.doubleValue(), 26.24 );
        
        BigDecimal nd1 = new BigDecimal(nd+"");
        assertEquals( nd1, new BigDecimal("26.24") );
        
        assertEquals( nd.compareTo(nd1), 0 );
        assertTrue( bd1.compareTo(bd2) < 0 );
        assertTrue( bd2.compareTo(bd1) > 0 );
        
    }
    
    
}
