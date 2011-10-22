/*
 * MapSerializer.java
 * JUnit based test
 *
 * Created on August 28, 2010, 7:55 AM
 */

package testutils;

import com.rameses.service.ScriptServiceContext;
import java.util.HashMap;
import java.util.Map;
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
   
    /*
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
    */
    
    public void testFolder() throws Exception {
        //System.getProperties().list(System.out);
        //File f = new File("D:/");
        //f.mkdirs();
        Map map = new HashMap();
        map.put("app.host", "10.0.0.1:8080;localhost:8080");
        map.put("app.context", "gazeebu-classroom");
        ScriptServiceContext sc = new ScriptServiceContext(map);
        MyEchoService p = sc.create( "EchoService", MyEchoService.class );
        Map c = new HashMap();
        c.put("name","jay");
        System.out.println("result is->"+p.test(c));
    }
    
    private interface MyEchoService {
        String test(Map m);
    }
    
    
}
