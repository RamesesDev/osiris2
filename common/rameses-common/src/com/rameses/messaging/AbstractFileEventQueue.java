/*
 * QueueProcessor.java
 *
 * Created on July 23, 2011, 9:32 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.messaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author jzamss
 */
public abstract class AbstractFileEventQueue extends AbstractEventQueue {
    
    private String name;
    private String rootPath  = System.getProperty("user.dir");
    private int allocation = 1024;
    private byte delimiter = (byte)1000;
    private File logindexfile;
    
    public AbstractFileEventQueue(String name, String rootPath ) {
        this.name = name;
        if(this.rootPath!=null) this.rootPath=rootPath;
        init();
    }
    
    protected FileQueueReader getQueueReader(String filename, boolean createIfNotExist) {
        File file = new File(rootPath + "/" + name + "/" + filename);
        if(!file.exists()){
            if( createIfNotExist ) {
                try { 
                    file.createNewFile();
                } 
                catch(Exception ign){
                    throw new RuntimeException(ign);
                }
            }
            else {
                return null;
            }
        }    
        FileQueueReader rp = new FileQueueReader(file,invoker);
        rp.setAllocation(allocation);
        rp.setDelimiter(delimiter);
        return rp;
    }
    
    public String getName() {
        return name;
    }
    
    public String getRootPath() {
        return rootPath;
    }
    
    public int getAllocation() {
        return allocation;
    }
    
    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }
    
    public byte getDelimiter() {
        return delimiter;
    }
    
    public void setDelimiter(byte delimiter) {
        this.delimiter = delimiter;
    }

    private File getLogIndexFile() throws Exception {
        if(logindexfile==null) {
            logindexfile = new File(rootPath + "/" + name + "/logindex" );
            if(!logindexfile.exists()) logindexfile.createNewFile();
        }
        return logindexfile;
    }
    
    protected void saveIndex(String value) {
        FileOutputStream fos = null;
        try {
            fos= new FileOutputStream(getLogIndexFile(),false);
            fos.write(value.getBytes());
        } catch(Exception e){
            throw new RuntimeException(e);
        } finally {
            try {fos.close();} catch(Exception ign){;}
        }
    }
    
    protected String getIndex() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream( getLogIndexFile() );
            int i = 0;
            StringBuffer sb = new StringBuffer();
            while((i=fis.read())!=-1) {
                sb.append( (char)i);
            }
            return sb.toString();
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {fis.close();}catch(Exception e){;}
        }
    }
    
}
