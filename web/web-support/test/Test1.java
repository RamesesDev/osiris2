import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import junit.framework.*;
/*
 * Test1.java
 * JUnit based test
 *
 * Created on May 16, 2011, 2:54 PM
 * @author jaycverg
 */

public class Test1 extends TestCase {
    
    public Test1(String testName) {
        super(testName);
    }
    
    public void testHello() throws Exception {
        File f = new File("d:/NEW_BUGS_FOUND.txt");
        FileInputStream fr = new FileInputStream(f);
        BufferedInputStream b = new BufferedInputStream(fr,1094*8);
        int j = 0;
        int counter = 0;
        while((j=b.read())!=-1) {
            System.out.println(counter++);
        }
        b.close();
        fr.close();
    }    
    
}
