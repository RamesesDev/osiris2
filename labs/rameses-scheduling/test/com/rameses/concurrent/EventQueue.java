/*
 * EventQueue.java
 * Created on July 16, 2011, 8:37 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.concurrent;

import com.rameses.util.StringUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class EventQueue implements Runnable {
    
    private String id;
    private boolean started;
    private boolean cancelled;
    private long lastPos;
    private int allocation = 1024;
    private byte delimiter = (byte)1000;
    
    private File file;
    
    private List<EventHandler> handlers = Collections.synchronizedList( new ArrayList());
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public boolean isStarted() {
        return started;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public EventQueue(String id, EventHandler handler) {
        this.id = id;
        if(handler!=null) this.handlers.add(handler);
        try {
            //create the file here.
            file = new File(id);
            this.lastPos = 12;
            file.createNewFile();
            saveInfo();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void start() throws Exception {
        if(started) return;
        ExecutorService svc = Executors.newCachedThreadPool();
        svc.submit(this);
    }
    
    private void retrieveInfo() throws Exception {
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
            this.lastPos = Long.parseLong(sb.toString().trim());
        } catch(Exception e) {
            throw e;
        } finally {
            try {fis.close();} catch(Exception e){;}
            try {r.close();} catch(Exception e){;}
        }
    }
    
    private void saveInfo() throws Exception {
        RandomAccessFile rf = null;
        FileChannel w = null;
        try {
            rf= new RandomAccessFile(file,"rw");
            w = rf.getChannel();
            String _pos = Long.toString(this.lastPos);
            String p = StringUtil.padRight(_pos,' ',12)+"\n";
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
        FileChannel readChannel = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos  = null;
        
        try {
            ByteBuffer buffer = ByteBuffer.allocate(allocation);
            retrieveInfo();
            fis = new FileInputStream(file);
            readChannel = fis.getChannel();
            if(lastPos >0) readChannel.position(lastPos);
            System.out.println("starting at " + lastPos );
            
            //started only from here
            started = true;
            cancelled = false;
            bos = new ByteArrayOutputStream();
            
            while(!cancelled) {
                int bytesRead = readChannel.read(buffer);
                if(bytesRead != -1) {
                    lastPos = lastPos + buffer.position();
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        byte b = buffer.get();
                        if( b == delimiter ) {
                            byte[] bytes = bos.toByteArray();
                            if(bytes.length>0) {
                                for(EventHandler handler: handlers) {
                                    handler.onMessage(bytes);
                                }
                                bos.reset();
                            }
                        } else {
                            bos.write( b );
                        }
                    }
                    
                    //flush remaining entry in the buffer
                    buffer.clear();
                    saveInfo();
                }
            }
        } catch(Exception ign){
            ign.printStackTrace();
        } finally {
            try { fis.close();} catch(Exception e){;}
            try { readChannel.close();} catch(Exception e){;}
        }
        System.out.println("stopping the queue");
        started = false;
    }
    
    public void sendMessage( String msg ) {
        FileChannel writeChannel = null;
        try {
            File f = new File(id);
            if(!f.exists())
                throw new Exception("File does not exist");
            
            byte[] data = (msg+"\n").getBytes();
            
            ByteBuffer buffer = ByteBuffer.allocate(allocation);
            buffer.put( data );
            buffer.put( delimiter );
            buffer.flip();
            
            writeChannel = (new FileOutputStream(f,true)).getChannel();
            writeChannel.write( buffer );
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try { writeChannel.close(); } catch(Exception e){;}
        }
    }
    
    public void destroy() {
        if(file.exists())file.delete();
    }
    
}
