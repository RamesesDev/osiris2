/*
 * ActionSupport.java
 *
 * Created on September 7, 2009, 12:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.framework;

import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.ui.UICommand;
import com.rameses.util.ExpressionResolver;
import com.rameses.util.MethodResolver;
import com.rameses.util.ValueResolver;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author elmo
 */
public final class ControlSupport {
    
    public static Object invoke(  MethodResolver me, ValueResolver ve, Object bean, String action, Object[] args ) throws Exception {
        //find first the bean if the action is nested.
        String xaction = action;
        
        if( xaction.indexOf(".")>0) {
            xaction = action.substring(action.lastIndexOf(".")+1);
            String p = action.substring(0, action.lastIndexOf("."));
            bean = ve.getValue(bean, p);
        }
        if( args !=null ) {
            return me.invoke(bean, xaction, null, args);
        } else {
            return me.invoke(bean, xaction, new Class[]{}, new Object[]{});
        }
    }
    
    public static Object invoke(  Binding b, Object bean, String action, Object[] args ) throws Exception {
        return invoke( ClientContext.getCurrentContext().getMethodResolver(), ClientContext.getCurrentContext().getValueResolver(), bean, action, args );
    }
    
    public static Object invoke(  Binding b, String action, Object[] args ) throws Exception {
        return invoke( ClientContext.getCurrentContext().getMethodResolver(), ClientContext.getCurrentContext().getValueResolver(), b.getBean(), action, args );
    }
    
    
    public static void setStyles(Map props, Component component) {
        for(Object o : props.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            ClientContext.getCurrentContext().getValueResolver().setValue( component, me.getKey()+"", me.getValue() );
        }
    }
     
    
    public static StyleRule[] getApplicableStyles( Object bean, StyleRule[] styleRules) {
        List<StyleRule> list = new ArrayList<StyleRule>();
        if( styleRules != null ) {
            for(StyleRule r: styleRules) {
                
                String rule = r.getExpression();
                boolean test = true;
                if( rule!=null){
                    Object o = ClientContext.getCurrentContext().getExpressionResolver().evaluate(bean, rule);
                    if( o instanceof Boolean ) {
                        test = ((Boolean)o).booleanValue();
                    }
                }
                if( test ) list.add(r);
            }
        }
        return (StyleRule[])list.toArray(new StyleRule[]{});
    }
    
    public static void applyControlStyle( StyleRule[] styleRules, Component component  ) {
        for(StyleRule r: styleRules) {
            String pattern = r.getPattern();
            String name = component.getName();
            if( name != null && name.matches(pattern) ) {
                setStyles(r.getProperties(), component );
            }
        }
    }
    
    public static Object init(Object bean, Map params, String action ) {
        setProperties(bean, params);
        return invoke( bean, action, null );
    }
    
    
    public static void setProperties(Object bean, Map params ) {
        if( params != null ) {
            ClientContext ctx = ClientContext.getCurrentContext();
            for( Object oo : params.entrySet()) {
                Map.Entry me = (Map.Entry)oo;
                ctx.getValueResolver().setValue(bean, me.getKey()+"", me.getValue() );
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
    
 
    /**
     * This function searches for the nearest ICommand
     */
    public static final UICommand findICommand(Component c) {
        while(c!=null) {
            if(c instanceof UICommand) return (UICommand)c;
            c = c.getParent();
        }
        return null;
    }
    
    public static final boolean isValidExpression(Object bean, String expr) {
        if(expr ==null) return true;
        ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
        try {
            return Boolean.valueOf( er.evaluate(bean, expr) + "" );
        }
        catch(Exception ign) {
            return true;
        }
    }
}
