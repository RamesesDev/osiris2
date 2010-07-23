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
    public void testHello() {
        String name = "http://mytest.com";
        String[] arr = name.split("://");
        String nameSpace = arr[0];
        String resourceName = arr[1];
        System.out.println("Name space ->" + nameSpace);
        System.out.println("resource->" + resourceName);
    }

}
