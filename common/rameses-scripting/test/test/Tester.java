/*
 * Tester.java
 * JUnit based test
 *
 * Created on October 15, 2010, 10:19 AM
 */

package test;

import com.rameses.scripting.impl.ScriptServiceImpl;
import java.util.HashMap;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class Tester extends TestCase {
    
    public Tester(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    public void xtestHello() throws Exception {
        /*
        ScriptObjectPoolItem o = ScriptManager.getInstance().getScriptObject("SampleScript");
        ScriptManager.getInstance().getInterceptorManager().injectInterceptors(o, "test");
        System.out.println(o.getBeforeInterceptors());
        System.out.println(o.getAfterInterceptors());
        System.out.println("loading....");
        o = ScriptManager.getInstance().getScriptObject("SampleScript");
         */
    }

    public void testSampleExecute() throws Exception {
        ScriptServiceImpl svc = new ScriptServiceImpl();
        System.out.println( svc.invoke("SampleScript", "test", new Object[]{"non-loc-test"}, new HashMap()) );
        System.out.println("*********************************");
        System.out.println( svc.invoke("SampleScript", "localtest", new Object[]{"loc-test"}, new HashMap()) );
        System.out.println("*********************************");
        System.out.println( svc.invoke("SampleScript", "asyncTest", new Object[]{"async-test"}, new HashMap()) );
    }


}
