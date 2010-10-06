package com.rameses.util;

import com.sun.jmx.remote.util.Service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public abstract class TemplateProvider {
    
    public abstract String[] getExtensions();
    public abstract Object getResult( String templateName, Object data);
    public abstract void transform( String templateName, Object data, OutputStream out);
    
    private static TemplateProvider instance;
    
    protected final InputStream getResourceStream(String name) {
        InputStream is = null;
        try {
            if( name.indexOf("://") < 0 ) {
                is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            } else {
                is = (new URL( name )).openStream();
            }
            return is;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public static TemplateProvider getInstance() {
        if(instance==null) {
            instance = new DefaultTemplateProvider();
        }
        return instance;
    }
    
    public static class DefaultTemplateProvider extends TemplateProvider {
        
        private Map<String,TemplateProvider> providers;
        
        private TemplateProvider getProvider(String ext) {
            if(providers==null) {
                providers = new Hashtable();
                Iterator iter = Service.providers(TemplateProvider.class, Thread.currentThread().getContextClassLoader());
                while(iter.hasNext()) {
                    TemplateProvider tp = (TemplateProvider)iter.next();
                    for(String s: tp.getExtensions()) {
                        providers.put( s, tp );
                    }
                }
                //automatically include xslt cause this is quite common.
            }
            if(!providers.containsKey(ext))
                throw new RuntimeException("There is no template handler found for " + ext);
            return providers.get(ext);
        }
        
        public String[] getExtensions() {
            return null;
        }
        
        public Object getResult(String name, Object data) {
            String ext = name.substring( name.lastIndexOf(".")+1 );
            TemplateProvider t = getProvider(ext);
            return t.getResult( name, data );
        }
        
        public void transform(String name, Object data, OutputStream out) {
            String ext = name.substring( name.lastIndexOf(".")+1 );
            TemplateProvider t = getProvider(ext);
            t.transform( name, data, out );
        }
    }
    
    public static class XsltTemplateProvider extends TemplateProvider {
        
        private Map<String,Transformer> cache = new Hashtable();
        
        public String[] getExtensions() {
            return new String[]{"xslt", "xsl"};
        }
        
        private Transformer getTransformer(String name) throws Exception {
            if( ! cache.containsKey(name)) {
                //get the inputstream
                InputStream is = null;
                try {
                    is = getResourceStream( name );
                    TransformerFactory tFactory = TransformerFactory.newInstance();
                    StreamSource source = new StreamSource(is);
                    Transformer transformer = tFactory.newTransformer(source);
                    cache.put( name, transformer );
                    
                } catch(Exception ex) {
                    throw ex;
                } finally {
                    try { is.close(); } catch(Exception ign ) {;}
                }
            }
            if( !cache.containsKey(name))
                throw new Exception("Name " + name + " not found");
            return cache.get(name);
        }
        
        public Object getResult(String templateName, Object data) {
            ByteArrayOutputStream bos = null;
            try {
                bos = new ByteArrayOutputStream();
                transform(templateName, data, bos );
                StringBuffer sb = new StringBuffer();
                for( byte b : bos.toByteArray() ) {
                    sb.append( (char)b );
                }
                return sb.toString();
            }
            catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        
        public void transform(String templateName, Object data, OutputStream out) {
            try {
                Transformer transformer = getTransformer(templateName);
                if(data==null)
                    throw new Exception("Xslt Data must not be null");

                StreamSource sourceData = null;
                if( data instanceof InputStream ) {
                    sourceData = new StreamSource((InputStream)data);
                }
                else if(data instanceof String) {
                    ByteArrayInputStream bis = new ByteArrayInputStream(data.toString().getBytes());
                    sourceData = new StreamSource(bis);
                }
                else {
                    throw new Exception("Xslt data must be an Xml document");
                }
                transformer.transform(sourceData, new StreamResult(out));
            }
            catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
    }
    
}
