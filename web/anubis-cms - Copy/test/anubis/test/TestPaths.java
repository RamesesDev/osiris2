/*
 * TestPaths.java
 * JUnit based test
 *
 * Created on July 31, 2012, 3:47 PM
 */

package anubis.test;

import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class TestPaths extends TestCase {
    
    public TestPaths(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        String path = "/about/history/mission";
        if(path.startsWith("/")) path = path.substring(1);
        while(true) {
            String template = path.replace("/", "_");
            System.out.println("process path->"+template);
            if( path.lastIndexOf("/") <=0 ) break;
            path = path.substring(0, path.lastIndexOf("/"));
        }
    }
    
}
