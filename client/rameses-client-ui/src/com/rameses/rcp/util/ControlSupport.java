package com.rameses.rcp.util;

import com.rameses.rcp.common.Opener;
import com.rameses.common.PropertyResolver;
import com.rameses.rcp.framework.*;
import com.rameses.rcp.ui.UIControl;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;



public final class ControlSupport {
    
    
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
                try {
                    resolver.setProperty(bean, me.getKey()+"", me.getValue() );
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static Object invoke(Object bean, String action, Object[] params  ) {
        ClientContext ctx = ClientContext.getCurrentContext();
        //fire actions
        if( action !=null && action.trim().length()>0) {
            try {
                return ctx.getMethodResolver().invoke(bean,action,null,params);
            } catch(Exception ex) {
                throw new IllegalStateException(ex);
            }
        }
        return null;
    }
    
    public static void fireNavigation(UIControl source, Object outcome) {
        NavigationHandler nh = ClientContext.getCurrentContext().getNavigationHandler();
        NavigatablePanel navPanel = UIControlUtil.getParentPanel((JComponent)source, null);
        nh.navigate(navPanel, source, outcome);
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
    
    public static Opener initOpener( Opener opener, UIController caller ) {
        if ( caller != null && ValueUtil.isEmpty(opener.getName()) ) {
            opener.setController( caller );
            
        } else if ( opener.getController() == null ) {
            //this checking should not be here
            if ( opener.getName().indexOf(":") < 0 && caller != null ) {
                String name = caller.getName();
                String mod = name.substring(0, name.indexOf(":"));
                opener.setName( mod + ":" + opener.getName() );
            }
            
            ControllerProvider provider = ClientContext.getCurrentContext().getControllerProvider();
            UIController controller = provider.getController(opener.getName());
            controller.setId( opener.getId() );
            controller.setName( opener.getName() );
            controller.setTitle( opener.getCaption() );
            
            if ( caller != null ) {
                Object callee = controller.getCodeBean();
                injectCaller( callee, callee.getClass(), caller.getCodeBean());
            }
            opener.setController( controller );
        }
        
        UIController controller = opener.getController();
        if( opener.getCaption()==null ) {
            opener.setCaption( controller.getName() );
        }
        Object o = controller.init(opener.getParams(), opener.getAction());
        if ( o != null && o instanceof String ) {
            opener.setOutcome( (String)o );
        }
        
        return opener;
    }
    
    public static void injectCaller( Object callee, Class clazz, Object caller ) {
        //if caller is the same as calle do not proceed
        //for cases for subforms having the same controller.
        if( callee!=null && callee.equals(caller)) return;
        
        //inject the caller here..
        for(Field f: clazz.getDeclaredFields()) {
            if( f.isAnnotationPresent(com.rameses.rcp.annotations.Caller.class)) {
                boolean accessible = f.isAccessible();
                f.setAccessible(true);
                try { f.set(callee, caller); } catch(Exception ign){;}
                f.setAccessible(accessible);
                break;
            }
        }
        Class superClass = clazz.getSuperclass();
        if(superClass!=null) {
            injectCaller( callee, superClass, caller );
        }
    }
    
    public static boolean isPermitted(String permission ) {
        //check if not permitted, block this
        if(permission!=null && permission.trim().length()>0) {
            ClientContext ctx = ClientContext.getCurrentContext();
            if( ctx.getSecurityProvider()==null ) {
                return  true;
            }
            return ctx.getSecurityProvider().checkPermission(permission);
        } else {
            return true;
        }
    }
    
}
