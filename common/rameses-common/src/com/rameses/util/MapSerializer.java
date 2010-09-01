/*
 * MapSerializer.java
 *
 * Created on August 27, 2010, 5:37 PM
 * @author jaycverg
 */

package com.rameses.util;

import com.rameses.util.MapScanner.MapScannerHandler;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @description
 *    basic implementation of abstract class MapSerializer
 */
public class MapSerializer {
    
    private MapScannerHandler scanHandler = new ScanHandler();
    private Writer writer;
    
    private static MapSerializer instance;
    
    public static MapSerializer getInstance() {
        if ( instance == null ) instance = new MapSerializer();
        return instance;
    }
    
    
    public MapSerializer() {}
    
    public String toString(Map data) {
        writer = new StringWriter();
        stringifyMap(data);
        return writer.toString();
    }
    
    public void write(Map data, Writer writer) {
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
    
    private void stringifyMap(Map data) {
        MapScanner scanner = new MapScanner(scanHandler);
        scanner.scan(data);
    }
    
    private class ScanHandler implements MapScannerHandler {
        
        public void startDocument() {}
        
        public void startElement(String name, int pos) {
            try {
                if(pos>0) writer.write(",");
                if( name!=null ) writer.write(name+":");
                writer.write("[");
                
            } catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
        public void property(String name, Object value, int pos) {
            try {
                if(pos>0) writer.write(",");
                if(name!=null) writer.write(name+":");
                
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
