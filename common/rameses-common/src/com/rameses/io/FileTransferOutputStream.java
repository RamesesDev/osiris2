/*
 * StatefulOutputStream.java
 *
 * Created on July 20, 2010, 9:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * As of the moment the current implementation
 * uses a FileOutputStream to store the output
 * mainly because of its ability to write at the tail
 * which is not avaliable for basic output streams.
 * 
 * Before starting to write something, this class
 * class does the ff: 
 * 1. if file not exist, create a new file with filename specified. 
 *    create header file with the same filename but marked with .dwd2 at the end. 
 * 2.     
 * 
 */
public class FileTransferOutputStream extends OutputStream {
    
    private FileTransferInfo fileTransferInfo;
    private FileOutputStream out;
    private String filename;
    private String suffix = ".dwd2";
    private File tempFile;
    private boolean bytesVerified;
    
    public FileTransferOutputStream(FileTransferInfo fileInfo, String outputFileName) {
        try {
            filename = outputFileName;
            bytesVerified = false;
            fileTransferInfo = fileInfo;
            prepareTarget(outputFileName);
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void setSuffix( String s) {
        suffix = s;
    }
    
    /**
     * target must be marked with ~ just in case another file already exists
     * before overrwriting it.
     */
    private void prepareTarget(String fileName) throws Exception {
        if( fileName == null )
            throw new NullPointerException("Please indicate an output filename ");
        //target file    
        tempFile = new File( fileName + suffix );
        if(!tempFile.exists()) {
            tempFile.createNewFile();
            tempFile.setLastModified( fileTransferInfo.getLastModified() );
            //append at the tail
            out = new FileOutputStream(tempFile,true);
        }
        else {
            out = new FileOutputStream(tempFile,true);
            //we need to verify if original file was not changed.
            if(fileTransferInfo.getLastModified()!=tempFile.lastModified()) 
                throw new TransferFileModifiedException("Last modified of source file does not match");
        }
    }
    
    public void verifyBytes(byte[] bytesToTransfer) throws IOException {
         long currentPos = out.getChannel().size();
        //if bytes read is smaller than current pos, it probably means the sending
        //file did not save the restore point. so in this case do nothing... 
        if( fileTransferInfo.getBytesRead() != (currentPos+bytesToTransfer.length) ) {
            throw new FileTransferException( currentPos );
        }
        bytesVerified = true;
    }
    
    public void write(int b) throws IOException {
        if(!bytesVerified)
            throw new IOException("Please run bytes verify method first and verify bytes to copy");
        out.write(b);
    }
    
    public void write(byte[] bytes) throws IOException {
        if( !bytesVerified) {
            verifyBytes(bytes);
        }
        out.write(bytes);
    }
    
    /**
     * on closing the file, check if position is already the end of file.
     * if yes, we rename the file removing the filename suffix. 
     */
    public void close() throws IOException {
        out.close();
        if( fileTransferInfo.isEof() ) {
            boolean t = tempFile.renameTo( new File(filename) );
        }
        else {
            //always update so it will not produce an error.
            tempFile.setLastModified( fileTransferInfo.getLastModified() );
        }
    }
    
    public void flush() throws IOException {
        out.flush();
    }
    
    
}
