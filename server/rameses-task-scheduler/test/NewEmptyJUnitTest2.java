import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import junit.framework.*;
/*
 * NewEmptyJUnitTest2.java
 * JUnit based test
 *
 * Created on June 28, 2011, 9:32 AM
 */

/**
 *
 * @author jzamss
 */
public class NewEmptyJUnitTest2 extends TestCase {
    
    public NewEmptyJUnitTest2(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    private static class Worker1 implements Runnable {
        public void run() {
            try {
                Thread.sleep(20000);
                System.out.println("finished worker 1");
            }
            catch(Exception e) {;}
        }
    }
    private static class Worker2 implements Runnable {
        public void run() {
            try {
                Thread.sleep(5000);
                System.out.println("finished worker 2");
            }
            catch(Exception e) {;}
        }
    }
    
    
    public void testHello() throws Exception {
        ExecutorService svc = Executors.newCachedThreadPool();
        List<Future> list = new ArrayList();
        list.add( svc.submit( new Worker1()));
        list.add( svc.submit( new Worker2()) );
        
        for(Future f: list) {
            try {
                System.out.println("getting future "+new Date());
                f.get(5000,TimeUnit.MILLISECONDS);
            }
            catch(Exception ign){
                System.out.println(ign);
            }
        }
        
    }

}
