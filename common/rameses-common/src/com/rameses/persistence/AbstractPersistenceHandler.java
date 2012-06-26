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
import com.rameses.schema.LinkField;
import com.rameses.schema.Schema;
import com.rameses.schema.SchemaConf;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaField;
import com.rameses.schema.SchemaHandler;
import com.rameses.schema.SchemaHandlerStatus;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.sql.AbstractSqlTxn;
import com.rameses.sql.CrudModel;
import com.rameses.sql.CrudSqlBuilder;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlUnit;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * This class is used for persistence
 */
public abstract class AbstractPersistenceHandler implements SchemaHandler {
    
    protected static final String TABLENAME = "tablename";
    
    protected SchemaHandlerStatus status;
    protected Schema schema;
    private Queue<AbstractSqlTxn> queue = new LinkedList();
    protected SqlContext sqlContext;
    protected SchemaManager schemaManager;
    protected SqlManager sqlManager;
    
    protected Stack<DbElementContext> stack = new Stack();
    protected Object rootData;
    
    public AbstractPersistenceHandler(SchemaManager schemaManager, SqlContext context, Object rootData) {
        this.sqlManager = context.getSqlManager();
        this.schemaManager = schemaManager;
        this.sqlContext = context;
        this.rootData = rootData;
    }
    
    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }
    
    public void startSchema(Schema schema) {
        this.schema = schema;
    }
    
    protected abstract String getAction();
    protected abstract SqlUnit getSqlUnit(CrudModel model);
    protected abstract AbstractSqlTxn getSqlTransaction(String name);
    
    private CrudSqlBuilder sqlBuilder;
    
    public CrudSqlBuilder getCrudSqlBuilder() {
        if( sqlBuilder != null ) return sqlBuilder;
        
        if( sqlContext != null && sqlContext.getDialect() != null ) {
            return (sqlBuilder = sqlContext.getDialect().createCrudSqlBuilder());
        }
        else {
            return (sqlBuilder = CrudSqlBuilder.getInstance());
        }
    }
    
    public void startElement(SchemaElement element, Object data) {
        //check if element has tableName;
        String tblName = (String) element.getProperties().get(TABLENAME);
        if(tblName!=null && tblName.trim().length()>0) {
            String name = element.getName();
            String contextPath = status.getContextPath();
            StringBuffer sb = new StringBuffer();
            sb.append(schema.getName());
            sb.append(":"+element.getName() + "/" + contextPath);
            sb.append( "_" + getAction() );
            sb.append( "." + SchemaConf.XML_SCHEMA );
            String ename = sb.toString() ;
            
            //check if sql unit exists in the cache
            SqlUnit u = sqlManager.getCache().get(ename);
            if(u==null) {
                CrudSchemaHandler crudBuilder = new CrudSchemaHandler();
                crudBuilder.setPrefix( contextPath );
                SchemaScanner sc = schemaManager.newScanner();
                sc.scan(schema, element, crudBuilder ) ;
                CrudModel crudModel = crudBuilder.getCrudModel();
                u = getSqlUnit(crudModel);
                sqlManager.getCache().put(ename, u);
            }
            
            AbstractSqlTxn sq = getSqlTransaction(ename);
            DbElementContext dbec = new DbElementContext(element);
            dbec.setSqlTxn( sq );
            stack.push( dbec );
            queue.add(sq);
        }
    }
    
    //pop a new context fo the link field has a table
    public void startLinkField(LinkField f, String refname, SchemaElement element) {
        DbElementContext dbec = stack.peek();
        if( f.isPrefixed() ) {
            dbec.addContext( f.getName() );
        }
    }
    
    public void endLinkField(LinkField f) {
        DbElementContext dbec = stack.peek();
        if( f.isPrefixed() ) {
            dbec.removeContext();
        }
    }
    
    public void endElement(SchemaElement element) {
        String tblName = (String) element.getProperties().get(TABLENAME);
        if(tblName!=null && tblName.trim().length()>0) {
            if(!stack.empty()) {
                stack.pop();
            }
        }
    }
    
    
    
    public void endComplexField(ComplexField cf) {
    }
    
    public void endSchema(Schema schema) {
    }
    
    
    public SqlManager getSqlManager() {
        return sqlManager;
    }
    
    public void setSqlManager(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }
    
    public void startComplexField(ComplexField cf, String refname, SchemaElement element, Object data ) {
    }
    
    public Queue<AbstractSqlTxn> getQueue() {
        return queue;
    }
    
    
    
    protected Object passSerializer( SchemaField sf, Object value, String refname ) {
        String serializer = (String)sf.getProperties().get("serializer");
        if(serializer==null || serializer.trim().length()==0) return value;
        
        Object d = rootData;
        String mapfield = (String)sf.getProperties().get("mapfield");
        if(mapfield==null) mapfield = refname;
                
        String merge = (String)sf.getProperties().get("merge");
        if( merge!=null && "true".equals(merge) ) {
            Map map = new HashMap();
            map.putAll( (Map)d );
            //find out the fields you want removed. to do this we need to scan the schema again.
            SchemaScanner scanner = schemaManager.newScanner();
            scanner.scan( this.schema, sf.getElement(), new ExcludeFieldsSchemaHandler(map) );
            d = map;
        }
        else if(!mapfield.equals(".")) {
            d = schemaManager.getConf().getPropertyResolver().getProperty(d, mapfield);
        }
        
        if(d==null) return null;
        return schemaManager.getSerializer().write( d );
    }

    
}
