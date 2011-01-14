/*
 * ObjectSerializer.java
 *
 * Created on August 27, 2010, 5:37 PM
 * @author jaycverg
 */

package com.rameses.util;

import com.rameses.util.ObjectScanner.ObjectScannerHandler;
import java.io.StringWriter;
import java.io.Writer;

/**
 * 
 * 
 * @description basic implementation of abstract class ObjectSerializer
 */
public class ObjectSerializer {
    
    private ObjectScannerHandler scanHandler = new ScanHandler();
    private Writer writer;
    
    private static ObjectSerializer instance;
    
    public static ObjectSerializer getInstance() {
        if ( instance == null ) instance = new ObjectSerializer();
        return instance;
    }
    
    
    public ObjectSerializer() {}
    
    public String toString(Object data) {
        writer = new StringWriter();
        stringifyMap(data);
        return writer.toString();
    }
    
    public void write(Object data, Writer writer) {
        try {
            this.writer = writer;
            stringifyMap(data);
            writer.flush();
            
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try { writer.close(); }catch(Exception e){}
        }
    }
    
    private void stringifyMap(Object data) {
        ObjectScanner scanner = new ObjectScanner(scanHandler);
        scanner.scan(data);
    }
    
    
    private String correctKeyName(String n) {
        if(n.contains(".") || n.contains("[") || n.contains("]")) {
            return "\"" + n + "\"";
        }
        else {
            return n;
        }
    }
    
    private class ScanHandler implements ObjectScannerHandler {
        
        public void startDocument() {}
        
        public void startElement(String name, int pos) {
            try {
                if(pos>0) writer.write(",");
                if( name!=null ) writer.write(correctKeyName(name)+":");
                writer.write("[");
                
            } catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
        public void property(String name, Object value, int pos) {
            try {
                if(pos>0) writer.write(",");
                if(name!=null) writer.write(correctKeyName(name)+":");
                
                if ( value == null ) {
                    writer.write("null");
                } else {
                    writer.write( ValueUtil.getValueAsString(value.getClass(), value) );
                }
                
            } catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
        public void endElement(String name) {
            try {
                writer.write("]");
            } catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
        public void endDocument() {}
        
        
    }
    
}
