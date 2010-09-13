package test;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ControlSupport;
import java.text.ParseException;
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
        Opener op = new Opener();
        Binding b = new Binding();
        ControlSupport.initOpener(op, b.getController());
        Object o = ControlSupport.init(op.getController(), op.getParams(), op.getAction());
    }
    
}
