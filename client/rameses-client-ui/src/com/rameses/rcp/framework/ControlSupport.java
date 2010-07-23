package com.rameses.rcp.framework;

import com.rameses.util.PropertyResolver;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;
import javax.swing.ImageIcon;



public final class ControlSupport {
    
    
//    public static StyleRule[] getApplicableStyles( Object bean, StyleRule[] styleRules) {
//        List<StyleRule> list = new ArrayList<StyleRule>();
//        if( styleRules != null ) {
//            for(StyleRule r: styleRules) {
//
//                String rule = r.getExpression();
//                boolean test = true;
//                if( rule!=null){
//                    Object o = ClientContext.getCurrentContext().getExpressionResolver().evaluate(bean, rule);
//                    if( o instanceof Boolean ) {
//                        test = ((Boolean)o).booleanValue();
//                    }
//                }
//                if( test ) list.add(r);
//            }
//        }
//        return (StyleRule[])list.toArray(new StyleRule[]{});
//    }
    
//    public static void applyControlStyle( StyleRule[] styleRules, Component component  ) {
//        for(StyleRule r: styleRules) {
//            String pattern = r.getPattern();
//            String name = component.getName();
//            if( name != null && name.matches(pattern) ) {
//                setStyles(r.getProperties(), component );
//            }
//        }
//    }
    
    public static void setStyles(Map props, Component component) {
        PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
        for(Object o : props.entrySet()) {
            try {
                Map.Entry me = (Map.Entry)o;
                resolver.setProperty(component, me.getKey()+"", me.getValue() );
                
            } catch(Exception ign) {;}
        }
    }
    
    public static Object init(Object bean, Map params, String action ) {
        setProperties(bean, params);
        return invoke( bean, action, null );
    }
    
    public static void setProperties(Object bean, Map params ) {
        if( params != null ) {
            ClientContext ctx = ClientContext.getCurrentContext();
            PropertyResolver resolver = ctx.getPropertyResolver();
            for( Object oo : params.entrySet()) {
                Map.Entry me = (Map.Entry)oo;
                resolver.setProperty(bean, me.getKey()+"", me.getValue() );
            }
        }
    }
    
    public static Object invoke(Object bean, String action, Object[] params  ) {
        ClientContext ctx = ClientContext.getCurrentContext();
        //fire actions
        if( action !=null && action.trim().length()>0) {
            try {
                return ctx.getMethodResolver().invoke(bean,action,null,null);
            } catch(Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        return null;
    }
    
    public static byte[] getByteFromResource( String name ) {
        if(name==null || name.trim().length()==0)
            return null;
        ByteArrayOutputStream bos = null;
        InputStream is = ClientContext.getCurrentContext().getResourceProvider().getResource(name);
        if( is != null ) {
            try {
                bos =  new ByteArrayOutputStream();
                int i = 0;
                while((i=is.read())!=-1) {
                    bos.write(i);
                }
                return bos.toByteArray();
            } catch(Exception ex) {
                return null;
            } finally {
                try { bos.close(); } catch(Exception ign){;}
                try { is.close(); } catch(Exception ign){;}
            }
        } else {
            return  null;
        }
    }
    
    public static ImageIcon getImageIcon(String name) {
        byte[] b = ControlSupport.getByteFromResource(name);
        if(b!=null) {
            return new ImageIcon( b );
        } else
            return null;
    }
    
}
