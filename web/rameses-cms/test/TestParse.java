import junit.framework.*;
/*
 * TestParse.java
 * JUnit based test
 *
 * Created on July 2, 2012, 7:31 AM
 */

/**
 *
 * @author Elmo
 */
public class TestParse extends TestCase {
    
    public TestParse(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        String cat = "/cat";
        System.out.println(cat.indexOf("/",1));
    }

}
