import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on July 15, 2010, 11:07 AM
 */

/**
 *
 * @author elmo
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
    public void testHello() throws Exception {
       //ClassLoader loader = Thread.currentThread().getContextClassLoader();
       //InputStream is = loader.getResourceAsStream("META-INF/services");
       //System.out.println("input stream is " + is);
       //System.out.println(StreamUtil.toString(is));
        
       String d = "a/b/b/c/d/e/f/g/";
       System.out.println( d.substring(0,d.length()-1)  );
    }

}
