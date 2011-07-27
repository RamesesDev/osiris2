/*
 * BatchLogQueueProcessor.java
 * Created on July 23, 2011, 10:28 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.messaging;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author jzamss
 */
public class BasicFileEventQueue extends AbstractFileEventQueue  {
    
    private long limit = 5000000;
    private long position;
    private File logindexfile;
    private long logindex;
    private FileQueueReader reader;
    private boolean started;
    
    public BasicFileEventQueue(String name, String rootPath) {
        super(name, rootPath);
    }
    
    //find the latest log file and set it.
    public void init() {
        try {
            //locate first all other records
            File dir = new File(getRootPath() + "/" + getName() + "/");
            if(!dir.exists()) dir.mkdirs();
            String lastIdx = super.getIndex();
            if(lastIdx.trim().length()>0) {
                this.logindex = Long.parseLong(lastIdx);
            }
            this.reader = super.getQueueReader(generateFileName(this.logindex), true);
            
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void start() {
        if(started) return;
        started = true;
        if(this.logindex>0) {
            //check previous by deducting logindex number.
            long test = this.logindex - 1;
            while(test >= 0) {
                FileQueueReader rp = super.getQueueReader(generateFileName(test),false);
                if(rp!=null && !rp.isClosed()) {
                    ExecutorService svc = Executors.newCachedThreadPool();
                    svc.submit(rp);
                    rp.stopLater();
                }
                test = test - 1;
            }
        }
        ExecutorService mainThread = Executors.newFixedThreadPool(1);
        mainThread.submit( this.reader );
    }
    
    public void stop() {
        reader.stop();
        started = false;
    }
    
    private String generateFileName(long idx) {
        return "queue-" + idx +".log";
    }
    
    private FileQueueReader createNewProcessor() throws Exception {
        this.logindex = this.logindex + 1;
        FileQueueReader rp = super.getQueueReader(generateFileName(this.logindex), true);
        super.saveIndex(Long.toString(logindex));
        if(started){
            ExecutorService mainThread = Executors.newFixedThreadPool(1);
            mainThread.submit(rp);
        }
        return rp;
    }
    
    public synchronized void send(QueueMessage qm) {
        //open a new queue here
        FileChannel writeChannel = null;
        try {
            if(this.position > this.limit ) {
                //stop the old and run the new
                this.reader.stopLater();
                this.reader = createNewProcessor();
            }
            
            byte[] data = qm.getMessage().toString().getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(super.getAllocation());
            buffer.put( data );
            buffer.put( super.getDelimiter() );
            buffer.flip();
            
            writeChannel = (new FileOutputStream(reader.getFile(),true)).getChannel();
            writeChannel.write( buffer );
            this.position = writeChannel.position();
            
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try { writeChannel.close(); } catch(Exception e){;}
        }
    }
    
    public long getLimit() {
        return limit;
    }
    
    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void shutdown() {
        super.destroy();
    }



    

    
    
}
