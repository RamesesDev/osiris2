package test;
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
    
    public void testHello() {
//        InputStream is = getClass().getResourceAsStream("style3");
//        StyleRuleParser parser = new StyleRuleParser();
//        DefaultParseHandler handler = new DefaultParseHandler();
//        try {
//            parser.parse(is, handler);
//
//            for (StyleRule r: handler.getList()) {
//                System.out.println(r);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        
        String value = "rgb(  50, 75 ,  80 )";
        Matcher m = Pattern.compile("rgb\\((\\d+),(\\d+),(\\d+)\\)")
        .matcher(value.replace(" ", ""));
        
        m.matches();
        
        System.out.println(m.group(1));
        System.out.println(m.group(2));
        System.out.println(m.group(3));
    }
    
}
