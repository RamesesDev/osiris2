import com.rameses.scripting.ScriptUtil;
import com.rameses.util.ExprUtil;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on October 17, 2010, 1:11 PM
 */

/**
 *
 * @author ms
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        Map map = new HashMap();
        map.put("name", "elmo");
        String msg = "hello sampler ${name} and #{name}";
        System.out.println(ExprUtil.substituteValues(msg, map));
    }

}
