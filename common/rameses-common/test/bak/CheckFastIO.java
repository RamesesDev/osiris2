/*
 * CheckFastIO.java
 * JUnit based test
 *
 * Created on June 30, 2011, 9:07 PM
 */

package bak;

import java.io.File;
import java.io.FileInputStream;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class CheckFastIO extends TestCase {
    
    public CheckFastIO(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() throws Exception {
        File f = new File("D:/server.log");
        FileInputStream is = new FileInputStream(f);
        StringBuffer sb = new StringBuffer();
        is.skip(125000);
        byte[] b = new byte[1000];
        is.read( b, 0, 1000);
        for(int i=0; i<b.length;i++) {
            sb.append(i);
        }
        is.close();
        System.out.println(sb.toString());
    }

}
