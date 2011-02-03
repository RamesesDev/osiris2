package com.rameses.util;
import java.util.Map;
import java.util.Set;
/*
 * HTMLBuilder.java
 *
 * Created on January 29, 2011, 9:34 AM
 */

/**
 *
 * @author jaycverg
 */
public class HTMLBuilder {
    
    private static String defaultStyle = "body { font-family: Arial; font-size: 11pt; }";
    
    
    public static String toString(Map data) {
        return toString(data, null);
    }
    
    public static String toString(Map data, String style) {
        String ss = (style != null)? style : defaultStyle;
        
        return "<html>" + 
                "<style>" + ss + "</style>" +
                "<body>" +
                doToString(data) +
                "</body></html>";
    }
    
    private static String doToString(Map data) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<table>");
        for(Map.Entry me : (Set<Map.Entry>)data.entrySet()) {
            buffer.append("<tr>");
            buffer.append("<td valign='top'><b>" + me.getKey() + " :</b></td>");
            
            Object value = me.getValue();
            buffer.append("<td valign='top'>");
            if( value instanceof Map ) {
                buffer.append(doToString((Map)value));
            }
            else {
                buffer.append(value);
            }
            buffer.append("</td>");
                    
            buffer.append("</tr>");
        }
        buffer.append("</table>");
        return buffer.toString();
    }

    public static boolean toHhtml(Map data) {
        return false;
    }
    
}
