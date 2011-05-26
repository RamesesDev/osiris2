import com.rameses.web.support.JsonUtil;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
/*
 * Test1.java
 * JUnit based test
 *
 * Created on May 16, 2011, 2:54 PM
 * @author jaycverg
 */

public class Test1 extends TestCase {
    
    public Test1(String testName) {
        super(testName);
    }
    
    public void testHello() {
        Map resp = new HashMap();
        resp.put("objid", "FILE-" + new java.rmi.server.UID());
        resp.put("filename", "one");
        
        String s = JsonUtil.toString( resp );
        System.out.println( s );
        
    }
    
}
