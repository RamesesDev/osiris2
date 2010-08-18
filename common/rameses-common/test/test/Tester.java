/*
 * Tester.java
 * JUnit based test
 *
 * Created on June 2, 2010, 2:26 PM
 */

package test;

import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class Tester extends TestCase {
    
    public Tester(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSubstitute() throws Exception {
        //String tmp = "/a/b/c";
        //System.out.println( tmp.substring( tmp.indexOf("/", 1)+1) );
        
        Map map = new HashMap();
        map.put("firstname","elmo");
        map.put("lastname","nazareno");
        
        Map map2 = new HashMap();
        map2.put("firstname","elmox");
        map2.put("address","cebu city");
        
        map2.putAll(map);
        for(Object o: map2.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            System.out.println(me.getKey()+"="+me.getValue());
        }
        assertEquals(map.get("firstname"), map2.get("firstname"));
        
        /*
        String c = "where cond = $P{cond}";
        Map map = new HashMap();
        map.put("condition", c);
        String sql = "select from o ${condition}";
        System.out.println( SqlUtil.substituteValues( sql, map ));
         */
    }

    
}
