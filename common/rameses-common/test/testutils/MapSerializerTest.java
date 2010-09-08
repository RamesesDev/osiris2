/*
 * ObjectSerializer.java
 * JUnit based test
 *
 * Created on August 28, 2010, 7:55 AM
 */

package testutils;

import com.rameses.util.ObjectSerializer;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class MapSerializerTest extends TestCase {
    
    public MapSerializerTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public Map createMapData() {
        Map map = new HashMap();
        map.put("string", "Simple string");
        map.put("esc-string", "\"Hello\" said the boy's father. How much is $100.00");
        map.put("decimal", new BigDecimal(100.0));
        map.put("integer", new Integer(100));
        map.put("small-int", 200);
        map.put("small-dbl", 200.0);
        map.put("double", new Double(200.0*3));
        map.put("long", new Long(20000));
        map.put("small-long", 100028);
        map.put("date", new Date());
        map.put("sql-date", java.sql.Date.valueOf("2004-01-01"));
        map.put("sql-timestamp", java.sql.Timestamp.valueOf("2004-01-01 03:10:22"));
        
        Map map2 = new HashMap();
        map2.put("_string", "Simple string");
        map2.put("esc-string", "\"Hello\" said the boy's father. How much is $100.00");
        map2.put("decimal", new BigDecimal(100.0));
        map2.put("_integer", new Integer(100));
        map2.put("small-int", 200);
        map2.put("small-dbl", 200.0);
        map2.put("double", new Double(200.0*3));
        map2.put("_long", new Long(20000));
        map2.put("_small-long", 100028);
        map2.put("date", new Date());
        map2.put("sql-date", java.sql.Date.valueOf("2004-01-01"));
        map2.put("sql-timestamp", java.sql.Timestamp.valueOf("2004-01-01 03:10:22"));
        //map2.put("child-map", map);
        map2.put("_lastname", "ziki");
        map2.put("_state", "hellower");
        
        return map2;
        
    }
    
    public void testScanner() {
        ObjectSerializer mp = new ObjectSerializer();
        StringWriter w = new StringWriter();
        mp.write( createMapData(), w );
        System.out.println(w.toString()); 
    }
    
    
}
