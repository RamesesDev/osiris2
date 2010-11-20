/*
 * Test2.java
 * JUnit based test
 *
 * Created on November 8, 2010, 2:38 PM
 */

package test;

import com.rameses.osiris2.client.WorkUnitUIController;
import java.util.Map;
import java.util.Set;
import javax.swing.UIManager;
import junit.framework.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author rameses
 */
public class Test2 extends TestCase {
    
    
    public Test2(String testName) {
        super(testName);
    }
    
    public void testHello() {
//        EntityManager em;
//        Binding b;
        WorkUnitUIController c;
        //c.getWorkunit().getWorkunit();
//        InvokerAction ia;
//        InvokerUtil.lookupOpeners("", null);
//        Opener o;
        
        Set<Map.Entry<Object, Object>> set = (Set<Map.Entry<Object, Object>>) UIManager.getLookAndFeel().getDefaults().entrySet();
        for(Map.Entry me: set) {
            if ( me.getKey().toString().contains("control") )
                System.out.println(me);
        }
    }
    
//    private class Handler extends DefaultHandler {
//        
//    }
    
}
