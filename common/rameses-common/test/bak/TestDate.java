/*
 * TestDate.java
 * JUnit based test
 *
 * Created on October 25, 2011, 11:30 AM
 */

package bak;

import com.rameses.util.DateUtil;
import java.util.Date;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestDate extends TestCase {
    
    public TestDate(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        Date d1 = java.sql.Date.valueOf("2011-01-01");
        Date d2 = java.sql.Date.valueOf("2011-01-30");
        System.out.println(DateUtil.diff(d1,d2));
    }

}
