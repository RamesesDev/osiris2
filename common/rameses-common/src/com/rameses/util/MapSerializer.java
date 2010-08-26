/*
 * MapSerializer.java
 *
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


public abstract class MapSerializer {
    
    private static MapSerializer instance;
    
    public static MapSerializer getInstance() {
        if ( instance == null ) {
            instance = new BasicMapSerializer();
        }
        return instance;
    }
    
    public abstract String toString(Map data);
    public abstract void write(Map data, OutputStream os);
    
    
    /**
     * @description 
     *    basic implementation of abstract class MapSerializer
     */
    public static class BasicMapSerializer extends MapSerializer {
        
        private static final Pattern ESC_STRING = Pattern.compile("[\\\\$\"\n\t\r]");
        private static final SimpleDateFormat DT_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
        private static final SimpleDateFormat TS_FORMATTER = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        
        
        public String toString(Map data) {
            return stringifyMap(data).toString();
        }
        
        public void write(Map data, OutputStream os) {
            BufferedOutputStream bos = null;
            ByteArrayInputStream bis = null;
            try {
                StringBuffer sb = stringifyMap(data);
                bis = new ByteArrayInputStream(sb.toString().getBytes());
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
        
        private StringBuffer stringifyMap(Map data) {
            StringBuffer sb = new StringBuffer("[");
            boolean first = true;
            for(Map.Entry me: (Set<Map.Entry>)data.entrySet()) {
                if ( !first ) sb.append(",");
                first = false;
                
                Object value = me.getValue();
                Object key = me.getKey();
                
                sb.append(key+":");
                sb.append(processValue(value));
            }
            sb.append("]");
            return sb;
        }
        
        private String processValue(Object value) {
            if (value == null) {
                return "null";
            } 
            else if (value instanceof Map) {
                return stringifyMap((Map)value).toString();
            } 
            else if (value instanceof List || value.getClass().isArray()) {
                return stringifyItems(value).toString();
            } 
            else if (value instanceof String) {
                return escape(value.toString()).toString();
            } 
            else if (isNumeric(value) || value instanceof Boolean || value.getClass() == boolean.class) {
                return value+"";
            }
            else if (value instanceof Timestamp) {
                return "\"" + TS_FORMATTER.format(value) + "\"";
            }
            else if (value instanceof Date) {
                return "\"" + DT_FORMATTER.format(value) + "\"";
            }
            
            return "null";
        }
        
        private StringBuffer stringifyItems(Object value) {
            List items = null;
            if ( value.getClass().isArray() ) {
                items = Arrays.asList((Object[]) value);
            } else {
                items = (List) value;
            }
            
            StringBuffer sb = new StringBuffer("[");
            boolean first = true;
            for(Object o: items) {
                if( !first) sb.append(",");
                first = false;
                
                sb.append(processValue(o));
            }
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
    
}
