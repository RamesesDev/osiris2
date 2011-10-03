/*
 * ServiceProxyTest.java
 * JUnit based test
 *
 * Created on September 21, 2011, 9:21 AM
 */

package http;

import com.rameses.service.EJBServiceContext;
import com.rameses.service.ServiceProxy;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class SessionTest extends TestCase {
    
    Map map = new HashMap();
    
    protected void setUp() throws Exception {
        map.put("app.context", "gazeebu-session");
        map.put("app.host", "localhost:8080");
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestRegister() throws Exception {
        EJBServiceContext ctx = new EJBServiceContext(map);
        ServiceProxy p = ctx.create( "SessionServer");
        Object[] args = new Object[]{
            "emn", "hello info"
        };
        System.out.println("session is ->"+p.invoke( "register", args ));
    }
    
    public void testPoll() throws Exception {
        //WXPPNU:-77ea1dbd:13290797ab9:-7ffe
        EJBServiceContext ctx = new EJBServiceContext(map);
        ServiceProxy p = ctx.create( "SessionServer");
        Object[] args = new Object[]{
            "WXPPNU:-77ea1dbd:13290797ab9:-7ffd", "TOKEN--1871119683"
        };
        System.out.println("result->"+p.invoke("poll", args));
    }
    

}
