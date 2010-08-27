/*
 * BasicMapSerializer.java
 *
 * Created on August 27, 2010, 5:37 PM
 * @author jaycverg
 */

package com.rameses.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @description
 *    basic implementation of abstract class MapSerializer
 */
public class BasicMapSerializer extends MapSerializer {
    
    private static final Pattern ESC_STRING = Pattern.compile("[\\\\$\"\n\t\r]");
    private static final SimpleDateFormat DT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TS_FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    
    
    public String toString(Map data) {
        signalStartDocument();
        String output = stringifyMap(null, data, -1).toString();
        signalEndDocument();
        
        return output;
    }
    
    public void write(Map data, OutputStream os) {
        BufferedOutputStream bos = null;
        ByteArrayInputStream bis = null;
        try {
            String str = toString(data);
            bis = new ByteArrayInputStream(str.getBytes());
            bos = new BufferedOutputStream(os, 10240);
            byte[] buff = new byte[10240];
            int bytesRead = -1;
            while( (bytesRead = bis.read(buff)) != -1 ) {
                bos.write(buff, 0, bytesRead);
            }
            bos.flush();
            
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try { bos.close(); } catch(Exception ign){}
            try { bis.close(); } catch(Exception ign){}
        }
    }
    
    private StringBuffer stringifyMap(String elemName, Map data, int pos) {
        signalStartElement(elemName, data, pos);
        
        StringBuffer sb = new StringBuffer("[");
        boolean first = true;
        for(Map.Entry me: (Set<Map.Entry>)data.entrySet()) {
            if ( !first )
                sb.append(",");
            else
                first = false;
            
            Object value = me.getValue();
            String itemKey = me.getKey()+"";
            processItem(sb, itemKey, value, -1);
        }
        sb.append("]");
        signalEndElement(elemName);
        
        return sb;
    }
    
    private void processItem(StringBuffer sb, String itemKey, Object value, int pos) {
        if (value == null) {
            //do nothing
        } 
        else if ( value instanceof Map ) {
            sb.append(itemKey+":");
            sb.append( stringifyMap(itemKey, (Map)value, pos) );
            
        } 
        else if ( value instanceof List || value.getClass().isArray() ) {
            sb.append( processCollection(itemKey, value, pos) );
        }
        else {
            if ( pos < 0 ) {
                signalStartProperty(itemKey, value);
            }
            else {
                signalStartElement(itemKey, value, pos);
            }
            
            sb.append(itemKey+":");
            sb.append(getValueAsString(value));
            
            if ( pos < 0 ) {
                signalEndProperty(itemKey);
            }
            else {
                signalEndElement(itemKey);
            }            
        }
    }
    
    private String getValueAsString(Object value) {
        if (value instanceof String) {
            return escape(value.toString()).toString();
        } else if (isNumeric(value) || value instanceof Boolean || value.getClass() == boolean.class) {
            return value+"";
        } else if (value instanceof Timestamp) {
            return "\"" + TS_FORMATTER.format(value) + "\"";
        } else if (value instanceof Date) {
            return "\"" + DT_FORMATTER.format(value) + "\"";
        }
        
        return "null";
    }
    
    private StringBuffer processCollection(String itemKey, Object value, int pos) {
        List items = null;
        if ( value.getClass().isArray() ) {
            items = Arrays.asList((Object[]) value);
        } else {
            items = (List) value;
        }
        
        StringBuffer sb = new StringBuffer("[");
        boolean first = true;
        int index = 0;
        
        signalStartElement(itemKey, value, pos);
        for(Object o: items) {
            if( !first) 
                sb.append(",");
            else 
                first = false;
            
            processItem(sb, itemKey, o, index++);
        }
        signalEndElement(itemKey);
        return sb.append("]");
    }
    
    private StringBuffer escape(String str) {
        StringBuffer sb = new StringBuffer();
        Matcher m = ESC_STRING.matcher(str);
        while( m.find() ) {
            char match = m.group(0).charAt(0);
            String rep = null;
            switch(match) {
                case '\n': rep = "\\\\n"; break;
                case '\r': rep = "\\\\r"; break;
                case '\t': rep = "\\\\t"; break;
                case '\\': rep = "\\\\"; break;
                case '"' : rep = "\\\\\""; break;
                case '$' : rep = "\\\\\\$"; break;
            }
            
            m.appendReplacement(sb, rep);
        }
        m.appendTail(sb);
        
        return sb.insert(0, "\"").append("\"");
    }
    
    private boolean isNumeric(Object value) {
        if ( value == null ) return false;
        
        Class type = value.getClass();
        
        return  type == Double.class || type == double.class ||
                type == long.class || type == Long.class ||
                type == int.class || type == Integer.class ||
                type == float.class || type == Float.class ||
                type == BigDecimal.class;
    }
    
}
