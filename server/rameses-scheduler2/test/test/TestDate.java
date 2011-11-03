/*
 * TestDate.java
 * JUnit based test
 *
 * Created on October 28, 2011, 10:05 AM
 */

package test;

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
    public void testHello() {
        Date d = java.sql.Timestamp.valueOf("2011-01-01 00:00:00");
        System.out.println(DateUtil.add(d, "1h"));
    }

}
