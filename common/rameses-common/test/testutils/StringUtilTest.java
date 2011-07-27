/*
 * StringUtilTest.java
 * JUnit based test
 *
 * Created on July 19, 2011, 10:09 AM
 */

package testutils;

import com.rameses.util.StringUtil;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class StringUtilTest extends TestCase {
    
    public StringUtilTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testOne() {
        String d = "dog";
        System.out.println(StringUtil.padLeft(d,'>',10));
    }

    public void testTwo() {
        String d = "dog";
        System.out.println(StringUtil.padRight(d,'>',10));
    }
    
}
