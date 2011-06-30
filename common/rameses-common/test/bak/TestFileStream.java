/*
 * TestFileStream.java
 * JUnit based test
 *
 * Created on June 28, 2011, 8:52 PM
 */

package bak;

import com.rameses.invoker.client.DynamicHttpTransfer;
import com.rameses.io.FileTransfer;
import com.rameses.io.FileTransfer.FileInputSource;
import com.rameses.io.FileTransfer.FileOutputHandler;
import java.io.File;
import junit.framework.*;

/**
 *
 * @author jzamss
 */
public class TestFileStream extends TestCase {
    
    public TestFileStream(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {

    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void xtestBasicTransfer() throws Exception {
        FileTransfer fu = new FileTransfer();
        FileInputSource fi = new FileTransfer.FileInputSource(new File("d:/CLFC_SCREEN.JPG"),1024*2);
        FileOutputHandler fo = new FileTransfer.FileOutputHandler(new File("d:/downloads/pic1.jpg"));
        fu.transfer(fi,fo, new ProcessListener());
    }

    public void xtestRemoteUpload() throws Exception {
        FileTransfer fu = new FileTransfer();
        FileInputSource fi = new FileTransfer.FileInputSource(new File("d:/CLFC_SCREEN.JPG"));
        DynamicHttpTransfer.Out doo = new DynamicHttpTransfer.Out("localhost:8080","upcebu","FileUploadService","upload");
        doo.getParameters().put( "filename","jomaski.jpg" );
        fu.transfer( fi, doo, new ProcessListener() );
    }
    
    /*
    public void xtestDownload() throws Exception {
        FileTransfer fu = new FileTransfer();
        File out = new File("D:/NEW_TRANSFER.txt");
        FileInputSource fi = new FileTransfer.FileInputSource(new File("D:/NEW_BUGS_FOUND.txt"), 100);
        FileOutputHandler fo = new FileTransfer.FileOutputHandler(out );
        fi.setPosition(out.length());
        byte[] bts = fi.read();
        if(bts!=null && bts.length>0) 
            fo.write( bts );
        else
            System.out.println("bytes is null");
        fi.close();
        fo.close();
    }
    */
    
    private class ProcessListener implements FileTransfer.TransferListener {
        public void start(long startAt) {
            System.out.println("starting bytes " + startAt);
        }
        public void process(long bytesTransferred) {
            System.out.println("bytes transferred " + bytesTransferred);
        }
        public void end(long totalTransferred) {
            System.out.println("finished total bytes transferred " + totalTransferred);
        }        
    }
    
     public void testRemoteDownload() throws Exception {
        FileTransfer fu = new FileTransfer();
        DynamicHttpTransfer.In din = new DynamicHttpTransfer.In("localhost:8080","upcebu","FileUploadService","download");
        din.getParameters().put("filename","jomaski.jpg");
        FileOutputHandler fo = new FileTransfer.FileOutputHandler(new File("d:/downloads/image2.jpg"));
        fu.transfer( din, fo, new ProcessListener() );
    }
}
