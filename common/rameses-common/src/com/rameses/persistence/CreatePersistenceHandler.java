/*
 * SchemaCrudBuilder.java
 *
 * Created on August 14, 2010, 6:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.persistence;

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
    
    public CreatePersistenceHandler(SchemaManager schemaManager, SqlContext context) {
        super(schemaManager, context);
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
            se.setParameter( sname , value );
        }
    }

    
}
