/*
 * SchemaSqlCacheProvider.java
 *
 * Created on August 13, 2010, 8:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.schema.Schema;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.sql.CrudModel;
import com.rameses.sql.CrudSqlBuilder;
import com.rameses.sql.SqlUnit;
import com.rameses.sql.SqlUnitProvider;

/**
 *
 * @author elmo
 */
public class XmlSchemaSqlUnitProvider extends SqlUnitProvider {
    
    private final static String CREATE  = "create";
    private final static String READ  = "read";
    private final static String UPDATE  = "update";
    private final static String DELETE  = "delete";
    private final static String LIST  = "list";
    
    public String getType() {
        return SchemaConf.XML_SCHEMA;
    }
    
    /***
     * pattern:   schemaname:[/.*]{0,}     
     * acceptable names sample
     *    customer_read.xml-schema     
     *    customer:/field1_read.xml-schema
     *    customer:address_read.xml-schema
     */
    
    public SqlUnit getSqlUnit(String name) {
        SchemaManager schemaManager = (SchemaManager) getConf().getExtensions().get(SchemaManager.class);
        if(schemaManager==null)
            throw new RuntimeException("Please add a SchemaFactory in the SqlManagerConf.extensions. Use " + SchemaManager.class.getName() + " as key ");
        
        String action = null;
        if( name.endsWith("_"+CREATE+"."+SchemaConf.XML_SCHEMA) ) action = CREATE;
        else if( name.endsWith("_"+READ+"."+SchemaConf.XML_SCHEMA) ) action = READ;
        else if( name.endsWith("_"+UPDATE+"."+SchemaConf.XML_SCHEMA) ) action = UPDATE;
        else if( name.endsWith("_"+DELETE+"."+SchemaConf.XML_SCHEMA) ) action = DELETE;
        else if( name.endsWith("_"+LIST+"."+SchemaConf.XML_SCHEMA) ) action = LIST;
        if( action == null )
            throw new RuntimeException("Please explicitly provide an action for " + name );
        
        String source = name.substring(0, name.lastIndexOf( "_" + action+"."+SchemaConf.XML_SCHEMA));
        
        //parse as follows:
        //example 1:   
        //  billing:customer/delivery/address
        //    schema : billing
        //    element: customer
        //    context: delivery/address
        //example 2:   
        //  billing:/delivery/address
        //    schema : billing
        //    element: billing
        //    context: delivery/address
        String schemaName = null;
        String elementName = null;
        String contextPath = null;
        
        if( source.indexOf(":")>0) {
            schemaName = source.substring( 0, source.indexOf(":") );
            String tmp = source.substring( source.indexOf(":")+1 );
            if( tmp.startsWith("/") ) {
                elementName = schemaName;
                contextPath = tmp.substring(1);
                contextPath = contextPath.replaceAll("/", "_");                
            }    
            else if(tmp.indexOf("/")>1) { 
                elementName = tmp.substring( 0, tmp.indexOf("/") );
                contextPath = tmp.substring( tmp.indexOf("/")+1 );
                contextPath = contextPath.replaceAll("/", "_");
            }    
            else {
                elementName = tmp;
            }
        }    
        else {
            schemaName = source;    
        }
        
        
        Schema schema = schemaManager.getSchema( schemaName );
        SchemaElement element = null;
        if( elementName==null ) {
            element = schema.getRootElement();
        } else {
            element = schema.getElement(elementName);
        }
        
        if(schema==null)
            throw new RuntimeException("error schema is null " + schemaName );
        if(element==null)
            throw new RuntimeException("schema element is null " + elementName );
        
        CreateSchemaHandler crudBuilder = new CreateSchemaHandler();
        crudBuilder.setPrefix( contextPath );
        SchemaScanner sc = schemaManager.newScanner();
        
        sc.scan(schema, element, crudBuilder ) ;
        //CrudModel crudModel = crudBuilder.getCrudModel();
        CrudModel crudModel = crudBuilder.getCrudModel();
        if(action.equals(CREATE))
            return CrudSqlBuilder.getInstance().getCreateSqlUnit(crudModel);
        else if(action.equals(READ))
            return CrudSqlBuilder.getInstance().getReadSqlUnit(crudModel);
        else if(action.equals(UPDATE))
            return CrudSqlBuilder.getInstance().getUpdateSqlUnit(crudModel);
        else if(action.equals(DELETE))
            return CrudSqlBuilder.getInstance().getDeleteSqlUnit(crudModel);
        else
            return CrudSqlBuilder.getInstance().getListSqlUnit(crudModel, schemaName);
    }
    
    
    
    
    
}
