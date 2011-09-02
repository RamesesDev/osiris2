
import com.rameses.util.TemplateProvider;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on October 17, 2010, 1:11 PM
 */

/**
 *
 * @author ms
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void test1Hello() {
        Map map = new HashMap();
        map.put("name", "${elmo}");
        Object o = TemplateProvider.getInstance().getResult("META-INF/template.groovy", map );
        System.out.println(o);
    }
    
    public void test1Queue() {
        BlockingQueue<String> b = new LinkedBlockingQueue();
        b.add( "one" );
        b.add( "two");
        b.add( "three");
        String s = null;
        while((s=b.poll())!=null) {
            System.out.println("process " + s);
        }
        System.out.println("finished");
        b.add( "four" );
        b.add( "five");
        b.add( "six");
        s = null;
        while((s=b.poll())!=null) {
            System.out.println("process " + s);
        }
        System.out.println("finished 2");
    }
    
    public void testAdd() {
        Set m = new HashSet();
        System.out.println( m.add( "one" ) );
        System.out.println( m.add( "one" ) );
        System.out.println( m.add( "one" ) );
        Integer i = new Integer(0);
        i = i+1;
        System.out.println(i);
    }
    
}
