/*
 * SchemaScanner.java
 *
 * Created on August 14, 2010, 2:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import com.rameses.common.PropertyResolver;

/**
 *
 * This can only be created thru the SchemaFactory
 */
public final class SchemaScanner {
    
    private PropertyResolver propertyResolver;
   
    
    public SchemaScanner(PropertyResolver resolver) {
        this.propertyResolver = resolver;
        if(propertyResolver==null)
            throw new RuntimeException("Property resolver is not set when building SchemaScanner");
    }
    
    public void scan(Schema schema, SchemaHandler handler) {
        scan( schema, schema.getRootElement(), null, handler );
    }
    
    public void scan(Schema schema, Object data, SchemaHandler handler) {
        scan( schema, schema.getRootElement(), data, handler );
    }
    
    public void scan(Schema schema, SchemaElement element,SchemaHandler handler) {
        scan( schema, element, null, handler );
    }
    
    public void scan(Schema schema, SchemaElement element, Object data, SchemaHandler handler) {
        if(handler==null) handler = new DefaultSchemaHandler();
        SchemaHandlerStatus status = new SchemaHandlerStatus(schema);
        handler.setStatus( status );
        handler.startSchema(schema);
        scanElement( schema, status, element, data, handler );
        handler.endSchema(schema);
    }
    
    //scans the elements
    private void scanElement( Schema schema, SchemaHandlerStatus status, SchemaElement element, Object data, SchemaHandler handler) {
        handler.startElement( element, data );
        for(SchemaField fld: element.getFields()) {
            if( fld instanceof SimpleField ) {
                //check first if we need to process this field. refer to parentLink stack above
                SimpleField sf = (SimpleField)fld;
                String refname = status.getFixedFieldName( sf );
                
                String mapfield = sf.getMapfield();
                if(mapfield!=null && mapfield.trim().length()>0) refname = mapfield;
                
                //check if linked field
                Object val = null;
                if(data!=null)
                    val = propertyResolver.getProperty( data, refname );
                
                handler.processField(sf, refname, val );
            } else if( fld instanceof LinkField ) {
                LinkField lf = (LinkField)fld;
                String ref = lf.getRef();
                if(ref==null) {
                    throw new RuntimeException("SchemaScanner error. link-field requires a ref attribute for schema " + schema.getName() );
                }
                    
                status.pushLinkField( lf );
                
                try {
                    handler.startLinkField( lf );
                    //link to element and scan it.
                    SchemaElement linkElement = schema.getElement(ref);
                    if( linkElement == null ) {
                        System.out.println("ERROR LINK FIELD -> " + ref + " element does not exist");
                    } else {
                        scanElement( schema, status, linkElement, data, handler );
                    }
                    handler.endLinkField( lf );
                } 
                catch(BreakException be) {;}
                status.popLinkField();
            } else if( fld instanceof ComplexField ) {
                ComplexField cf = (ComplexField)fld;
                try {
                    handler.startComplexField( cf );
                    handler.endComplexField( cf );
                }
                catch(BreakException be) {;}
            }
        }
        handler.endElement( element );
    }
    
    
}
