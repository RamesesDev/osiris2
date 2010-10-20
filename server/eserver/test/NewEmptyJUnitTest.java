
import com.rameses.util.TemplateProvider;
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
        map.put("name", "${elmo}");
        Object o = TemplateProvider.getInstance().getResult("META-INF/template.groovy", map );
        System.out.println(o);
    }
    
}
