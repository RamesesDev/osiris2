/*
 * PermissionParser.java
 *
 * Created on August 4, 2010, 3:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author elmo
 */
public class PermissionParser extends DefaultHandler {
    
    private Map<String,PermissionSet> map;
    private StringBuffer buff;
    private PermissionSet currentPermissionSet;
    private String currentModuleName;
    private SAXParser parser;
    private String category;
    private String source;
    
    private List parseErrors = new ArrayList();
    
    public PermissionParser(Map map) {
        try {
            parser = SAXParserFactory.newInstance().newSAXParser();
            this.map = map;
        }
        catch(Exception ex){
            throw new IllegalStateException(ex);
        }
    }
    
    public void load(InputStream is, String source) {
        try {
            parseErrors.clear();
            this.source = source;
            parser.parse( is, this );
        }
        catch(Exception ex) {
            System.out.println("error loading permission ->" + ex.getMessage());
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("permission-set") ) {
            String name = attributes.getValue("name");
            currentModuleName = attributes.getValue("module");
            category = attributes.getValue("category");
            if(name == null ) {
                buff = null;
                parseErrors.add( "Name attribute must be provided for permission-set. source:[" + source + "]" );
            }
            else {
                //check permission set in map 
                currentPermissionSet = map.get(name);
                if( currentPermissionSet == null ) {
                    currentPermissionSet = new PermissionSet(name);
                    map.put(name, currentPermissionSet);
                }
                buff = new StringBuffer();
            }
        }
        else {
            buff = null;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        if(buff!=null) buff.append( ch, start, length );
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("permission-set") && buff!=null) {
            try {
                String perms = buff.toString();
                StringReader sr = new StringReader( perms );
                BufferedReader br = new BufferedReader(sr);
                String line = null;
                String moduleName = currentModuleName;
                if(moduleName==null || moduleName.trim().length()==0 ) 
                    moduleName = "";
                else
                    moduleName = moduleName.trim() + ":";
                
                while( (line=br.readLine())!=null )  {
                    if(line.trim().length()==0) continue;
                    if( line.trim().startsWith("#")) continue;
                    String arr[] = line.trim().split("=");
                    String name = arr[0];
                    String title = arr[1];
                    
                    String action = moduleName+name;
                    
                    //action must match the correct prescribed pattern.
                    if(!action.matches(".*:.*\\..*")) {
                        String err = "Wrong permission pattern: " + action + ". source:["+source+"]";
                        parseErrors.add( err );
                        continue;
                    }
                    
                    Permission p = new Permission( moduleName+name, title );
                    if(category!=null && category.trim().length()>0) p.setCategory(category);
                    currentPermissionSet.addPermission( p );
                }
            }
            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }    
    }

    public List getParseErrors() {
        return parseErrors;
    }

    
    
}
