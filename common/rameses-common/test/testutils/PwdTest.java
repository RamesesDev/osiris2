/*
 * PwdTest.java
 * JUnit based test
 *
 * Created on November 26, 2010, 9:59 AM
 */

package testutils;

import java.util.concurrent.ArrayBlockingQueue;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class PwdTest extends TestCase {
    
    public PwdTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        //System.out.println( Encoder.MD5.encode( "loren", "12345"));
        //"e791b43af91c0523b2095b3841413ece"
        //Map map = new HashMap();
        //map.put("filter", " where lastname like $P{lastname}");
        //String s = "select o from where ${filter} ";
        //System.out.println( ExprUtil.substituteValues( s, map ));
        ArrayBlockingQueue aq = new ArrayBlockingQueue(2);
        aq.add("test1");
        aq.add("test2");
        aq.remove("test2");
        aq.add("test3");
        
    }

}
