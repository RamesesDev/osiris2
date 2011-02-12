import com.rameses.invoker.server.JsonUtil;
import junit.framework.*;
/*
 * JsonTest.java
 * JUnit based test
 *
 * Created on February 9, 2011, 4:28 PM
 */

/**
 *
 * @author ms
 */
public class JsonTest extends TestCase {
    
    public JsonTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        String a = "{name:'elmo', age:36}";
        System.out.println(  JsonUtil.toMap( a ) );  
    }

}
