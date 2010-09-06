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
import com.rameses.util.ExprUtil;
import java.util.Map;

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
                //if(mapfield!=null && mapfield.trim().length()>0) refname = mapfield;
                
                //check if linked field
                Object val = null;
                if(data!=null) {
                    if( mapfield!=null && mapfield.trim().length()>0) {
                        val = propertyResolver.getProperty( data, mapfield );
                    }    
                    else {
                        val = propertyResolver.getProperty( data, refname );
                    }    
                }
                
                handler.processField(sf, refname, val);
            } else if( fld instanceof LinkField ) {
                LinkField lf = (LinkField)fld;
                String ref = lf.getRef();
                if(ref==null) {
                    throw new RuntimeException("SchemaScanner error. link field requires a ref attribute for schema " + schema.getName() );
                }
                
                //extract context field before pushing this linkfield.
                LinkField contextField = status.getFieldContext();
                status.pushLinkField( lf );
                try {
                    //link to element and scan it. if there is a : then access schema manager
                    SchemaElement linkElement = null;
                    if(ref.indexOf(":")>0) {
                        linkElement = schema.getSchemaManager().getElement(ref);
                    } else {
                        linkElement = schema.getElement(ref);
                    }
                    if( linkElement == null ) {
                        System.out.println("ERROR LINK FIELD -> " + ref + " element does not exist");
                    } else {
                        handler.startLinkField( lf, lf.getName(), linkElement );
                        scanElement( schema, status, linkElement, data, handler );
                        handler.endLinkField( lf );
                    }
                } catch(BreakException be) {;}
                status.popLinkField();
            } else if( fld instanceof ComplexField ) {
                ComplexField cf = (ComplexField)fld;
                try {
                    SchemaElement refElement = null;
                    String refname = cf.getName();
                    Object val = null;
                    String ref = cf.getRef();
                    
                    if(cf.getSerializer()==null) {
                        //bypass ref checks if it is a serializer
                        if(ref==null )
                            throw new RuntimeException("SchemaScanner error. ref is required for complex field" );
                        
                        //add dynamic ref. Dynamic ref are marked with $ and enclosed with braces e.g.:  ${ref-name}
                        if( data!=null && ref.indexOf("$")>=0) {
                            ref = ExprUtil.substituteValues(ref, (Map)data, propertyResolver );
                        }
                        
                        
                        if(ref!=null && ref.indexOf("$")<0) {
                            if(ref.indexOf(":")>0) {
                                refElement = schema.getSchemaManager().getElement(ref);
                            } else {
                                refElement = schema.getElement(ref);
                            }
                            if(refname==null) refname = refElement.getName();
                        }
                        
                        
                        if(data!=null)
                            val = propertyResolver.getProperty( data, refname );
                    }
                    handler.startComplexField( cf, refname, refElement,val );
                    handler.endComplexField( cf );
                } catch(BreakException be) {;}
            }
        }
        handler.endElement( element );
    }
    
    
}
