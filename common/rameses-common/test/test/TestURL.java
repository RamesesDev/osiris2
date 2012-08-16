/*
 * TestURL.java
 * JUnit based test
 *
 * Created on August 15, 2012, 3:12 PM
 */

package test;

import junit.framework.*;

/**
 *
 * @author Elmo
 */
public class TestURL extends TestCase {
    
    public TestURL(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        String pattern = "[^/]*(/|.)$";
        
        String path1 = "file1/file2/gypsy/";
        String path2 = "gypsy/";
        String path3 = "gypsy.pg";
        String path4 = "file2/gypsy.pg";
        System.out.println(path1.matches( pattern ));
        System.out.println(path2.matches( pattern ));
        System.out.println(path3.matches( pattern ));
        System.out.println(path4.matches( pattern ));
    }

}
