/*
 * Tester.java
 * JUnit based test
 *
 * Created on October 15, 2010, 10:19 AM
 */

package test;

import com.rameses.scripting.impl.ScriptServiceImpl;
import com.rameses.util.DateUtil;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;
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
    
    public void testDate() {
        Date d1 = Timestamp.valueOf("2011-06-27 04:51:00");
        System.out.println(DateUtil.diff(d1,new Date(),Calendar.HOUR));
        
        LinkedBlockingQueue lk = new LinkedBlockingQueue(3);
        lk.add("test1");
        lk.add("test2");
        lk.add("test3");
        lk.add("test4");
        lk.add("test5");
        lk.add("test6");
        lk.add("test7");
        lk.add("test8");
        lk.add("test9");
        
        Vector v = new Vector();
        lk.drainTo(v);
        System.out.println("size of pool " + lk.size());
        System.out.println("size of target " + v.size());
        for(int i=0; i<2; i++) {
            lk.add(v.get(i));
        }
        System.out.println("***************");
        System.out.println("size of pool " + lk.size());
        System.out.println("size of target " + v.size());
        Object d = null;
        while( (d=lk.poll())!=null ) {
            System.out.println(d);
        }
    }

    public void xtestSampleExecute() throws Exception {
        ScriptServiceImpl svc = new ScriptServiceImpl();
        System.out.println( svc.invoke("SampleScript", "test", new Object[]{"non-loc-test"}, new HashMap()) );
        System.out.println("*********************************");
        System.out.println( svc.invoke("SampleScript", "localtest", new Object[]{"loc-test"}, new HashMap()) );
        System.out.println("*********************************");
        System.out.println( svc.invoke("SampleScript", "asyncTest", new Object[]{"async-test"}, new HashMap()) );
    }


}
