import java.util.Iterator;
import java.util.Map;
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
    
    public void testHello() {
        
        Iterator itr = UIManager.getLookAndFeel().getDefaults().entrySet().iterator();
        while (itr.hasNext()) {
            Object item = itr.next();
            Map.Entry entry = (Map.Entry) item;
            String name = entry.getKey().toString();
            if (name.toLowerCase().indexOf("table") >= 0)
                System.out.println(entry);
        }
    }
    
}
