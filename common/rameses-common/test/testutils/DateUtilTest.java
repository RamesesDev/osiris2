/*
 * MapSerializer.java
 * JUnit based test
 *
 * Created on August 28, 2010, 7:55 AM
 */

package testutils;

import com.rameses.util.DateUtil;
import java.sql.Timestamp;
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
    
    
}
