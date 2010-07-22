package test.io;
import com.rameses.io.FileTransferException;
import com.rameses.io.FileTransferInfo;
import com.rameses.io.FileTransferInputStream;
import com.rameses.io.FileTransferOutputStream;
import com.rameses.io.TargetFileExistsException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import javax.swing.JOptionPane;
/*
 * FTUtil.java
 *
 * Created on July 21, 2010, 4:06 PM
 * @author jaycverg
 */

public class FTUtil {
    
    public static boolean transfer(File f, String destFname) {
        FileTransferInputStream sis = null;
        FileTransferInfo fileInfo = null;
        ByteArrayOutputStream baos = null;
        byte[] bytes = null;
        try {
            sis = new FileTransferInputStream( f );
            sis.setByteSize(1024*32);
            bytes = sis.readNext();
            sis.saveRestorePoint();
            fileInfo = sis.getFileTransferInfo();
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try { sis.close(); } catch(Exception ign){;}
        }
        
        //transfer output
        FileTransferOutputStream fos = null;
        try {
            fos = new FileTransferOutputStream(fileInfo, destFname);
            fos.write( bytes );
        } catch(TargetFileExistsException tfe ) {
            fileInfo.delete();
        } catch(FileTransferException fe) {
            try {
                JOptionPane.showMessageDialog(null,fe.getMessage());
                long restorePoint = fe.getCurrentPos();
                fileInfo.setBytesRead( restorePoint );
                fileInfo.save();
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalStateException(e);
        } finally {
            try {fos.close(); } catch(Exception ign){;}
        }
        
        if ( fileInfo.isEof() ) fileInfo.delete();
        
        return !fileInfo.isEof();
    }
    
    public static void main(String[] args) {
        String src = "/home/rameses/Desktop/testfile.zip";
        String dest = "/home/rameses/testfile.zip";
        while ( transfer(new File(src), dest) );
    }
}
