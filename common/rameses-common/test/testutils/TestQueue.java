/*
 * TestQueue.java
 * JUnit based test
 *
 * Created on July 26, 2011, 11:03 AM
 */

package testutils;

import com.rameses.messaging.LogFile;
import java.io.File;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestQueue extends TestCase {
    
    public TestQueue(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void XtestLogFile1() throws Exception {
        File f = new File("d:/test1/test.log");
        if(!f.exists()) {
            //f.mkdirs();
            (new File("d:/test1")).mkdirs();
            f.createNewFile();
        }
        LogFile lf = new LogFile(f);
        lf.setMaxSize(500);
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 1\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 2\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 3\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 4\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 5\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 6\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 7\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 8\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 9\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 10\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 11\n".getBytes()) );
        assertFalse( lf.write("The quick brown fox jumped over the lazy dog 12\n".getBytes()) );
        
        System.out.println("about to reopen another file");
        LogFile lf2 = new LogFile(f);
        lf2.setMaxSize(500);
        assertTrue( lf2.isClosed() );
        System.out.println( "write pos " + lf2.getWritePosition()  );
        
        lf2.read(new MyReadHandler());
    }

    public void testLogFile2() throws Exception {
        File f = new File("d:/test1/test.log");
        if(!f.exists()) {
            //f.mkdirs();
            (new File("d:/test1")).mkdirs();
            f.createNewFile();
        }
        LogFile lf = new LogFile(f);
        lf.setMaxSize(0);
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 1\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 2\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 3\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 4\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 5\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 6\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 7\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 8\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 9\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 10\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 11\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 12\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 13\n".getBytes()) );
        assertTrue( lf.write("The quick brown fox jumped over the lazy dog 14\n".getBytes()) );
        lf.forceClose();
        assertFalse( lf.write("The quick brown fox jumped over the lazy dog 15\n".getBytes()) );
        assertTrue(lf.isClosed());
    }
    
    private static class MyReadHandler implements LogFile.ReadDataCollector {
        public void fetchData(byte[] bytes) {
            String n = new String(bytes);
            System.out.println(n);
        }
    }

    public void xtestFail() throws Exception {
        File f = new File("d:/test1/test.log");
        LogFile lf = new LogFile(f);
        assertTrue( lf.read(new MyReadHandler(), 450) );
    }
    
    
}
