import com.rameses.service.EJBServiceContext;
import com.rameses.service.ScriptServiceContext;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/*
 * ServiceTest.java
 * JUnit based test
 *
 * Created on June 26, 2012, 7:39 AM
 * @author Elmo
 */
public class ServiceTest extends TestCase {
    
    public ServiceTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        Map map = new HashMap();
        map.put("app.context", "gazeebu-classroom");
        map.put("app.host", "10.0.0.118:8080");
        ScriptServiceContext ctx = new ScriptServiceContext(map);
        EchoService echo = ctx.create( "EchoService", EchoService.class );
        System.out.println(echo.test("hello"));
        
        
        EJBServiceContext ejb = new EJBServiceContext(map);
        ScriptService svc = ejb.create( "ScriptService/local", ScriptService.class );
        byte[] bytes = svc.getScriptInfo( "DateService" );
        System.out.println(new String(bytes));
    }
    
    public interface EchoService {
        Object test(Object params);
    }
    
    
    public interface ScriptService {
        public byte[] getScriptInfo(String name);
    }
}
