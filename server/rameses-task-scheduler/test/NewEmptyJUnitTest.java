import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on June 28, 2011, 8:00 AM
 */

/**
 *
 * @author jzamss
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    private static class MyWorker1 implements Runnable {
        public void run() {
            try{
                Thread.sleep(5000);
                System.out.println("firing MyWorker 1");
            } catch(Exception e){;}
        }
    }
    
    private static class MyWorker2 implements Runnable {
        public void run() {
            try{
                Thread.sleep(10000);
                System.out.println("firing MyWorker 2");
            } catch(Exception e){
                System.out.println("interrupted " + e.getClass());
            }
        }
    }
    
    public void testRun()throws Exception {
        ExecutorService e = Executors.newCachedThreadPool();
        
        List<Future> futures = new ArrayList();
        futures.add( e.submit( new MyWorker2() ));
        futures.add( e.submit( new MyWorker1() ));
        e.shutdown();
        while(e.isTerminated()) {
            
        }
        /*
        System.out.println("starting");
        for(Future f: futures) {
            try {
                f.get(5000, TimeUnit.MILLISECONDS);
            }
            catch(Exception ex) {
                System.out.println("interrupred main thread " + ex.getClass());
            }
        }
         */
        System.out.println("ended");
    }
    
}
