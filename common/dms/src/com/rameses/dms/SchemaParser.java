package com.rameses.dms;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SchemaParser extends DefaultHandler {
    
    private Schema schema;
    private Table currentTable;
    private StringBuffer buffer = new StringBuffer();
    
    
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (qName.equals("schema")) {
            schema = new Schema();
            schema.setTargetdialect(attributes.getValue("targetdialect"));
            schema.setSourcedialect(attributes.getValue("sourcedialect"));
        } 
        else if (qName.equals("table")) {
            currentTable = new Table();
            currentTable.setName( attributes.getValue("name") );
            currentTable.setTablename(attributes.getValue("tablename"));
            currentTable.setSource(attributes.getValue("source"));
            schema.setTable(currentTable);
        } 
        else if (qName.equals("field")) {
            Field f = new Field();
            f.setName(attributes.getValue("name"));
            f.setFieldname(attributes.getValue("fieldname"));
            f.setSourcefield(attributes.getValue("sourcefield"));
            String dtype = attributes.getValue("type");
            if(dtype==null || dtype.trim().length()==0) dtype = "STRING";
            f.setType(DataType.valueOf(dtype.toUpperCase()));
            
            String len = attributes.getValue("length");
            if (len!=null && len.trim().length()>0) {
                f.setLength( Integer.parseInt(len)  );
            }
            String scale = attributes.getValue("scale");
            if (scale!=null && scale.trim().length()>0) {
                f.setScale( Integer.parseInt(scale)  );
            }
            currentTable.getFields().add(f);
        } else                    if (qName.equals("source")) {
            buffer = new StringBuffer();
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }
    
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals("source")) {
            currentTable.setSource(buffer.toString());
        }
    }

    public Schema getSchema() {
        return schema;
    }
    
    
    
    
}