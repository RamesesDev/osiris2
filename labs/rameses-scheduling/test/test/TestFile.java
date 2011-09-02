/*
 * TestFile.java
 * JUnit based test
 *
 * Created on July 19, 2011, 8:44 AM
 */

package test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import junit.framework.*;

public class TestFile extends TestCase {
    
    public TestFile(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testHello() throws Exception {
        File file = new File("d:/zzz.txt");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file,true);
        byte[] data = ("hello\n").getBytes();
        
        ByteBuffer buffer = ByteBuffer.allocate(100);
        buffer.put( data );
        buffer.flip();
        
        FileChannel writeChannel = fos.getChannel();
        writeChannel.write( buffer );
        System.out.println(writeChannel.position());
        writeChannel.close();
        
        /*
        File file = new File("d:/xsample.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel writer = raf.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(9);
        buffer.put( "JJJ<<<<<<".getBytes() );
        buffer.flip();
        //fos.write( "EFG<<<<<<".getBytes(), 0,9 );
        writer.write( buffer );
         
        writer.close();
        System.out.println("finished");
         */
        
        
    }
    
}
