/*
 * FileTransferUtil.java
 * Created on June 28, 2011, 5:53 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 *
 * @author jzamss
 */
public final class FileTransfer {
   
    public final void transfer(InputSource in, OutputHandler out ) throws Exception {
        transfer(in,out);
    }
    
    public final void transfer(InputSource in, OutputHandler out, TransferListener listener ) throws Exception {
        try {
            byte[] b = null;
            if(listener!=null) listener.start(in.getPosition());
            while( (b=in.read())!=null ) {
                if(b.length==0) break;
                out.write(b);
                if(listener!=null) listener.process(in.getPosition());
            }
            if(listener!=null) listener.end(in.getPosition()); 
        } catch(Exception ex){
            throw ex;
        } finally {
            in.close();
            out.close();
        }
    }
    
    public static interface InputSource {
        //should return null if eof
        byte[] read() throws Exception;
        void close();
        long getPosition();
    }
    
    public static interface OutputHandler {
        void write(byte[] bytes) throws Exception;
        void close();
    }

    public static interface TransferListener {
        void start(long startAt);
        void process( long bytesTransferred);
        void end( long totalTransferred );
    }

    
    public static class FileInputSource implements InputSource {
        private InputStream bis;
        int bufferSize = 1024*8;
        boolean eof = false;
        long position = 0;
        
        public FileInputSource(InputStream s) throws Exception {
            this.bis = s;
        }
        public FileInputSource(InputStream s, int bufferSize) throws Exception {
            this.bis = s;
            this.bufferSize = bufferSize;
        }
        
        public FileInputSource(File f, int bufferSize) throws Exception {
            this( new BufferedInputStream(new FileInputStream(f)), bufferSize );
        }
        public FileInputSource(File f) throws Exception {
            this( new BufferedInputStream(new FileInputStream(f)) );
        }
        
        public void setPosition(long pos) throws Exception {
            int _loops = new Integer( Long.toString( pos / bufferSize)  );
            int _remainder = new Integer( Long.toString( pos % bufferSize));
            byte[] b = new byte[bufferSize];
            for(int i=0; i<_loops; i++) {
                bis.read(b,0,bufferSize);
            }
            if(_remainder > 0 ) {
                b = new byte[_remainder];
                bis.read(b,0,_remainder);
            }
            position = (_loops * bufferSize) + _remainder;
        }        
        
        public long getPosition() {
            return position;
        }
        
        public byte[] read() throws Exception {
            if(eof) return null;
            if( bis.available() < bufferSize ) {
                byte[] readBytes = new byte[bis.available()];
                bis.read( readBytes, 0, bis.available() );
                System.out.println("last count " + readBytes.length);
                position = position + readBytes.length;
                eof = true;
                return readBytes;
            } 
            else {
                byte[] readBytes = new byte[bufferSize];
                bis.read( readBytes  );
                position = position + readBytes.length;
                return readBytes;
            }
        }
        
        public void close()  {
            try { this.bis.close(); }catch(Exception ign){;}
        }
    }
    
    //file implementations
    public static class FileOutputHandler implements OutputHandler {
        private FileOutputStream fos;
        public FileOutputHandler(File file) throws Exception {
            this(file, true);
        }
        
        public FileOutputHandler(File file, boolean append) throws Exception {
            this.fos = new FileOutputStream(file, append);
        }
        
        public void write(byte[] bytes) throws Exception {
            this.fos.write( bytes );
        }
        
        public void close() {
            try { fos.flush(); } catch(Exception ign){;}
            try { fos.close(); } catch(Exception ign){;}
        }
    }
    
}
