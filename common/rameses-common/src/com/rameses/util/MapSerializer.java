/*
 * MapSerializer.java
 *
 * @author jaycverg
 */
package com.rameses.util;

import java.io.OutputStream;
import java.util.Map;

public abstract class MapSerializer {
    
    private MapSerializerHandler handler = new DefaultHandler();
    private static MapSerializer instance;
    
    public static MapSerializer getInstance() {
        if ( instance == null ) {
            instance = new BasicMapSerializer();
        }
        return instance;
    }
    
    public abstract String toString(Map data);
    public abstract void write(Map data, OutputStream os);
    
    public MapSerializerHandler getHandler() {
        return handler;
    }
    
    public void setHandler(MapSerializerHandler handler) {
        this.handler = handler;
    }
    
    
    protected void signalStartDocument() {
        handler.startDocument();
    }
    
    protected void signalStartElement(String key, Object value, int rowPos) {
        handler.startElement(key, value, rowPos);
    }
    
    protected void signalStartProperty(String key, Object value) {
        handler.startProperty(key, value);
    }
    
    protected void signalEndProperty(String key) {
        handler.endProperty(key);
    }
    
    protected void signalEndElement(String key) {
        handler.endElement(key);
    }
    
    protected void signalEndDocument() {
        handler.endDocument();
    }
    
    /**
     * @description
     *   default implementation of MapSerializerHandler interface
     */
    public static class DefaultHandler implements MapSerializerHandler {
        
        public String getIgnorPrefix() {
            return "_";
        }
        public void startDocument() {}
        public void startElement(String key, Object value, int rowPos) {}
        public void startProperty(String key, Object value) {}
        public void endProperty(String key) {}
        public void endElement(String key) {}
        public void endDocument() {}
        
    }
    
}
