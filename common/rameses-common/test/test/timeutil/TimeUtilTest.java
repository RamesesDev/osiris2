/*
 * TimeUtilTest.java
 * JUnit based test
 *
 * Created on March 29, 2011, 8:47 AM
 */

package test.timeutil;

import com.rameses.util.TimeUtil;
import java.util.List;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TimeUtilTest extends TestCase {
    
    public TimeUtilTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        TimeUtil.ScheduleBean sked = new TimeUtil.ScheduleBean("08:30-12:30 M-F");
        assertTrue(sked.isMon());
        assertTrue(sked.isTue());
        assertTrue(sked.isWed());
        assertTrue(sked.isThu());
        assertTrue(sked.isFri());
        assertFalse(sked.isSat());
        assertFalse(sked.isSun());
    }

    public void testParseToDay() {
        List<Map> list = TimeUtil.parseToDayListMap(  "08:30-09:30 MWF" );
        assertEquals(list.size(),3);
        for(Map x: list) System.out.println("entry " + x);
    }
    
}
