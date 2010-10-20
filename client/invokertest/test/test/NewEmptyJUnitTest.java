/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on October 13, 2010, 4:39 PM
 */

package test;

import java.util.HashMap;
import java.util.Map;
import junit.framework.*;
import tester.HttpClient;

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
    public void testHello() throws Exception {
        Map env = new HashMap();
        env.put( "default.host", "localhost:8080" );
        env.put( "app.context", "mytest2" );
        HttpClient t = new HttpClient( env );
        HttpClient.ClientService c = (HttpClient.ClientService)t.create(  "MyTest" );
        System.out.println( c.invoke( "test", new Object[]{"elmo"} ) );
    }
    
}
