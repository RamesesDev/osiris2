/*
 * TestCluster.java
 * JUnit based test
 *
 * Created on September 23, 2011, 8:37 AM
 */

package test;

import com.rameses.service.DefaultEJBServiceProxy;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestCluster extends TestCase {
    
    private Map conf;
    
    public TestCluster(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        conf  = new HashMap();
        conf.put("app.context","gazeebu-session");
        conf.put("app.host","localhost:8080");        
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        DefaultEJBServiceProxy ctx = new DefaultEJBServiceProxy("ClusterService",conf);
        System.out.println("this host name " + ctx.invoke("getCurrentHostName"));
        System.out.println(ctx.invoke("getRemoteHosts"));
    }

}
