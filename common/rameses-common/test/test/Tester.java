/*
 * Tester.java
 * JUnit based test
 *
 * Created on June 2, 2010, 2:26 PM
 */

package test;

import com.rameses.util.CipherUtil;
import com.rameses.util.MachineInfo;
import java.io.Serializable;
import java.text.MessageFormat;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class Tester extends TestCase {
    
    public Tester(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestMachineInfo() throws Exception {
        //System.out.println(System.getProperty("os.name"));
        MachineInfo m = MachineInfo.getInstance();
        System.out.println(m.getOs() + " " + m.getMacAddress() );
    }

    public void xtestCipher() throws Exception {
        String a = "the cat in the hat";
        Object o = CipherUtil.encode( a );
        System.out.println(CipherUtil.decode((Serializable)o));
    }
    
    public void xtestMessageFormatter() {
        Object[] arr = new Object[]{ "windhell", "jayrome"};
        System.out.println(MessageFormat.format("{0} needs help from {1}, di ba {0}?", arr));
    }
    
    public void testSplit() {
        String s = "select * from a vAlueS  b";
        System.out.println(s.split("\\s(V|v)(a|A)(l|L)(u|U)(e|E)(s|S)\\s").length);
    }
    
    
}
