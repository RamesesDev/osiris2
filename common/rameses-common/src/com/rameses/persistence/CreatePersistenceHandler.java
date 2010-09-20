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
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SimpleField;
import com.rameses.sql.AbstractSqlTxn;
import com.rameses.sql.CrudModel;
import com.rameses.sql.CrudSqlBuilder;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlUnit;

/**
 *
 * This class is used for persistence
 */
public class CreatePersistenceHandler extends AbstractPersistenceHandler {
    
    public CreatePersistenceHandler(SchemaManager schemaManager, SqlContext context, Object rootData) {
        super(schemaManager, context, rootData);
    }
    
    protected String getAction() {
        return "create";
    }
    
    protected SqlUnit getSqlUnit(CrudModel model) {
        return CrudSqlBuilder.getInstance().getCreateSqlUnit(model);
    }
    
    protected AbstractSqlTxn getSqlTransaction(String name) {
        return sqlContext.createNamedExecutor( name );
    }
    
    public void processField(SimpleField sf, String refname, Object value) {
        if(!stack.empty()) {
            
            //String sname = status.getFixedFieldName( sf );
            String tbname = (String)sf.getElement().getProperties().get(TABLENAME);
            //if this has no table name, exclude the excluded fields.
            if(tbname==null || tbname.trim().length()==0) {
                if(status.isExcludeField(sf)) return;
            }
            
            DbElementContext dbec = stack.peek();
            String sname = dbec.correctName( sf.getName() );
            SqlExecutor se = (SqlExecutor)dbec.getSqlTxn();
            
            Object rvalue = passSerializer( sf, value, refname );
            se.setParameter( sname , rvalue );
        }
    }
    
    public void startComplexField(ComplexField cf, String refname, SchemaElement element, Object data) {
        String serializer = cf.getSerializer();
        
        //serialize object if serializer is mentioned.
        //lookup appropriate serializer if not exist use default
        
        if(serializer!=null) {
            if(!stack.empty()) {
                Object svalue = passSerializer( cf, data, refname );
                DbElementContext dbec = stack.peek();
                SqlExecutor se = (SqlExecutor)dbec.getSqlTxn();
                se.setParameter( cf.getName() , svalue );
                
            }
        }
    }
    
}
