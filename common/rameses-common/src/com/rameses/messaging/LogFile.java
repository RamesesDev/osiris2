/*
 * LogFile.java
 * Created on July 26, 2011, 8:41 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.messaging;

import com.rameses.util.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author jzamss
 */
public class LogFile  {
    
    private File file;
    
    //initial size is 5MB limit
    private long maxSize = 500000;
    private int allocation = 1024;
    private byte delimiter = (byte)1000;
    
    //this is retrieved during opening of the file.
    private long writePosition;
    private boolean closed;
    
    /** Creates a new instance of LogFile */
    public LogFile(File f) {
        file = f;
        FileInputStream fis = null;
        FileChannel readChannel = null;
        try {
            fis = new FileInputStream(file);
            ByteBuffer buffer = ByteBuffer.allocate(12);
            readChannel = fis.getChannel();
            readChannel.read( buffer, 0 );
            buffer.flip();
            StringBuffer sb = new StringBuffer();
            while(buffer.hasRemaining()) {
                char c = (char)buffer.get();
                if( c == '#') {
                    closed = true;
                    break;
                }
                else if(c == ' ') {
                    break;
                }
                else {
                    sb.append( c );
                }
            }
            String sval = sb.toString().trim();
            if(sval.length()==0) {
                this.writePosition = 12;
                savePosition(12,false);
            }
            else {
                this.writePosition = Long.parseLong(sval);    
            }
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {fis.close();}catch(Exception e){;}
            try {readChannel.close();}catch(Exception e){;}
        }
    }
    
    public void forceClose() {
        this.closed = true;
        this.savePosition( this.writePosition, true );
    }
    
    private void savePosition(long pos, boolean _closed) {
        RandomAccessFile rf = null;
        FileChannel w = null;
        try {
            rf= new RandomAccessFile(file,"rw");
            w = rf.getChannel();
            String _pos = Long.toString(pos) ;
            if(_closed) _pos = _pos + "#";
            String p = StringUtil.padRight(_pos,' ',12) + "\n";
            ByteBuffer buffer = ByteBuffer.allocate(13);
            buffer.put( p.getBytes() );
            buffer.flip();
            w.write(buffer, 0);
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { w.close(); } catch(Exception ign){;}
            try { rf.close(); } catch(Exception ign){;}
        }
    }
    
    /**
     * appends data to the end of the file.
     * if the data exceeds max size, this log file will be marked closed.
     */
    public synchronized boolean write(byte[] data) {
        if(closed) return false;
        FileChannel writeChannel = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(allocation);
            buffer.put( data );
            buffer.put( delimiter );
            buffer.flip();
            writeChannel = (new FileOutputStream(file,true)).getChannel();
            writeChannel.write( buffer );
            this.writePosition = writeChannel.position();
            if( this.maxSize > 0 && this.writePosition > this.maxSize) {
                this.closed = true;
                savePosition(this.writePosition, true);  
            }
            else {
                savePosition(this.writePosition, false);    
            }
            return true;
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try { writeChannel.close(); } catch(Exception e){;}
        }
    }
    
    /**
     * this interface is used for collecting bytes from the read 
     * delimited by the delimiter.
     */
    public static interface ReadDataCollector {
        void fetchData(byte[] bytes);
    }
    
    /**
     *
     *
     */
    public boolean read(ReadDataCollector collector ) {
        return read( collector, 13 );
    }
    public boolean read(ReadDataCollector collector, long readPosition ) {
        FileChannel readChannel = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            if(readPosition < 13 ) readPosition = 13;
            ByteBuffer buffer = ByteBuffer.allocate(allocation);
            fis = new FileInputStream(file);
            readChannel = fis.getChannel();
            readChannel.position(readPosition);
            bos = new ByteArrayOutputStream();
            int bytesRead = readChannel.read(buffer);
            if( bytesRead != -1 ) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    if( b == delimiter ) {
                        byte[] bytes = bos.toByteArray();
                        if(bytes.length>0) {
                            collector.fetchData(bytes);
                            bos.reset();
                        }
                    } else {
                        bos.write( b );
                    }
                }
                return true;
            }
            else {
                return false;
            }
        }
        catch(Exception e) {
            return false;
        }
        finally {
            try { fis.close();} catch(Exception e){;}
            try { readChannel.close();} catch(Exception e){;}
        }
    }

    public long getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
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

    public long getWritePosition() {
        return writePosition;
    }

    public boolean isClosed() {
        return closed;
    }
    
        
}
