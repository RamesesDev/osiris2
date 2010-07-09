/*
 * EmailParseHandler.java
 *
 * Created on May 13, 2009, 12:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.email.service;

import java.util.HashMap;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author elmo
 */
public class EmailParseHandler extends DefaultHandler {
    
    private Map out = new HashMap();
    private StringBuffer sb = new StringBuffer();
    
    public Map getResult() {
        return out;
    }
    
    public void startElement(String string, String string0, String qName, Attributes attributes) throws SAXException {
        sb.delete(0, sb.length());
    }
    
    public void characters(char[] c, int i, int i0) throws SAXException {
        sb.append(c, i, i0);
    }
    
    public void endElement(String string, String string0, String qName) throws SAXException {
        out.put(qName, sb.toString());
    }
    
}
