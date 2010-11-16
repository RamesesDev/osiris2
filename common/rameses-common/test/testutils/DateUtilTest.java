/*
 * MapSerializer.java
 * JUnit based test
 *
 * Created on August 28, 2010, 7:55 AM
 */

package testutils;

import com.rameses.util.DateUtil;
import com.rameses.util.TimeUtil;
import java.sql.Timestamp;
import java.util.Date;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class DateUtilTest extends TestCase {
    
    public DateUtilTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
   
    public void testUtil() {
        Timestamp tp = Timestamp.valueOf("2000-01-01 00:01:01");
        System.out.println(DateUtil.getFormattedTime(tp, "US"));
    }
    
    public void testCheckTime1() {
        Date d = new Date();
        String pattern = "08:00-12:00 MWF";
        TimeUtil.ScheduleBean t = new TimeUtil.ScheduleBean(pattern);
        assertTrue( t.isMon() );
        assertTrue( t.isWed() );
        assertTrue( t.isFri() );
        assertFalse( t.isTue() );
        assertFalse( t.isThu() );
    }

    public void testCheckTime2() {
        Timestamp d = java.sql.Timestamp.valueOf( "2010-11-15 18:30:00");
        String pattern = "17:00-19:00 MWF";
        assertTrue( TimeUtil.checkSchedule( d, pattern ));
    }
    
    
    
}
