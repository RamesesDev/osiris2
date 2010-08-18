/*
 * DefaultSchemaProvider.java
 *
 * Created on August 13, 2010, 10:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public class XmlSchemaProvider extends SchemaProvider {
    
    
    
    /** Creates a new instance of DefaultSchemaProvider */
    public XmlSchemaProvider() {
    }
    
    public Schema getSchema(String name) {
        InputStream is = null;
        try {
            is = getConf().getResourceProvider().getResource(name);
            SchemaXmlParser parser = new SchemaXmlParser( getConf().getPropertyResolver() );
            return parser.parse( is, name );
        } catch(Exception e) {
            return null;
        } finally {
            try { is.close(); } catch(Exception ign){;}
        }
    }

    
    
}
