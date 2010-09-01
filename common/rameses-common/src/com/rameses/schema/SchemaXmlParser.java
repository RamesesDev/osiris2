/*
 * XmlSchemaParser.java
 *
 * Created on August 12, 2010, 1:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import com.rameses.util.ParserUtil;
import com.rameses.common.PropertyResolver;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author elmo
 */
public class SchemaXmlParser extends DefaultHandler{
    
    private PropertyResolver resolver;
    private SchemaElement currentElement;
    private Schema schema; 
    private SchemaManager schemaManager;
    
    /** Creates a new instance of XmlSchemaParser */
    public SchemaXmlParser(SchemaManager sm) {
        schemaManager = sm;
        resolver = sm.getConf().getPropertyResolver();
    }
    
    public Schema parse(InputStream is, String name ) throws Exception {
        schema = new Schema(name,schemaManager);
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        parser.parse( is, this );
        return schema;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals("element")) {
            currentElement = new SchemaElement(schema);
            ParserUtil.loadAttributes(currentElement,currentElement.getProperties(), attributes, resolver);
            schema.addElement(currentElement);
        }
        else if(qName.equals("field")) {
            SimpleField field = new SimpleField();
            ParserUtil.loadAttributes(field,field.getProperties(), attributes, resolver);
            currentElement.addSchemaField(field);
        }
        else if(qName.equals("link-field") || qName.equals("link")) {
            LinkField field = new LinkField();
            ParserUtil.loadAttributes(field,field.getProperties(), attributes, resolver);
            currentElement.addSchemaField(field);
        }
        else if(qName.equals("complex")) {
            ComplexField field = new ComplexField();
            ParserUtil.loadAttributes(field,field.getProperties(), attributes, resolver);
            currentElement.addSchemaField(field);
        }
        else {
            if(!qName.equals("schema")) System.out.println("schema " + qName + " is not supported"); 
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("element")) {
            currentElement = null;
        }
    }
    
    
}
