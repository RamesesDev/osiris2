/*
 * FolderParser.java
 *
 * Created on March 29, 2009, 11:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import com.rameses.util.ParserUtil;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Stack;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author elmo
 */
public final class FolderParser {
    
    
    public static final void parseFolder(FolderManager fm, ClassLoader loader) {
        try {
            SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
            FolderParseHandler fp = new FolderParseHandler(fm);
            //load all resources under META-INF/xfolders.xml
            Enumeration e = loader.getResources( "META-INF/xfolders.xml" );
            while(e.hasMoreElements()) {
                URL u = (URL) e.nextElement();
                InputStream is =null;
                try {
                    is = u.openStream();
                    sp.parse( is, fp );
                } catch(Exception fex) {
                    //do nothing
                    fex.printStackTrace();
                } finally {
                    try { is.close(); } catch(Exception ign) {;}
                }
            }
        } catch(Exception ex) {
            
        } finally {
            
        }
    }
    
    public static class FolderParseHandler extends DefaultHandler {
        
        private FolderManager fm;
        private Stack stack = new Stack();
        private List invokers = null;
        
        
        public FolderParseHandler(FolderManager f ) {
            this.fm = f;
        }
        
        public void startElement(String string, String string0, String qName, Attributes attributes) throws SAXException {
            if( qName.equals("folder")) {
                //should merge
                String _name = attributes.getValue("name");
                String _id = attributes.getValue("id");
                String _caption = attributes.getValue("caption");
                
                String id = _id;
                if( id == null ) id = _name;
                if( id == null ) id = _caption;
                
                String caption = _caption;
                if( caption == null) caption = _name;
                if( caption == null ) caption  = _id;
                
                //check parent 
                Folder f = new Folder( id, caption );
                Folder parent = null;
                if( !stack.empty() ) {
                    parent = (Folder)stack.peek();
                }
                
                ParserUtil.loadAttributes( f, f.getProperties(), attributes, fm.getAppContext().getPropertyResolver() ); 
                Folder ff = fm.addFolder(f, parent);
                stack.push( ff );
                
            }
        }
        
        public void endElement(String string, String string0, String qName) throws SAXException {
            if(qName.equals("folder")) {
                Folder f = null;
                if( !stack.empty()) {
                    f = (Folder)stack.pop();
                }
            }
        }
    }
    
    
}
