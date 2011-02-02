import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on December 13, 2010, 4:17 PM
 */

/**
 *
 * @author rameses
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testHello() {
        try{
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date d = f.parse("2010-01-01 10:30:00");
            System.out.println(">> " + d.toString());
        }catch(Exception e) { ; }
    }
    
}
