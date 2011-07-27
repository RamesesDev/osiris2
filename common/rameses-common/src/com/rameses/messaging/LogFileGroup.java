/*
 * LogFileGroup.java
 * Created on July 26, 2011, 11:44 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.messaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 *
 * @author jzamss
 */
public class LogFileGroup {
    
    private String rootPath  = System.getProperty("user.dir");
    private String name;
    private int index = 0;
    private File logindexfile;
    private LogFile logfile;
    
    private long maxSize = 500000;
    private int allocation = 1024;
    private byte delimiter = (byte)1000;
    
    
    public LogFileGroup(String name, String rootPath) {
        this.rootPath = rootPath;
        this.name = name;
        
        try {
            //prepare first the rootPath and names
            File f = new File(rootPath + "/" + name );
            if(!f.exists()) {
                f.mkdirs();
            }
            
            //find the index file. check the last index
            logindexfile = new File(rootPath + "/" + name + "/logindex" );
            if(!logindexfile.exists()) {
                logindexfile.createNewFile();
                this.index = 0;
                saveIndex(0);
            } else {
                this.index = getIndex();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private int getIndex() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream( logindexfile);
            int i = 0;
            StringBuffer sb = new StringBuffer();
            while((i=fis.read())!=-1) {
                sb.append( (char)i);
            }
            String sval = sb.toString().trim();
            if(sval.length()>0) {
                return Integer.parseInt(sval);
            } else {
                return 0;
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {fis.close();}catch(Exception e){;}
        }
    }
    
    private void saveIndex(int idx) {
        FileOutputStream fos = null;
        try {
            fos= new FileOutputStream(logindexfile,false);
            fos.write((idx+"").getBytes());
        } catch(Exception e){
            throw new RuntimeException(e);
        } finally {
            try {fos.close();} catch(Exception ign){;}
        }
    }
    
    private LogFile getLogFile(int idx, boolean createIfNotExist)  {
        try {
            String filename = "queue-" + idx +".log";
            File f = new File( rootPath + "/" + name + "/" + filename );
            if( createIfNotExist && !f.exists()) f.createNewFile();
            if(!f.exists()) return null;
            LogFile l =  new LogFile(f);
            l.setMaxSize(maxSize);
            l.setAllocation(allocation);
            l.setDelimiter(delimiter);
            return l;
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public int getCurrentIndex() {
        return index;
    }
    
    public boolean write(byte[] data)  {
        try {
            if(this.logfile==null) {
                this.logfile = getLogFile(this.index,true);
            }
            boolean test = logfile.write( data );
            if(!test) {
                this.index = this.index + 1;
                saveIndex(this.index);
                this.logfile = getLogFile(this.index,true);
                return this.logfile.write( data );
            }
            return true;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }
    
    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }
    
    public void setDelimiter(byte delimiter) {
        this.delimiter = delimiter;
    }
    
    //map here returns the index and the position of the last record
    public boolean read( FileGroupCollector collector ) {
        return read(collector,this.index,0);
    }
    
    public boolean read( FileGroupCollector collector, int fromIndex ) {
        return read(collector,fromIndex,0);
    }
     
    public boolean read( FileGroupCollector collector, int fromIndex, long fromPosition ) {
        LogFile readLog = getLogFile(fromIndex,false);
        if(readLog==null) return false;

        boolean hasFetched = readLog.read(collector,fromPosition);
        //if log is closed proceeed with next 
        if(readLog.isClosed() ) {
            return read(collector,fromIndex+1,0);
        }
        else {
            return hasFetched;
        }
    }
    
    public static abstract class FileGroupCollector implements LogFile.ReadDataCollector {
        private int index;
        private long position;
        
        public void setIndex(int index) {
            this.index = index;
        }
        public void setPosition(long pos) {
            this.position = pos;
        }

        public int getIndex() {
            return index;
        }

        public long getPosition() {
            return position;
        }
    }
    
    
}
