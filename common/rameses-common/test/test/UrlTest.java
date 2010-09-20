/*
 * UrlTest.java
 * JUnit based test
 *
 * Created on September 12, 2010, 7:27 PM
 */

package test;

import java.net.URL;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class UrlTest extends TestCase {
    
    public UrlTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception{
        URL u = Thread.currentThread().getContextClassLoader().getResource("META-INF/services");
        System.out.println(u.toExternalForm());
        System.out.println("protocol " + u.getProtocol());
        System.out.println("host :" + u.getHost());
        assertTrue( u.getHost().trim().length()==0 );
        System.out.println(u.getFile());
        String s = u.toExternalForm();
        s = s.substring(0, s.lastIndexOf("/"));
        URL u1 = new URL( s );
        System.out.println(u1);
    }

}
