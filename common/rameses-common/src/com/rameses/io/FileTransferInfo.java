/*
 * TransferFile.java
 *
 * Created on July 20, 2010, 10:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.io;

import java.io.File;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Date;

/**
 * This is used by the FileTransferInputStream and FileTransferOutputStream
 * Data is serialzable. 
 */
public class FileTransferInfo implements Serializable {
    
    private String tempId;
    private String name;
    private String path;
    private long lastModified;
    private long dtstarted;
    private String fileExt;
    private long size;
    private long bytesRead; 
    private boolean eof;
    
    /** Creates a new instance of TransferFile */
    public FileTransferInfo(File f) {
        name = f.getName();
        int idx =name.indexOf("."); 
        if(idx>0) fileExt = name.substring(idx+1);
        tempId = "F"+new UID() + (fileExt!=null?"":"."+fileExt); 
        path = f.getPath();
        size = f.length();
        lastModified = f.lastModified();
        dtstarted = (new Date()).getTime();
    }

    public String getTempId() {
        return tempId;
    }

    public String getName() {
        return name;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getFileExt() {
        return fileExt;
    }
    public long getSize() {
        return size;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public String getPath() {
        return path;
    }

    public String getFileId() {
        return this.tempId + "." + fileExt;
    }

    public long getDtstarted() {
        return dtstarted;
    }

    public boolean isEof() {
        return eof;
    }

    public void setEof(boolean eof) {
        this.eof = eof;
    }

}
