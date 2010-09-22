/*
 * SchemaCrudBuilder.java
 *
 * Created on August 14, 2010, 6:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.persistence;

import com.rameses.schema.ComplexField;
import com.rameses.schema.Schema;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SimpleField;
import com.rameses.sql.AbstractSqlTxn;
import com.rameses.sql.CrudModel;
import com.rameses.sql.CrudSqlBuilder;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlQuery;
import com.rameses.sql.SqlUnit;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * This class is used for persistence
 */
public class ReadPersistenceHandler extends AbstractPersistenceHandler {
    
    
    private List<String> removeFields = new ArrayList();
    private List<String> serializedFields = new ArrayList();
    
    public ReadPersistenceHandler(SchemaManager schemaManager, SqlContext context, Object rootData) {
        super(schemaManager,context,rootData);
    }
    
    public void startSchema(Schema schema) {
        super.startSchema(schema);
        removeFields.clear();
    }
    
    protected String getAction() {
        return "read";
    }
    
    protected SqlUnit getSqlUnit(CrudModel model) {
        return CrudSqlBuilder.getInstance().getReadSqlUnit(model);
    }
    
    protected AbstractSqlTxn getSqlTransaction(String name) {
        return sqlContext.createNamedQuery(name);
    }
    
    public void processField(SimpleField sf, String refname, Object value) {
        if(!stack.empty()) {
            String tbname = (String)sf.getElement().getProperties().get(TABLENAME);
            //if this has no table name, exclude the excluded fields.
            if(tbname!=null && tbname.trim().length()>0) {
                if(status.isExcludeField(sf)) {
                    removeFields.add( sf.getName() );
                }
            }
            DbElementContext dbec = stack.peek();
            String sname = dbec.correctName( sf.getName() );
            SqlQuery sq = (SqlQuery)dbec.getSqlTxn();
            if( sq.getParameterNames().indexOf(sname)>=0) {
                sq.setParameter( sname , value );
            }
        }
    }
    
    public void startComplexField(ComplexField cf, String refname, SchemaElement element, Object data) {
        String serializer = cf.getSerializer();
        
        //serialize object if serializer is mentioned.
        //lookup appropriate serializer if not exist use default
        
        if(serializer!=null) {
            if(!stack.empty()) {
                DbElementContext dbec = stack.peek();
                String sname = dbec.correctName( cf.getName() );
                serializedFields.add(sname);
            }
        }
    }
    
    public List<String> getRemoveFields() {
        return removeFields;
    }
    
    public List<String> getSerializedFields() {
        return serializedFields;
    }
}
