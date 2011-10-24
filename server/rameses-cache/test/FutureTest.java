import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import junit.framework.*;
/*
 * FutureTest.java
 * JUnit based test
 *
 * Created on September 27, 2011, 11:35 PM
 */

/**
 *
 * @author jzamss
 */
public class FutureTest extends TestCase {
    
    public FutureTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        List<Future> list = new ArrayList();
        ExecutorService sv = Executors.newCachedThreadPool();
        list.add(sv.submit(new XRunnable("Hello1")));
        list.add(sv.submit(new XRunnable("Hello2")));
        list.add(sv.submit(new XRunnable("Hello3")));
        for(Future f: list) {
            try {
                System.out.println(f.get());
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    private class XRunnable implements Callable {
        private String name;
        
        public XRunnable(String n) {
            this.name = n;
        }

        public Object call() throws Exception {
            return name;
        }
    }
    
}
