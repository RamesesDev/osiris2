/*
 * LogReaderQueueProcessor.java
 * Created on July 23, 2011, 9:35 AM
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
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author jzamss
 */
public class FileQueueReader implements Runnable,Serializable   {
    
    private File file;
    private boolean cancelled = false;
    private boolean ended;
    private int allocation = 1024;
    private byte delimiter = (byte)1000;
    private MessageInvoker invoker;
    private long position = 12;
    
    private EventQueueListener listener;
    
    //file must exist
    public FileQueueReader(File f, MessageInvoker invoker ) {
        this.file = f;
        this.invoker = invoker;
        this.position = retrievePosition();
    }
    
    public boolean isClosed() {
        return this.position < 0;
    }
    
    //if this returns -1 then this is closed already
    private long retrievePosition() {
        FileChannel r = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int k = 0;
            ByteBuffer buffer = ByteBuffer.allocate(12);
            r = fis.getChannel();
            r.read( buffer, 0 );
            buffer.flip();
            StringBuffer sb = new StringBuffer();
            while(buffer.hasRemaining()) {
                sb.append( (char)buffer.get() );
            }
            String val = sb.toString();
            if(val.trim().length()==0) {
                savePosition(12,false);
                return 12;
            }    
            else {
                String sval = sb.toString().trim();
                if(sval.endsWith("#")) {
                    return -1;
                }
                return Long.parseLong(sval);
            }    
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {fis.close();} catch(Exception e){;}
            try {r.close();} catch(Exception e){;}
        }
    }
    
    private void savePosition(long position, boolean ended) throws Exception {
        RandomAccessFile rf = null;
        FileChannel w = null;
        try {
            rf= new RandomAccessFile(file,"rw");
            w = rf.getChannel();
            String _pos = Long.toString(position) ;
            if(ended) _pos = _pos + "#";
            String p = StringUtil.padRight(_pos,' ',12) + "\n";
            ByteBuffer buffer = ByteBuffer.allocate(13);
            buffer.put( p.getBytes() );
            buffer.flip();
            w.write(buffer, 0);
        } catch(Exception e) {
            throw e;
        } finally {
            try { w.close(); } catch(Exception ign){;}
            try { rf.close(); } catch(Exception ign){;}
        }
    }
    
    
    public void run() {
        System.out.println("START RUNNING FILE " + file.getName());
        FileChannel readChannel = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos  = null;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(allocation);
            fis = new FileInputStream(file);
            readChannel = fis.getChannel();
            readChannel.position(this.position);
            bos = new ByteArrayOutputStream();
            if(listener!=null) listener.onStart();
            while(!cancelled) {
                int bytesRead = readChannel.read(buffer);
                if(bytesRead != -1) {
                    this.position = this.position + buffer.position();
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        byte b = buffer.get();
                        if( b == delimiter ) {
                            byte[] bytes = bos.toByteArray();
                            if(bytes.length>0) {
                                ByteQueueMessage qm = new ByteQueueMessage(bytes);
                                invoker.invoke(qm);
                                bos.reset();
                            }
                        } else {
                            bos.write( b );
                        }
                    }
                    
                    //flush remaining entry in the buffer
                    buffer.clear();
                    savePosition(this.position, false);
                }
                
                //check if ended
                if( ended ) {
                    savePosition(this.position, true);
                    break;
                }
            }
            System.out.println("stopping the queue for "+file.getName());
            if(listener!=null) listener.onStop();
            cancelled = false;
        } 
        catch(Exception ign){
            ign.printStackTrace();
        } 
        finally {
            try { fis.close();} catch(Exception e){;}
            try { readChannel.close();} catch(Exception e){;}
        }
    }
    
    public void stop() {
        cancelled = true;
    }
    
    public void stopLater() {
        ended = true;
    }

    public void setAllocation(int allocation) {
        this.allocation = allocation;
    }

    public void setDelimiter(byte delimiter) {
        this.delimiter = delimiter;
    }

    public File getFile() {
        return file;
    }

    public EventQueueListener getListener() {
        return listener;
    }

    public void setListener(EventQueueListener listener) {
        this.listener = listener;
    }
    
}
