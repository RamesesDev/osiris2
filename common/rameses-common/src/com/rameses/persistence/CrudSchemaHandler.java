/*
 * CrudSchemaHandler2.java
 *
 * Created on August 16, 2010, 8:36 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.schema.BreakException;
import com.rameses.schema.ComplexField;
import com.rameses.schema.LinkField;
import com.rameses.schema.Schema;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaHandler;
import com.rameses.schema.SchemaHandlerStatus;
import com.rameses.schema.SimpleField;
import com.rameses.sql.CrudModel;
import com.rameses.sql.CrudModel.CrudField;

/**
 *
 * @author elmo
 */
public class CrudSchemaHandler implements SchemaHandler {
    
    private static final String TABLENAME = "tablename";
    private static final String DBFIELD = "dbfield";
    private static final String PRIMARY = "primary";
    
    private SchemaHandlerStatus status;
    private CrudModel crudModel;
    private Schema schema;
    private String tableName;
    private String prefix;
    
    
    
    public CrudSchemaHandler() {
    }
    
    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }
    
    public void startSchema(Schema schema) {
        this.schema = schema;
        
    }
    
    /***
     * if context element name was set by the link field,
     * compare the current element processed.
     * if element is not the same as context element,
     * do not make this element the main table but we still need
     * to scan through each of its fields.
     */
    public void startElement(SchemaElement element, Object data) {
        if( tableName == null ) {
            tableName = (String)element.getProperties().get(TABLENAME);
            if(tableName!=null && element==null) tableName = element.getName();
            if(tableName==null) tableName = element.getName();
            String alias = element.getName();
            crudModel = new CrudModel();
            if(alias==null) alias = tableName;
            crudModel.setTableName( tableName );
            crudModel.setAlias(alias);
        }
    }
    
    /**
     * checks the referenced element if there is a table.
     * if reference table has a tablename do not proceed.
     * This means the referenced element is another table.
     */
    public void startLinkField(LinkField f) {
        SchemaElement e = schema.getElement( f.getRef() );
        String tblname =  (String)e.getProperties().get(TABLENAME);
        if( tblname != null  && tblname.trim().length()>0)
            throw new BreakException();
    }
    
    /***
     * how to determine the fieldname to use in the database?
     *   if there is a dbfield this is automatically the fieldname
     *   if not, use simplefield.name
     *
     * how to determine what map field to use? map field refers to
     * the mapping of the data object.
     *   if there is a mapfield, automatically use it.
     *   if there is a prefix specified, append prefix + "_" + name
     *   else use refname
     *
     * how to determine if we should exclude the field?
     *   check if the field is under a linkfield scope.
     *   then check if the fieldname is excluded there.
     */
    public void processField(SimpleField sf, String refname, Object value) {
        if( status.isExcludeField(sf)) return;
        
        
        String mapfield = sf.getName();
        
        //this is very important
        //if this has no table name, exclude the excluded fields and use the refname instead
        String tbname = (String)sf.getElement().getProperties().get(TABLENAME);
        if(tbname==null || tbname.trim().length()==0) {
            if(status.isExcludeField(sf)) return;
            mapfield = refname;
        }
        
        String fieldName = (String) sf.getProperties().get(DBFIELD);
        if(fieldName==null || fieldName.trim().length()==0) {
            fieldName = mapfield;
        }
        
        CrudField cf = new CrudField();
        String sprimary = (String)sf.getProperties().get(PRIMARY);
        boolean primary = false;
        if( sprimary!=null) {
            try {
                primary = Boolean.parseBoolean(sprimary);
            } catch(Exception ign){;}
        }
        cf.setName(mapfield);
        cf.setFieldName(fieldName);
        cf.setPrimary(primary);
        crudModel.getFields().add(cf);
    }
    
    public void endElement(SchemaElement element) {
        
    }
    
    
    
    public void endLinkField(LinkField f) {
    }
    
    public void startComplexField(ComplexField cf) {
    }
    
    public void endComplexField(ComplexField cf) {
    }
    
    public void endSchema(Schema schema) {
    }
    
    public CrudModel getCrudModel() {
        return crudModel;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    
    
    
    
}