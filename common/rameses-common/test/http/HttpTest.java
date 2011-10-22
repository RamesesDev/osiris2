package http;
import com.rameses.http.HttpClient;
import com.rameses.util.SealedMessage;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;


/*
 * HttpTest.java
 * JUnit based test
 *
 * Created on September 19, 2011, 10:00 AM
 *
 * @author jzamss
 */
public class HttpTest extends TestCase {
    
    public HttpTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestSimpleGet() throws Exception {
        HttpClient c = new HttpClient("www.google.com");
        System.out.println( c.get() );
    }
    
    public void xtestGetWithParams() throws Exception {
        HttpClient c = new HttpClient("localhost:8080");
        c.setAppContext("test");
        Map map = new HashMap();
        map.put("name", "get info");
        System.out.println( c.get("testservice.jsp", map) );
    }
    
    public void xtestFailover() throws Exception {
        HttpClient c = new HttpClient("10.0.0.111:8080;10.0.0.113:8080;localhost1:8080");
        c.setConnectionTimeout(1000);
        c.setReadTimeout(1000);
        c.setAppContext("test");
        Map map = new HashMap();
        map.put("name", "post info");
        System.out.println( c.post("testservice.jsp", map) );
    }
    
    public void xtestMBean() throws Exception {
        HttpClient c = new HttpClient("localhost:8080;", true);
        c.setAppContext("gazeebu-session");
        System.out.println( c.post("ejb/ClusterService.getCurrentHostName") );
    }
    
    public void xtestService() throws Exception {
        HttpClient c = new HttpClient("localhost:8080;");
        c.setAppContext("gazeebu-session");
        Object[] args = new Object[] {"TestService", "test", new Object[]{"pat"}, new HashMap()};
        System.out.println( c.post("ejb/ScriptService/local.invoke", args) );
    }
    
     public void testScriptService() throws Exception {
        HttpClient c = new HttpClient("localhost:8080;");
        c.setAppContext("gazeebu-session");
        Map map = new HashMap();
        map.put("firstname", "elmox");
        map.put("lastname", "nazarenox");
        SealedMessage sm = new SealedMessage((Serializable)map);
        System.out.println( c.post("script/TestService.test", sm).getClass() );
    }
    
}
