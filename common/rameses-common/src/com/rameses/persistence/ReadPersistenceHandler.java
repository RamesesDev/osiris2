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
import com.rameses.schema.SchemaHandler;
import com.rameses.schema.SchemaHandlerStatus;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SimpleField;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * This class is used for persistence
 */
public class ReadPersistenceHandler implements SchemaHandler {
    
    private static final String TABLENAME = "tablename";
    
    private SchemaHandlerStatus status;
    private Queue<SqlQuery> queue = new LinkedList();
    
    private Schema schema;
    private Stack<SqlQuery> stack = new Stack(); 
    private SqlManager sqlManager;
    private SchemaManager schemaManager;
    private String action = "read";
    private List<String> removeFields = new ArrayList();
    
    private SqlContext sqlContext;
    
    public ReadPersistenceHandler(SchemaManager schemaManager, SqlContext context) {
        this.sqlManager = sqlManager;
        this.schemaManager = schemaManager;
        sqlContext = context;
    }
    
    public void setStatus(SchemaHandlerStatus status) {
        this.status = status;
    }
    
    public void startSchema(Schema schema) {
        this.schema = schema;
        removeFields.clear();
    }

    public void startElement(SchemaElement element, Object data) {
        //check if element has tableName;
        String tblName = (String) element.getProperties().get(TABLENAME);
        if(tblName!=null && tblName.trim().length()>0) {
            String name = element.getName();
            String contextPath = status.getContextPath();
            StringBuffer sb = new StringBuffer();
            sb.append(schema.getName());
            sb.append(":"+element.getName());
            sb.append( "_" + action );
            sb.append( "." + SchemaConf.XML_SCHEMA );
            String ename = sb.toString() ;
            SqlQuery sq = sqlContext.createNamedQuery(ename);
            stack.push(sq);
            queue.add(sq);
        }
    }
    
    public void startLinkField(LinkField f) {;}
    
    public void processField(SimpleField sf, String refname, Object value) {
        if(!stack.empty()) {
            String sname = sf.getName();
            String tbname = (String)sf.getElement().getProperties().get(TABLENAME);
            //if this has no table name, exclude the excluded fields.
            if(tbname!=null && tbname.trim().length()>0) {
                if(status.isExcludeField(sf)) {
                    removeFields.add(sf.getName());
                }
            }
            
            if(tbname==null || tbname.trim().length()==0) {
                sname = refname;
            }
            
            SqlQuery sq = stack.peek();
            if( sq.getParameterNames().indexOf(sf.getName())>=0) {
                sq.setParameter( sname , value );
            }
            
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
    
    
    
    public void endLinkField(LinkField f) {
    }
    
    public void startComplexField(ComplexField cf) {
    }
    
    public void endComplexField(ComplexField cf) {
    }
    
    public void endSchema(Schema schema) {
    }
    
    public Queue<SqlQuery> getQueue() {
        return queue;
    }

    public SqlManager getSqlManager() {
        return sqlManager;
    }

    public void setSqlManager(SqlManager sqlManager) {
        this.sqlManager = sqlManager;
    }

    public List<String> getRemoveFields() {
        return removeFields;
    }

    
    
}
