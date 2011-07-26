/*
 * ObjectDeserializer.java
 *
 * Created on July 22, 2011, 2:04 PM
 * @author jaycverg
 */

package com.rameses.util;

import java.io.Reader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ObjectDeserializer 
{
    
    private static ObjectDeserializer instance;
    
    public static ObjectDeserializer getInstance() {
        if( instance == null ) instance = new ObjectDeserializer();
        return instance;
    }
    
    
    public ObjectDeserializer() {
    }
    
    public Object read(String text) {
        return doRead(new StringReader(text));
    }
    
    public Object read(Reader reader) {
        return doRead(reader);
    }
    
    private Object doRead(Reader reader) {
        GroovyMapTextParser parser = new GroovyMapTextParser();
        return parser.parse(reader);
    }
    
    private class GroovyMapTextParser {
        
        public Object parse(Reader reader) {
            try 
            {
                StringBuffer buffer = new StringBuffer();
                Stack<ObjectNode> nodes = new Stack();
                ObjectNode root = null;
                
                StreamTokenizer tokenizer = new StreamTokenizer(reader);
                tokenizer.ordinaryChar('.');
                tokenizer.ordinaryChars('0', '9');

                while( tokenizer.nextToken() != StreamTokenizer.TT_EOF ) {
                    int type = tokenizer.ttype;
                    if( type == '[' ) {
                        nodes.push(new ObjectNode());
                        if( root == null ) {
                            root = nodes.peek();
                        }
                    }
                    else if( type == ':' ) {
                        if( nodes.isEmpty() ) 
                            throw new Exception("character ':' is not expected at line " + tokenizer.lineno() + ".");
                        
                        nodes.peek().currentFldKey = buffer.toString();
                        buffer.delete(0, buffer.length());
                    }
                    else if( type == ',' ) {
                        if( nodes.isEmpty() )
                            throw new Exception("character ',' is not expected at line " + tokenizer.lineno() + ".");
                            
                        processValue(nodes.peek(), buffer);
                    }
                    else if( type == ']' ) {
                        if( nodes.isEmpty() ) 
                            throw new Exception("character ']' is not expected at line " + tokenizer.lineno() + ".");
                        
                        ObjectNode n = nodes.pop();
                        processValue(n, buffer);
                        if( !nodes.isEmpty() ) {
                            processObjectValue(nodes.peek(), n.getObject());
                        }
                    }
                    else if( type == StreamTokenizer.TT_WORD ) {
                        buffer.append( tokenizer.sval );
                    }
                    else if( type == '"' || type == '\'' ) {
                        buffer.append( "\"" + tokenizer.sval + "\"");
                    }
                    else if( type == StreamTokenizer.TT_NUMBER ) {
                        buffer.append( tokenizer.nval );
                    }
                    else {
                        buffer.append( (char)type );
                    }
                }
                
                return root.getObject();
                
            } 
            catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }
        
        private void processValue(ObjectNode n, StringBuffer buffer) {
            if( n.currentFldKey != null ) {
                if( !n.isInitialized() ) n.init( new LinkedHashMap() );
                Object value = getValue(buffer.toString());
                Object key = correctKey(n.currentFldKey);
                if( !(key instanceof EmptyValue && value instanceof EmptyValue) ) {
                    if( value instanceof EmptyValue ) value = null;
                    if( key instanceof EmptyValue ) key = null;
                    n.addEntry(key, value);
                }
                n.currentFldKey = null;
            }
            else {
                if( !n.isInitialized() ) n.init( new ArrayList() );
                Object value = getValue(buffer.toString());
                if( !(value instanceof EmptyValue) )
                    n.addEntry(value);
            }
            buffer.delete(0, buffer.length());
        }
        
        private void processObjectValue(ObjectNode n, Object value) {
            if( n.currentFldKey != null ) {
                if( !n.isInitialized() ) n.init( new LinkedHashMap() );
                Object key = correctKey(n.currentFldKey);
                if( key instanceof EmptyValue ) key = null;
                n.addEntry(key, value);
                n.currentFldKey = null;
            }
            else {
                if( !n.isInitialized() ) n.init( new ArrayList() );
                n.addEntry(value);
            }
        }
        
        private Object correctKey(String text) {
            if( text.startsWith("\"") && text.endsWith("\"") )
                return text.substring(1, text.length()-1);
            if( text.trim().length() == 0 )
                return new EmptyValue();
                
            return text;
        }
        
        private Object getValue(String text) {
            if( text.startsWith("\"") && text.endsWith("\""))
                return text.substring(1, text.length()-1);
            if( text.matches("\\d+") )
                return new Integer(text);
            if( text.matches("\\d+\\.\\d+") )
                return new BigDecimal(text);
            if( "null".equals(text) )
                return null;
            
            return new EmptyValue();
        }

    }
    
    
    private class ObjectNode 
    {
        
        public String currentFldKey;
        
        private boolean initialized;
        private Object obj;
        
        ObjectNode() {}
        
        public void addEntry(Object key, Object value) {
            if( obj instanceof Map ) {
                ((Map)obj).put(key, value);
            }
            else {
                throw new RuntimeException("Object is not a map.");
            }
        }
        
        public void addEntry(Object value) {
            if( obj instanceof List ) {
                ((List)obj).add(value);
            }
            else {
                throw new RuntimeException("Object is not a list.");
            }
        }

        public Object getObject() {
            return obj;
        }
        
        public void init(Object value) {
            this.obj = value;
            initialized = true;
        }

        public boolean isInitialized() {
            return initialized;
        }

    }
    
    //a flag class
    private class EmptyValue {}

}