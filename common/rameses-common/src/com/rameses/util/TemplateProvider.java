package com.rameses.util;

import com.sun.jmx.remote.util.Service;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

public abstract class TemplateProvider implements Serializable {
    
    public abstract String[] getExtensions();
    public abstract Object getResult( String templateName, Object data);
    public abstract Object getResult( String templateName, Object data, TemplateSource source);
    public abstract void transform( String templateName, Object data, OutputStream out);
    public abstract void transform( String templateName, Object data, OutputStream out, TemplateSource source);
    
    //when null is passed, it should clear all.
    public abstract void clear(String name);
    
    private static TemplateProvider instance;
    
    public static void setInstance(TemplateProvider  tp ) {
        instance = tp;
    }
    
    public static TemplateProvider getInstance() {
        if(instance==null) {
            instance = new DefaultTemplateProvider();
        }
        return instance;
    }
    
    
    public static class DefaultTemplateProvider extends TemplateProvider {
        
        private Map<String,TemplateProvider> providers;
        
        public DefaultTemplateProvider() {
            //automatically include xslt cause this is quite common.
            providers = new Hashtable();
            Iterator iter = Service.providers(TemplateProvider.class, getClass().getClassLoader());
            while(iter.hasNext()) {
                TemplateProvider tp = (TemplateProvider)iter.next();
                for(String s: tp.getExtensions()) {
                    providers.put( s, tp );
                }
            }
        }
        
        private TemplateProvider getProvider(String ext) {
            if(!providers.containsKey(ext))
                throw new RuntimeException("There is no template handler found for " + ext);
            return providers.get(ext);
        }
        
        public String[] getExtensions() {
            return null;
        }
        
        public Object getResult(String name, Object data) {
            return getResult( name, data, null );
        }
        
        public Object getResult(String name, Object data, TemplateSource source) {
            if( source == null ) {
                source = new ClassLoaderSourceProvider();
            }
            String ext = name.substring( name.lastIndexOf(".")+1 );
            TemplateProvider t = getProvider(ext);
            return t.getResult( name, data, source );
        }
        
        public void transform(String name, Object data, OutputStream out) {
            transform( name, data, out, null );
        }

        public void transform(String name, Object data, OutputStream out, TemplateSource source) {
            String ext = name.substring( name.lastIndexOf(".")+1 );
            TemplateProvider t = getProvider(ext);
            t.transform( name, data, out, source );
        }
        
        public void clear(String name) {
            
            String ext = name.substring( name.lastIndexOf(".")+1 );
            TemplateProvider t = getProvider(ext);
            t.clear( name );
        }
    }
    
    public static class ClassLoaderSourceProvider implements TemplateSource {
        public InputStream getSource(String name) {
            InputStream is = null;
            try {
                if( name.indexOf("://") < 0 ) {
                    is = getClass().getClassLoader().getResourceAsStream(name);
                } else {
                    is = (new URL( name )).openStream();
                }
                return is;
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
}
