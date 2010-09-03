package test;
import java.text.ParseException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.UIManager;
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
        Set s = UIManager.getLookAndFeel().getDefaults().entrySet();
        for(Object o: s) {
            Map.Entry me = (Entry) o;
            if ( (me.getKey()+"").indexOf("Box") != -1 ) {
                System.out.println( me );
            }
        }
        
    }
    
}
