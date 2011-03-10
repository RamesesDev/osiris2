/*
 * ObjectScanner.java
 *
 * Created on August 28, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class ObjectScanner {
    
    private String excludeMatch = "_.*[^_]$";
    
    private ObjectScannerHandler handler;
    
    public ObjectScanner() {;}
    
    public ObjectScanner(ObjectScannerHandler h) {
        this.handler = h;
    }
    
    public void scan( Object data ) {
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
            if( map.isEmpty() ) {
                handler.emptyElement(name, map, pos);
            } else {
                handler.startElement(name,pos);
                int i = 0;
                for(Object m : map.entrySet()) {
                    Map.Entry me = (Map.Entry)m;
                    String key = me.getKey()+"";
                    if(excludeMatch==null || !key.matches(excludeMatch)) {
                        Object value = me.getValue();
                        scanObject(key, value,i++);
                    }
                }
                handler.endElement(name);
            }
        } else if( (o instanceof List) || o.getClass().isArray() ) {
            List list = null;
            if ( o.getClass().isArray() ) {
                list = Arrays.asList((Object[])o);
            } else {
                list = (List) o;
            }
            if( list.isEmpty() ) {
                handler.emptyElement(name, list, pos);
            } else {
                handler.startElement(name, pos);
                int i = 0;
                for(Object item: list) {
                    scanObject( null, item, i++ );
                }
                handler.endElement(name);
            }
        } else {
            handler.property(name, o, pos);
        }
    }
    
    /**
     *
     *
     * @description scan event handler of ObjectScanner
     */
    public static interface ObjectScannerHandler {
        void startDocument();
        void emptyElement(String name, Object value, int pos);
        void startElement(String name, int pos);
        void property(String name, Object value, int pos);
        void endElement(String name);
        void endDocument();
    }
    
    
    /**
     *
     *
     * @description default implementation of ObjectScannerHandler interface
     */
    public static class DefaultHandler implements ObjectScannerHandler {
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
        
        public void emptyElement(String name, Object value, int pos) {
        }
        
    }
    
    public String getExcludeMatch() {
        return excludeMatch;
    }
    
    public void setExcludeMatch(String excludeMatch) {
        this.excludeMatch = excludeMatch;
    }
    
    
    
}
