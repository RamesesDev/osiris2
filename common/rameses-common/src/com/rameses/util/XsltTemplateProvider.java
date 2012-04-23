package com.rameses.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Map;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


public class XsltTemplateProvider extends TemplateProvider {
    
    private Map<String, Transformer> cache = new Hashtable();
    
    public String[] getExtensions() {
        return new String[]{"xslt", "xsl"};
    }
    
    private Transformer getTransformer(String name, TemplateSource ts) throws Exception {
        if (! cache.containsKey(name)) {
            //get the inputstream
            InputStream is = null;
            try {
                if( ts == null ) {
                    ts = new TemplateProvider.ClassLoaderSourceProvider();
                }
                is = ts.getSource( name );
                TransformerFactory tFactory = TransformerFactory.newInstance();
                StreamSource source = new StreamSource(is);
                Transformer transformer = tFactory.newTransformer(source);
                cache.put( name, transformer );
                
            }  catch (Exception ex) {
                throw ex;
            } finally {
                try { is.close(); } catch (Exception ign) {;}
            }
        }
        if (!cache.containsKey(name))
            throw new Exception("Name " + name + " not found");
        return cache.get(name);
    }
    
    public Object getResult(String templateName, Object data) {
        return getResult( templateName, data, null);
    }
    
    public Object getResult(String templateName, Object data, TemplateSource source) {
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            transform(templateName, data, bos, source );
            StringBuffer sb = new StringBuffer();
            for(byte b : bos.toByteArray() ) {
                sb.append( (char) b );
            }
            return sb.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void transform(String templateName, Object data, OutputStream out) {
        transform(templateName, data, out, null);
    }
    
    public void transform(String templateName, Object data, OutputStream out, TemplateSource ts) {
        try {
            Transformer transformer = getTransformer(templateName, ts);
            if (data==null)
                throw new Exception("Xslt Data must not be null");
            
            StreamSource sourceData = null;
            if (data instanceof InputStream) {
                sourceData = new StreamSource((InputStream) data);
            } else                if (data instanceof String) {
                ByteArrayInputStream bis = new ByteArrayInputStream(data.toString().getBytes());
                sourceData = new StreamSource(bis);
            } else {
                throw new Exception("Xslt data must be an Xml document");
            }
            transformer.transform(sourceData, new StreamResult(out));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void clear(String name) {
        if( name !=null )
            cache.clear();
        else
            cache.remove( name );
    }

    
}