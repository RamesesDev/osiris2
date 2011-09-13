/*
 * GroovyObjectDeserializer.java
 *
 * Created on September 14, 2010, 2:04 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.util.ValueUtil;
import groovy.lang.GroovyShell;
import java.io.InputStream;


/**
 * This class has been deprecated due to class creation problem
 * when using GroovyShell
 *
 * Use the ObjectDeserializer class of com.rameses.util package instead
 * of this class
 */
@Deprecated
public class GroovyObjectDeserializer {
    
    private static GroovyObjectDeserializer instance;
    
    public GroovyObjectDeserializer() {}
    
    public static GroovyObjectDeserializer getInstance() {
        if ( instance == null ) {
            instance = new GroovyObjectDeserializer();
        }
        return instance;
    }
    
    public Object deserialize(String data) {
        if ( ValueUtil.isEmpty(data) ) return null;
        GroovyShell gs = null;
        try {
            gs = new GroovyShell();
            return gs.evaluate(data);
        } catch(Exception e) {
            throw new IllegalStateException("Data deserialization error", e);
        } finally {
            gs = null;
        }
    }
    
    public Object deserialize(InputStream data) {
        if ( ValueUtil.isEmpty(data) ) return null;
        GroovyShell gs = null;
        try {
            gs = new GroovyShell();
            return gs.evaluate(data);
        } catch(Exception e) {
            throw new IllegalStateException("Data deserialization error", e);
        } finally {
            gs = null;
        }
    }

}
