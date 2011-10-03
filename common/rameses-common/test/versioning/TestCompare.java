/*
 * TestCompare.java
 * JUnit based test
 *
 * Created on September 30, 2011, 8:40 AM
 */

package versioning;

import com.rameses.util.MapVersionControl;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestCompare extends TestCase {
    
    public TestCompare(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        Map map1 = new HashMap();
        map1.put( "firstname", "elmo" );
        map1.put( "lastname", "nazareno" );
        map1.put( "address", "cebu" );
        map1.put( "age", 40 );
        
        Map map2 = new HashMap();
        map2.put( "firstname", "elmo" );
        map2.put( "lastname", "nazarenox" );
        map2.put( "address", "cebu" );
        map2.put( "age", 42 );
        
        System.out.println("before applying " + map1.toString());
        MapVersionControl mv = MapVersionControl.getInstance();
        Map diff = mv.diff(map1, map2);
        System.out.println("diff is " + diff.toString());
        Map test1 = mv.merge(map1, diff);
        
        System.out.println("new map1->" + test1.toString());
        System.out.println("check the values must be the same");
        Map test2 = mv.compareAndMerge(map1,map2);
        System.out.println("test2 ->"+test2.toString());
    }

}
