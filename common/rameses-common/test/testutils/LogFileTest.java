/*
 * LogFileTest.java
 * JUnit based test
 *
 * Created on July 26, 2011, 12:43 PM
 */

package testutils;

import com.rameses.messaging.LogFile;
import com.rameses.messaging.LogFileGroup;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class LogFileTest extends TestCase {
    
    public LogFileTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestBasicLog() throws Exception {
        LogFileGroup lgroup = new LogFileGroup("test1", "d:/zzlogtest");
        lgroup.setMaxSize(100);
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 1\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 2\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 3\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 4\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 5\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 6\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 7\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 8\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 9\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 10\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 11\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 12\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 13\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 14\n".getBytes()) );
        assertTrue( lgroup.write("The quick brown fox jumped over the lazy dog 15\n".getBytes()) );
        System.out.println(lgroup.getCurrentIndex());
    }
    
    public void xtestIndex() {
        LogFileGroup lgroup = new LogFileGroup("test1", "d:/zzlogtest");
        assertEquals(lgroup.getCurrentIndex(), 7 );
    }
    
    public void testRead() {
        
    }
    
    private static class MyReadHandler implements LogFile.ReadDataCollector {
        public void fetchData(byte[] bytes) {
            String n = new String(bytes);
            System.out.println(n);
        }
    }
    
    
}
