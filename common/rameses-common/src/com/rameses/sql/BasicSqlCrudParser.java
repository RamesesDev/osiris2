/*
 * CrudParser.java
 *
 * Created on August 12, 2010, 8:05 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import com.rameses.sql.CrudModel.CrudField;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author elmo
 */
public class BasicSqlCrudParser extends DefaultHandler {
    
    private CrudModel crudModel;
    private StringBuffer sb;
    
    public BasicSqlCrudParser() {
    }
    
    public CrudModel parse(InputStream is) throws Exception {
        try {
            crudModel = new CrudModel();
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse( is, this );
            return crudModel;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try { is.close(); } catch(Exception ign){;}
        }
        
    }
    
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("table")) {
            String tableName = attributes.getValue( "name" );
            String alias = attributes.getValue("alias");
            crudModel.setTableName(tableName);
            crudModel.setAlias(alias);
            sb = new StringBuffer();
        } else if(qName.equals("link-ref")) {
            sb = new StringBuffer();
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(sb!=null) sb.append( ch, start, length );
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("table")) {
            String txt = sb.toString();
            try {
                StringReader rd = new StringReader(txt);
                BufferedReader brd = new BufferedReader(rd);
                String line = null;
                while( (line=brd.readLine())!=null ) {
                    if(line.trim().length()==0) continue;
                    if(line.trim().startsWith("#"))continue;
                    
                    //we are parsing the ff: name=fieldName *
                    CrudField crf = new CrudField();
                    crf.setName( line.substring(0, line.indexOf("=")).trim());
                    String f = line.substring(line.indexOf("=")+1).trim();
                    if(f.contains("*")) {
                        crf.setPrimary(true);
                        crf.setFieldName( f.substring(0, f.indexOf("*")).trim());
                    } else {
                        crf.setFieldName( f.trim() );
                    }
                    
                    //if field name has an alias example customer.name, then it is assumed
                    //to be a link field. also primary is set off because link fields
                    //can never be primary keys
                    if(crf.getFieldName().indexOf(".")>0) {
                        crf.setLinked( true );
                        crf.setPrimary( false) ;
                    }
                    crudModel.getFields().add(crf);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        } else if(qName.equals("link-ref")) {
            crudModel.setLinkTable(sb.toString());
        }
    }
}
