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

                int type = 0;
                while( tokenizer.nextToken() != StreamTokenizer.TT_EOF ) {
                    type = tokenizer.ttype;
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
            if( text.startsWith("\"") && text.endsWith("\""))   //string
                return text.substring(1, text.length()-1);
            if( text.matches("\\d+") )                          //integer
                try {
                    return new Integer(text);
                }
                //this might throw an error if value exceeds Integer.MAX_VALUE
                catch(Exception e) {
                    return new Long(text);
                }
            if( text.matches("\\d+\\.\\d+.*") )                 //decimal
                return new BigDecimal(text);
            if( text.matches("true|false") )                    //boolean
                return new Boolean(text);
            if( text.endsWith(".class") ) {                     //class type
                text = text.replaceAll("\\.class$", "");
                
                if( "int".equals(text) )     return int.class;
                if( "long".equals(text) )    return long.class;
                if( "short".equals(text) )   return  short.class;
                if( "float".equals(text) )   return float.class;
                if( "double".equals(text) )  return double.class;
                if( "boolean".equals(text) ) return boolean.class;
                
                try {
                    return getClass().getClassLoader().loadClass(text);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                    return null;
                }
            }
            if( "null".equals(text) )                           //null
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
