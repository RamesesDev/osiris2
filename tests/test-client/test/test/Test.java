package test;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junit.framework.*;

/*
 * Test.java
 * JUnit based test
 *
 * Created on June 22, 2010, 4:17 PM
 */

/**
 *
 * @author compaq
 */
public class Test extends TestCase {
    
    public Test(String testName) {
        super(testName);
    }
    
    public void test() throws ParseException {
        Date d = new Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("(EEEE) MMM dd, yyyy");
        //System.out.println( sdf.format(d) );
        
        String reg = "(\\d{4}\\D+\\d{2}\\D+\\d{2})|(\\d{2}\\D+\\d{2}\\D+\\d{4})";
        Pattern p = Pattern.compile(reg);
        
        Matcher m = p.matcher("01 01 2010");
        if ( m.matches() ) {
            System.out.println("1 - " + m.group(1));
            System.out.println("2 - " + m.group(2));
        }
        
    }
    
}
