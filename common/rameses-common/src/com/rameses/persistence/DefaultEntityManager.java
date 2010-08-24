/*
 * EntityManager.java
 *
 * Created on August 15, 2010, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.schema.Schema;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.schema.ValidationResult;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author elmo
 */
public class DefaultEntityManager implements EntityManager {
    
    private SqlContext sqlContext;
    private SchemaManager schemaManager;
    private boolean debug;
    
    public DefaultEntityManager(SchemaManager scm, SqlContext sqlContext) {
        this.sqlContext= sqlContext;
        this.schemaManager = scm;
    }
    
    public void setSqlContext(SqlContext ctx) {
        this.sqlContext = ctx;
    }
    
    public SqlContext getSqlContext() {
        return this.sqlContext;
    }
    
    public Object create(String schemaName, Object data) {
        Queue<SqlExecutor> queue = null;
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            CreatePersistenceHandler handler = new CreatePersistenceHandler(schemaManager,sqlContext);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,data,handler);
            queue = handler.getQueue();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        
        try {
            sqlContext.openConnection();
            while(!queue.isEmpty()) {
                SqlExecutor se= queue.remove();
                if(debug) {
                    System.out.println(se.getStatement());
                    int i = 0;
                    for(Object s: se.getParameterNames()) {
                        System.out.println("param->"+s+ "="+ se.getParameterValues().get(i++) );
                    }
                }
                se.execute();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
            
        } finally {
            sqlContext.closeConnection();
        }
        
        return data;
    }
    
    public Object read(String schemaName, Object data) {
        Queue<SqlQuery> queue = null;
        List<String> removeFields = new ArrayList();
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            ReadPersistenceHandler handler = new ReadPersistenceHandler(schemaManager,sqlContext);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,data,handler);
            queue = handler.getQueue();
            removeFields = handler.getRemoveFields();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            Map map = new HashMap();
            sqlContext.openConnection();
            while(!queue.isEmpty()) {
                SqlQuery sq = queue.remove();
                if(debug) {
                    System.out.println(sq.getStatement());
                    int i =0;
                    for(Object s: sq.getParameterNames()) {
                        System.out.println("param->"+s+ "="+ sq.getParameterValues().get(i++) );
                    }
                }
                
                Map m = (Map)sq.getSingleResult();
                if(m!=null) map.putAll(m);
            }
            
            //print excluded fields
            if(debug) {
                System.out.println("excluded fields:");
                for(String s: removeFields) {
                    System.out.println("       " +s);
                }
            }
            
            for(String s: removeFields) {
                map.remove(s);
            }
            
            data = map;
        } catch(Exception e) {
            throw new RuntimeException(e);
            
        } finally {
            sqlContext.closeConnection();
        }
        return data;
    }
    
    
    /***
     * we need to ensure first to read the record before updating.
     *
     */
    public Object update(String schemaName, Object data) {
        if(!(data instanceof Map))
            throw new RuntimeException("Data that is not a map is not yet supported at this time");
        
        Map oldData = (Map) read( schemaName, data );
        oldData.putAll( (Map)data );
        
        Queue<SqlExecutor> queue = null;
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            UpdatePersistenceHandler handler = new UpdatePersistenceHandler(schemaManager,sqlContext);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,oldData,handler);
            queue = handler.getQueue();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        
        try {
            sqlContext.openConnection();
            while(!queue.isEmpty()) {
                SqlExecutor se= queue.remove();
                if(debug) {
                    System.out.println(se.getStatement());
                    int i = 0;
                    for(Object s: se.getParameterNames()) {
                        System.out.println("param->"+s+ "="+ se.getParameterValues().get(i++) );
                    }
                }
                se.execute();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
            
        } finally {
            sqlContext.closeConnection();
        }
        
        return oldData;
    }
    
    public void delete(String schemaName, Object data) {
        Queue<SqlExecutor> queue = null;
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            DeletePersistenceHandler handler = new DeletePersistenceHandler(schemaManager,sqlContext);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,data,handler);
            queue = handler.getQueue();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        
        try {
            sqlContext.openConnection();
            while(!queue.isEmpty()) {
                SqlExecutor se= queue.remove();
                if(debug) {
                    System.out.println(se.getStatement());
                    int i = 0;
                    for(Object s: se.getParameterNames()) {
                        System.out.println("param->"+s+ "="+ se.getParameterValues().get(i++) );
                    }
                }
                se.execute();
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
            
        } finally {
            sqlContext.closeConnection();
        }
        
    }
    
    
    
    /***
     * extended methods in the DefaultEntityManager
     */
    public boolean isDebug() {
        return debug;
    }
    
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public Object createModel(String schemaName) {
        return schemaManager.createMap(schemaName);
    }
    
    public ValidationResult validateModel(String schemaName, Object data) {
        return schemaManager.validate(schemaName, data);
    }
    
    
}
