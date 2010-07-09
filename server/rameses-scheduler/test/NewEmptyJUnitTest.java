import java.io.File;
import java.io.FileOutputStream;
import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on December 13, 2009, 9:59 AM
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
    public void xtestHello() {
        String method = "timeout";
        String scriptName = "serviceOne";
        if(scriptName.indexOf(".")>0) {
            String arr[] = scriptName.split("\\.");
            scriptName = arr[0];
            method = arr[1];
        }
        System.out.println("script name " + scriptName);
        System.out.println("methid " + method);
    }
    
     public void testHello() throws Exception {
        File f = new File("/Volumes/PTGN/testfile.txt");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write("Hello world test".getBytes());
        fos.flush();
        fos.close();
     }
    
}
