/*
 * StatefulInputStream.java
 *
 * Created on July 20, 2010, 7:20 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * This mechanism reads bytes from the specified file. 
 * It creates a FileTransferInfo object to allow us to 
 * resume uploading just in case the transaction was aborted.
 * This file resides on the same path as the file and its filename
 * filename is marked with ~.
 * It is recommended to serialize the restore point
 * to assure that we can continue the transfer. 
 */
public class FileTransferInputStream extends InputStream {
    
    private long restorePoint = 0;
    private int byteSize = 1024*8;  //default value is 8 kilobytes
    private InputStream inputStream;
    private FileTransferInfo fileTransferInfo;
    private String tmpFileName;
    
    private String suffix = ".dwd";
    
    /***
     * check the following:
     * 1. check if temp file header exists. if exists, open it; if not, create new
     */
    public FileTransferInputStream(File f) {
        try {
            tmpFileName = f.getPath() + suffix;
            inputStream = new FileInputStream( f );
            boolean exists = retrieveInfo( f );
            if(exists) {
                verify( f );
                restore();
            }
        }
        catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
    
    /***
     * returns true if the file already exists.
     */
    private boolean retrieveInfo(File sourceFile) throws Exception {
        File tmpFile = new File(tmpFileName);
        if( !tmpFile.exists() ) {
            fileTransferInfo = new FileTransferInfo( sourceFile );    
            return false;
        }
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(tmpFile);
            ois = new ObjectInputStream(fis);
            fileTransferInfo = (FileTransferInfo)ois.readObject();
            return true;
        }
        catch(Exception e) {
            throw e;
        }
        finally {
            try {ois.close();} catch(Exception ign){;}
            try {fis.close();} catch(Exception ign){;}
        }
    }
    
    public void deleteTempFile() {
        File f = new File(tmpFileName);
        if(f.exists()) f.delete();
    }
    
    /**
     * check if the file has been modified.
     * if there are changes - an exception is thrown
     * but should we let the user configure what to do if an error occurs?
     * would we delete immediately, not delete, start a new session?
     */
    private void verify(File f ) throws Exception {
        //only 2 points to check. size and dtmodified
        if( f.length() != fileTransferInfo.getSize() || f.lastModified() != fileTransferInfo.getLastModified() ) {
            deleteTempFile();
            throw new TransferFileModifiedException();
        }    
    }
    
    private void restore() throws Exception {
        if(fileTransferInfo!=null) {
            restorePoint = fileTransferInfo.getBytesRead();
            int _loops = new Integer( Long.toString( restorePoint / byteSize)  );
            int _remainder = new Integer( Long.toString( restorePoint % byteSize));
            byte[] b = new byte[byteSize];
            for(int i=0; i<_loops; i++) {
                inputStream.read(b,0,byteSize);
            }
            if(_remainder > 0 ) {
                b = new byte[_remainder];
                inputStream.read(b,0,_remainder);
            }
        }
    }

    public int read() throws IOException {
        return inputStream.read();
    }
    
    public void reset() throws IOException {
        inputStream.reset();
    }
    
    /***
     * This method resets the counter to the restore point
     */
    public byte[] readNext() throws Exception {
        byte[] readByte = new byte[byteSize];
        int fetchCount = inputStream.read(readByte,0,byteSize);
        if( fetchCount < 0) {
            return null;
        } 
        else if(fetchCount < byteSize) {
            byte[] fixedBytes = new byte[fetchCount];
            for( int i=0; i<fetchCount; i++ ) {
                fixedBytes[i] = readByte[i]; 
            }
            restorePoint = restorePoint + fixedBytes.length;
            fileTransferInfo.setEof(true);
            return fixedBytes;
        }
        else {
            restorePoint = restorePoint + byteSize;
            return readByte;
        }
    }
    
    public int getByteSize() {
        return byteSize;
    }
    
    public void setByteSize(int size) {
        this.byteSize = size;
    }
    
    public long getRestorePoint() {
        return restorePoint;
    }
    
    public void close() throws IOException {
        if(fileTransferInfo.isEof())  {
            deleteTempFile();
        }
        inputStream.close();
    }

    public FileTransferInfo getFileTransferInfo() {
        return fileTransferInfo;
    }
    
    public void saveRestorePoint() throws Exception {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fileTransferInfo.setBytesRead( restorePoint );
            fos = new FileOutputStream(new File( tmpFileName ));
            oos = new ObjectOutputStream(fos);
            oos.writeObject( fileTransferInfo);
        }
        catch(Exception e) {
            throw e;
        }
        finally {
            try{ oos.close();}catch(Exception ign){;}
            try{ fos.close();}catch(Exception ign){;}
        }
    }

    
}
