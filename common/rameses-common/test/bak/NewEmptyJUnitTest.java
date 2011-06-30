/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on June 30, 2011, 9:38 AM
 */

package bak;

import com.rameses.io.FileTransfer;
import java.io.File;
import junit.framework.*;

/**
 *
 * @author jzamss
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
        FileTransfer t = new FileTransfer();
        FileTransfer.FileInputSource fs = new FileTransfer.FileInputSource(new File("c://aaa.txtx"),1024*8);
        FileTransfer.FileOutputHandler fout = new FileTransfer.FileOutputHandler(new File("c://aaa"));
        t.transfer(fs,fout);
    }

    
}
