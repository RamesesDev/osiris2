/*
 * XsltTemplateProvider.java
 *
 * Created on July 17, 2010, 11:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.templates;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author elmo
 */
public class XsltTemplateProvider implements TemplateProvider {
    
    /** Creates a new instance of GroovyTemplateProvider */
    public XsltTemplateProvider() {
    }

    public boolean accept(String fileName) {
        if( fileName.endsWith(".xslt") || fileName.endsWith(".xsl")) {
            return true;
        }
        return false;
    }

    public Template createTemplate(InputStream is) throws Exception {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        StreamSource source = new StreamSource(is);
        Transformer transformer = tFactory.newTransformer(source);
        return new XsltTemplate( transformer );
    }

    public String toString() {
        return "Xslt Template Provider [.xslt, .xsl extension]";
    }
    
    
    public class XsltTemplate implements Template {
        private Transformer transformer;
        
        XsltTemplate(Transformer t) {
            transformer = t;
        }
        
        public Object transform(Object data, OutputStream out ) {
            try {
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
                
                if(out!=null) {
                    transformer.transform(sourceData, new StreamResult(out));
                    return null;
                }
                else {
                    StringWriter writer = new StringWriter();
                    StreamResult result = new StreamResult(writer);
                    transformer.transform(sourceData, result );
                    return writer.toString();
                }
                
            }
            catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
    }
    
    
}
