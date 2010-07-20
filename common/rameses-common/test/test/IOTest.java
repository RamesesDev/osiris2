/*
 * IOTest.java
 * JUnit based test
 *
 * Created on July 20, 2010, 7:28 AM
 */

package test;

import com.rameses.io.FileTransferInfo;
import com.rameses.io.FileTransferInputStream;
import com.rameses.io.FileTransferOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.swing.JOptionPane;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class IOTest extends TestCase {
    
    public IOTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testTransfer() throws Exception {
        File f = new File("e:/mytest.txt");
        FileTransferInputStream sis = null;
        FileTransferInfo fileInfo = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
            baos = new ByteArrayOutputStream();
            sis = new FileTransferInputStream( f );
            sis.setByteSize(200);
            byte[] b = null;
            while((b=sis.readNext())!=null) {
                baos.write( b );
                if(JOptionPane.showConfirmDialog(null,"continue?","test", JOptionPane.YES_NO_OPTION)==JOptionPane.NO_OPTION) {
                    break;
                }
            }
            bytes = baos.toByteArray();
            sis.saveRestorePoint();
            fileInfo = sis.getFileTransferInfo();
        } 
        catch(Exception e) {
            throw e;
        } finally {
            try { sis.close(); } catch(Exception ign){;}
        }
        
        //transfer output
        FileTransferOutputStream fos = null;
        try {
            fos = new FileTransferOutputStream(fileInfo, "e:/myelmotest.txt");
            fos.write( bytes );
        }
        catch(Exception e) {
            throw e;
        }
        finally {
            try {fos.close(); } catch(Exception ign){;}
        }
        
        
        
    }
    
    public void XtestOut() throws Exception {
        //File f = new File("e:/testOutput.txt");
        //System.out.println("absolute path " + f.getAbsolutePath());
        //System.out.println("canonical path " + f.getCanonicalPath());
        //System.out.println("name " + f.getName());
        //System.out.println("path " + f.getPath());
        System.getProperties().list(System.out);
    }
    
}
