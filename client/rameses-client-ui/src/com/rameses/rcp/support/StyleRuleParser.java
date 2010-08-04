/*
 * StyleRuleParser.java
 *
 * Created on June 24, 2010, 5:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.rcp.support;

import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.framework.ClientContext;
import java.awt.Color;
import java.io.InputStream;
import java.io.StreamTokenizer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

public class StyleRuleParser {
    
    private boolean startStyle;
    private boolean startProperties;
    private StringBuffer buffer = new StringBuffer();
    
    private String propertyName;
    private String stylePattern;
    
    //<editor-fold defaultstate="collapsed" desc="  parse method  ">
    public void parse( InputStream is, ParseHandler handler ) throws Exception {
        StreamTokenizer tokenizer = new StreamTokenizer(is);
        tokenizer.ordinaryChar('.');
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChars('0', '9');
        while(tokenizer.nextToken() != tokenizer.TT_EOF ) {
            if(tokenizer.ttype == '"') {
                startStyle = true;
                stylePattern = tokenizer.sval;
                handler.startStyle();
                buffer.delete(0, buffer.length() );
                
            } else if(tokenizer.ttype == '[') {
                if ( stylePattern == null ) {
                    stylePattern = buffer.toString();
                    buffer.delete(0, buffer.length() );
                }
            } else if(tokenizer.ttype == ']') {
                handler.setStyle( stylePattern, buffer.toString() );
                buffer.delete(0, buffer.length() );
                stylePattern = null;
                
            } else if( tokenizer.ttype == '{' ) {
                handler.startProperties();
                startProperties = true;
                
            } else if( tokenizer.ttype == '}' ) {
                handler.endProperties();
                startProperties = false;
                startStyle = false;
                handler.endStyle();
                
            } else if( tokenizer.ttype == ':' ) {
                propertyName = buffer.toString();
                buffer.delete(0, buffer.length() );
                
            } else if( tokenizer.ttype == ';' ) {
                handler.addProperty( propertyName, buffer.toString() );
                buffer.delete(0, buffer.length() );
                propertyName = null;
                
            } else {
                if(!startStyle) {
                    startStyle = true;
                    handler.startStyle();
                    buffer.delete(0, buffer.length() );
                }
                if( tokenizer.ttype==39) {
                    buffer.append("'"+tokenizer.sval+"'");
                    
                } else if(tokenizer.ttype==tokenizer.TT_WORD) {
                    buffer.append(tokenizer.sval);
                    
                } else if(tokenizer.ttype==tokenizer.TT_NUMBER) {
                    buffer.append(tokenizer.nval);
                    
                } else {
                    buffer.append((char)tokenizer.ttype);
                }
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  StyleRuleParser interfaces  ">
    public static interface ParseHandler {
        void startStyle();
        void setStyle(String pattern, String expr);
        void startProperties();
        void addProperty(String name, String property);
        void endProperties();
        void endStyle();
    }
    
    public static interface ValueResolver {
        
        Object resolve(String name, String value);
        
    }
    
    public static interface PropertyResolver {
        
        String resolve(String propertyName);
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  DefaultParseHandler (class)  ">
    public static class DefaultParseHandler implements StyleRuleParser.ParseHandler {
        
        private StyleRule currentStyle;
        private List<StyleRule> list = new ArrayList();
        private List<ValueResolver> vResolvers = new ArrayList();
        private List<PropertyResolver> pResolvers = new ArrayList();
        
        public DefaultParseHandler() {
            addValueResolver( new ColorResolver() );
            addValueResolver( new DataTypeResolver() );
            addPropertyResolver( new DefaultPropertyResolver() );
        }
        
        public void startStyle() {;}
        public void startProperties() {;}
        public void endProperties() {;}
        public void endStyle() {;}
        
        public void setStyle(String pattern, String expr) {
            if ( !expr.startsWith("#{")) {
                expr = "#{" + expr + "}";
            }
            
            currentStyle = new StyleRule(pattern, expr);
            list.add(currentStyle);
        }
        
        public void addProperty(String name, String property) {
            String xname = resolveProperty(name);
            Object value = resolveValue(name, property);
            currentStyle.add(xname, value);
        }
        
        private Object resolveValue( String name, String value )  {
            if(value==null) {
                return null;
            }
            if(value.toLowerCase().equals("null")) {
                return null;
            }
            
            Object o = value;
            for(ValueResolver vr : vResolvers) {
                Object x = vr.resolve(name, value);
                if( x!=null ) return x;
            }
            return o;
        }
        
        private String resolveProperty( String name ) {
            if( name == null ) return null;
            String propName = name;
            for( PropertyResolver res: pResolvers ) {
                String s = res.resolve( name );
                if ( s != null ) return s;
            }
            return propName;
        }
        
        
        public List<StyleRule> getList() {
            return list;
        }
        
        public void addValueResolver(ValueResolver res) {
            if ( !vResolvers.contains(res) ) {
                vResolvers.add( res );
            }
        }
        
        public void addPropertyResolver(PropertyResolver res) {
            if ( !pResolvers.contains( res ) ) {
                pResolvers.add( res );
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Default Value/Property Resolver classes  ">
    public static class ColorResolver implements ValueResolver {
        
        public Object resolve(String name, String value) {
            //a hex color value is expected
            if(value.startsWith("#")) {
                return Color.decode(value);
            }
            
            //an rgb color value is expected
            //example: rgb(200, 219, 227)
            else if ( value.startsWith("rgb") ) {
                String exp = "rgb\\((\\d+),(\\d+),(\\d+)\\)";
                Matcher m = Pattern.compile(exp).matcher(value.replace(" ", ""));
                if ( m.matches() ) {
                    int r = Integer.parseInt(m.group(1));
                    int g = Integer.parseInt(m.group(2));
                    int b = Integer.parseInt(m.group(3));
                    return new Color(r,g,b);
                }
            }
            
            return null;
        }
        
    }
    
    public static class DataTypeResolver implements ValueResolver {
        
        public Object resolve(String name, String value) {
            if ( "true".equals(value) ) {
                return true;
            } else if ( "false".equals(value) ) {
                return false;
            } else if ( "icon".equals(name) ) {
                ClassLoader loader = ClientContext.getCurrentContext().getClassLoader();
                URL u = loader.getResource(value);
                return new ImageIcon(u);
            }
            
            return null;
        }
        
    }
    
    public static class DefaultPropertyResolver implements PropertyResolver {
        
        public String resolve(String name) {
            if ( "background-color".equals(name) ) {
                return "background";
            } else if ( "forecolor".equals(name) ) {
                return "foreground";
            }
            
            return null;
        }
        
    }
    //</editor-fold>
    
}
