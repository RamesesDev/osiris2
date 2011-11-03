/*
 * TestDate.java
 * JUnit based test
 *
 * Created on October 25, 2011, 2:03 PM
 */

package joda;

import java.util.Date;
import junit.framework.*;
import org.joda.time.DateTime;

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
        Date d1 = java.sql.Date.valueOf("2010-07-01");
        Date d2 = java.sql.Date.valueOf("2010-08-01");
        DateTime dt1 = new DateTime(d1);
        DateTime dt2 = new DateTime(d2);
        
    }

}
