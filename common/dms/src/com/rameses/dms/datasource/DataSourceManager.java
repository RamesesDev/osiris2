/*
 * DataSourceManager.java
 *
 * Created on December 28, 2009, 5:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms.datasource;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.sql.DataSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author elmo
 */
public class DataSourceManager {
    
    private static Map<String, DataSource> datasources;
    
    public DataSourceManager() {
    }
    
    public static DataSource getDataSource(String name) {
        if(datasources==null) {
            datasources = new Hashtable<String,DataSource>();  
            try {
                SAXParser p = SAXParserFactory.newInstance().newSAXParser();
                DsParser dsp = new DsParser();
                Enumeration urls = Thread.currentThread().getContextClassLoader().getResources("META-INF/datasource.xml");
                while(urls.hasMoreElements()) {
                    URL u = (URL)urls.nextElement();
                    p.parse(u.openStream(),dsp );
                }
            }
            catch(Exception ex) {
                System.out.println("error parsing " + ex.getMessage() );
            }
            
        }
        return datasources.get(name);
    }
    
    private static class DsParser extends DefaultHandler {
        private Map map = new HashMap();
        private StringBuffer sb;
        
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if( qName.equals("datasource")) {
                map.clear();
            }
            sb = new StringBuffer();
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            sb.append(ch, start, length);
        }
        
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if(qName.equals("datasource")) {
                BasicDataSource ds = new BasicDataSource(map);
                if( ds.getName()!=null) {
                    datasources.put(ds.getName(),ds);
                }
                map.clear();
            }
            else if(qName.equals("datasources")) {
                //
            }
            else {
                map.put(qName, sb.toString());
            }
        }

    }
    
    public static void main(String[] args) {
        System.out.println(DataSourceManager.getDataSource("DefaultDS"));
    }
}
