package test;
import java.text.ParseException;
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
        String workunitid = "module1:sample";
        
        String name = "create";
        Matcher m = Pattern.compile("(?:(.*):)?[^\\.]*\\.[^\\.]*$").matcher(name);
        if ( m.matches() ) {
            if ( m.group(1) == null ) {
                name = workunitid.split(":")[0] + ":" + name;
            }
        }
        else {
            name = workunitid + "." + name;
        }
        System.out.println( name );
    }
    
}
