/*
 * MapSerializerHandler.java
 *
 * Created on August 27, 2010, 4:08 PM
 * @author jaycverg
 */

package com.rameses.util;


public interface MapSerializerHandler {
    
    public String getIgnorPrefix();
    void startDocument();
    void startElement(String key, Object value, int rowPos);
    void startProperty(String key, Object value);
    void endProperty(String key);
    void endElement(String key);
    void endDocument();
    
}
