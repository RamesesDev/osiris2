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
        System.setProperty("web.app.url", "http://localhost:8080");
        
        String s = "app.url=${web.app.url}/something";
        
        StringBuffer sb = new StringBuffer();
        Matcher m = Pattern.compile("\\$\\{(.*)\\}").matcher(s);
        boolean result = m.find();
        while(result) {
            m.appendReplacement(sb, System.getProperty(m.group(1)) );
            result = m.find();
        }
        m.appendTail(sb);
        
        System.out.println("output: " + sb);
    }
    
}
