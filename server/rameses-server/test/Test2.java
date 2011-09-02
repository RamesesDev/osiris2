import java.security.MessageDigest;
import junit.framework.*;
/*
 * Test2.java
 * JUnit based test
 *
 * Created on August 4, 2011, 9:29 AM
 */

/**
 *
 * @author jzamss
 */
public class Test2 extends TestCase {
    
    public Test2(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello()throws Exception {
        /*
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        queue.add("a");
        queue.add("b");
        queue.add("c");
        queue.add("d");
        queue.add("e");
        queue.add("d");
        queue.add("f");
        assertEquals(queue.size(), 7);
        queue.remove("d");
        System.out.println(queue.size());
        Object x = null;
        while((x=queue.poll())!=null) {
            System.out.println(x);
        }
         */
       System.out.println("192.168.3.111".hashCode());
       MessageDigest d = MessageDigest.getInstance("MD5");
       byte[] b = d.digest( "192.168.3.111".getBytes() );
    }

}
