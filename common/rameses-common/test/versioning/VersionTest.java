/*
 * VersionTest.java
 * JUnit based test
 *
 * Created on September 29, 2011, 8:22 AM
 */

package versioning;

import com.rameses.cache.ChangeQueue;
import com.rameses.util.MapVersionControl;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class VersionTest extends TestCase {
    
    public VersionTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestDiff() throws Exception {
        Map map1 = new HashMap();
        map1.put("firstname", "elmo");
        map1.put("lastname", "nazareno");

        Map map2 = new HashMap();
        map2.put("firstname", "elmo");
        map2.put("lastname", "nazareno1");
        
        MapVersionControl mv = MapVersionControl.getInstance();
        Map diff = mv.diff(map1,map2);
        System.out.println(diff.toString());
    }
    
     // TODO add test methods here. The name must begin with 'test'. For example:
    public void testChangeQueue() throws Exception {
        ChangeQueue q = new ChangeQueue(new ChangeQueue.DefaultUpdateHandler());
        q.push( "firstname", "elmo" );
        q.push( "lastname", "nazareno" );
        q.push( "address", "cebu" );
        q.push( "age", 40 );
        q.applyChanges();
        q.applyChanges();
        q.applyChanges();
        System.out.println("sleeping");
        Thread.sleep(5000);
        q.push( "lastname", "nazareno" );
        q.push( "address", "cebu" );
        q.applyChanges();
    }

}
