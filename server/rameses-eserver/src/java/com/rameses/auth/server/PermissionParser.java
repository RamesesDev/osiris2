/*
 * PermissionParser.java
 *
 * Created on April 9, 2010, 9:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.auth.server;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class PermissionParser extends DefaultHandler {
    
    private List permissions;
    private PermissionResource resource;
    
    public PermissionParser(PermissionResource pr) {
        this.resource = pr;
    }
    
    public List parse(String name) {
        InputStream is = null;
        try {
            permissions = new ArrayList();
            if(!name.endsWith(".xml")) name = name + ".xml";
            is = resource.getIputStream(name);
            if(is!=null) {
                SAXParser p = SAXParserFactory.newInstance().newSAXParser();
                p.parse(is, this);
            }
            return permissions;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } finally {
            try { is.close(); } catch(Exception ex){;}
        }
        
    }
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("permission")) {
            String name = attributes.getValue("name");
            boolean allow = true;
            try {
                String a = attributes.getValue("allowed");
                if(a!=null && a.trim().length()>0) {
                    allow = Boolean.parseBoolean(a);
                }
            } catch(Exception ign){;}
            String title = attributes.getValue("title");
            if(title==null) title = name;
            permissions.add(new Permission(name,title,allow));
        } 
        else if(qName.equals("import")) {
            PermissionParser rp = new PermissionParser(resource);
            String name = attributes.getValue("name");
            List list = rp.parse( name );
            String exclude = attributes.getValue("exclude");
            String allow = attributes.getValue("allow");
            String disallow = attributes.getValue("disallow");
            PermissionUtil.filter(list, exclude,allow,disallow);
            permissions.addAll(list);
        }
        
    }
    
    
    
}
