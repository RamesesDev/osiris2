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
        return new HTMLBuilder(data, null, style).toString();
    }
    
    
    private Map data;
    private Map meta;
    private String style;
    
    private String html;
    
    
    public HTMLBuilder() {}
    
    public HTMLBuilder(Map data) {
        this(data, null);
    }
    
    public HTMLBuilder(Map data, Map meta) {
        this(data, meta, null);
    }
    
    public HTMLBuilder(Map data, Map meta, String style) {
        this.data = data;
        this.meta = meta;
        this.style = style;
    }
    
    public void rebuild() {
        html = null;
        toString();
    }
    
    public String toString() {
        if( html == null ) {
            String ss = style != null? style : defaultStyle;
            html = "<html>" +
                    "<style>" + ss + "</style>" +
                    "<body>" +
                    doToString(data, meta) +
                    "</body></html>";
        }
        
        return html;
    }
    
    private String doToString(Map data, Map metaData) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<table>");
        for(Map.Entry me : (Set<Map.Entry>)data.entrySet()) {
            Map def = metaData == null? null : (Map) metaData.get(me.getKey());
            
            String caption = null;
            if( def != null && def.get("caption") != null )
                caption = def.get("caption")+"";
            else
                caption = me.getKey()+"";
            
            buffer.append("<tr>");
            buffer.append("<td valign='top'><b>" + caption + " :</b></td>");
            
            Object value = me.getValue();
            buffer.append("<td valign='top'>");
            if( value instanceof Map ) {
                buffer.append(doToString((Map)value, def));
            } else {
                buffer.append(value);
            }
            buffer.append("</td>");
            
            buffer.append("</tr>");
        }
        buffer.append("</table>");
        return buffer.toString();
    }
    
    public Map getData() {
        return data;
    }
    
    public void setData(Map data) {
        this.data = data;
    }
    
    public Map getMeta() {
        return meta;
    }
    
    public void setMeta(Map meta) {
        this.meta = meta;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
    
}
