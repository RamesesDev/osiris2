/*
 * MapScanner.java
 *
 * Created on August 28, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import com.rameses.schema.BreakException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class MapScanner {
    
    private MapScannerHandler handler;
    
    public MapScanner() {;}
    
    public MapScanner(MapScannerHandler h) {
        this.handler = h;
    }
    
    public void scan( Map data ) {
        if(handler==null) handler = new DefaultHandler();
        handler.startDocument();
        scanObject(null, data, 0);
        handler.endDocument();
    }
    
    public void scanObject( String name, Object o, int pos ) {
        if( o == null ) {
            handler.property(name, o, pos);
        } else if( o instanceof Map ) {
            Map map = (Map)o;
            try {
                handler.startElement(name,pos);
                int i = 0;
                for(Object m : map.entrySet()) {
                    Map.Entry me = (Map.Entry)m;
                    String key = me.getKey()+"";
                    Object value = me.getValue();
                    
                    try {
                        scanObject(key, value, i);
                        i++;
                    } catch(BreakException ign){;}
                }
                handler.endElement(name);
            } 
            catch(BreakException ign) {;}
            catch(Exception e) {
                throw new RuntimeException(e);
            }
            
        } else if( (o instanceof List) || o.getClass().isArray() ) {
            List list = null;
            if ( o.getClass().isArray() ) {
                list = Arrays.asList((Object[])o);
            } else {
                list = (List) o;
            }
            try {
                handler.startElement(name, 0);
                int i = 0;
                for(Object item: list) {
                    try {
                        scanObject( null, item, i );
                        i++;
                    } catch(BreakException ign){;}
                }
                handler.endElement(name);
            } 
            catch(BreakException ign) {;}
            catch(Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            handler.property(name, o, pos);
        }
    }
    
    /**
     * @description
     *   scan event handler of MapScanner
     */
    public static interface MapScannerHandler {
        void startDocument();
        void startElement(String name, int pos);
        void property(String name, Object value, int pos);
        void endElement(String name);
        void endDocument();
    }
    
    
    /**
     * @description
     *   default implementation of MapScannerHandler interface
     */
    public static class DefaultHandler implements MapScannerHandler {
        public void startDocument() {
        }
        
        public void startElement(String name, int pos) {
        }
        
        public void property(String name, Object value, int pos) {
        }
        
        public void endElement(String name) {
        }
        
        public void endDocument() {
        }
        
    }
    
    
}
