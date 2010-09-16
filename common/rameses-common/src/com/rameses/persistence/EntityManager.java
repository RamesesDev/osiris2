/*
 * EntityManager.java
 *
 * Created on August 15, 2010, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.common.PropertyResolver;
import com.rameses.schema.Schema;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.schema.SchemaSerializer;
import com.rameses.schema.ValidationResult;
import com.rameses.sql.SqlContext;
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
public class EntityManager {
    
    private SqlContext sqlContext;
    private SchemaManager schemaManager;
    private boolean debug;
    private boolean transactionOpen = false;
    
    public EntityManager(SchemaManager scm, SqlContext sqlContext) {
        this.sqlContext= sqlContext;
        this.schemaManager = scm;
    }
    
    public void setSqlContext(SqlContext ctx) {
        if(transactionOpen)
            throw new RuntimeException("SqlContext cannot be set at this time because transaction is currently open");
        this.sqlContext = ctx;
    }
    
    public SqlContext getSqlContext() {
        return this.sqlContext;
    }
    
    public Object create(String schemaName, Object data) {
        return create( schemaName, data, null );
    }
    
    public Object create(String schemaName, Object data, Map vars) {
        Queue queue = null;
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            CreatePersistenceHandler handler = new CreatePersistenceHandler(schemaManager,sqlContext,data);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,data,handler);
            queue = handler.getQueue();
            EntityManagerUtil.executeQueue(queue,sqlContext,vars,transactionOpen,debug);
            
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }
    
    
    /***
     * if there are no records found, this function returns null
     */
    public Object read(String schemaName, Object data) {
        return read( schemaName, data, null);
    }
    
    public Object read(String schemaName, Object data, Map vars) {
        Queue queue = null;
        List<String> removeFields = new ArrayList();
        List<String> serializedFields = new ArrayList();
        
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            ReadPersistenceHandler handler = new ReadPersistenceHandler(schemaManager,sqlContext,data);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,data,handler);
            queue = handler.getQueue();
            removeFields = handler.getRemoveFields();
            serializedFields = handler.getSerializedFields();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            Map map = new HashMap();
            if(!transactionOpen) {
                sqlContext.openConnection();
            }
            while(!queue.isEmpty()) {
                SqlQuery sq = (SqlQuery)queue.remove();
                if(debug) {
                    System.out.println(sq.getStatement());
                    int i =0;
                    for(Object s: sq.getParameterNames()) {
                        System.out.println("param->"+s+ "="+ sq.getParameterValues().get(i++) );
                    }
                }
                if(vars!=null) sq.setVars(vars);
                Map m = (Map)sq.getSingleResult();
                if(m!=null) map.putAll(m);
            }
            
            //print excluded fields
            if(debug) {
                if(removeFields.size()>0)System.out.println("excluded fields:");
                for(String s: removeFields) {
                    System.out.println("       " +s);
                }
                if(serializedFields.size()>0) System.out.println("serialized fields:");
                for(String s: serializedFields) {
                    System.out.println("       " +s);
                }
            }
            
            
            for(String s: removeFields) {
                map.remove(s);
            }
            
            if(serializedFields.size()>0) {
                PropertyResolver resolver = schemaManager.getConf().getPropertyResolver();
                for(String s: serializedFields) {
                    String o = (String)resolver.getProperty(map, s);
                    if(o!=null) {
                        Object x = schemaManager.getSerializer().read(o);
                        resolver.setProperty(map,s,x);
                    }
                }
            }
            
            if(map.size()==0)
                data = null;
            else
                data = map;
            
        } catch(Exception e) {
            throw new RuntimeException(e);
            
        } finally {
            if(!transactionOpen) {
                sqlContext.closeConnection();
            }
        }
        return data;
    }
    
    
    /***
     * we need to ensure first to read the record before updating.
     *
     */
    public Object update(String schemaName, Object data) {
        return update(schemaName, data, null);
    }
    
    public Object update(String schemaName, Object data, Map vars) {
        if(!(data instanceof Map))
            throw new RuntimeException("Data that is not a map is not yet supported at this time");
        Map oldData = (Map) read( schemaName, data );
        oldData.putAll( (Map)data );
        
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            UpdatePersistenceHandler handler = new UpdatePersistenceHandler(schemaManager,sqlContext,data);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,oldData,handler);
            Queue queue = handler.getQueue();
            EntityManagerUtil.executeQueue(queue,sqlContext,vars,transactionOpen,debug); 
            return oldData;
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void delete(String schemaName, Object data) {
        delete(schemaName, data, null);
    }
    
    public void delete(String schemaName, Object data, Map vars) {
        try {
            SchemaScanner scanner = schemaManager.newScanner();
            DeletePersistenceHandler handler = new DeletePersistenceHandler(schemaManager,sqlContext,data);
            Schema schema = schemaManager.getSchema( schemaName );
            SchemaElement element = schema.getElement( schemaName );
            scanner.scan(schema,element,data,handler);
            Queue queue = handler.getQueue();
            EntityManagerUtil.executeQueue(queue,sqlContext,vars,transactionOpen,debug);    
        } catch(Exception ex) {
            throw new RuntimeException(ex);
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
    
   
    
    //returns true if opening transaction was successful.
    public boolean beginTransaction() throws Exception {
        if(transactionOpen) 
            return false;
        if(sqlContext==null) return false;
        sqlContext.openConnection();
        transactionOpen = true;
        return true;
    }
    
    //returns true if closing transaction was successful.
    public boolean closeTransaction() throws Exception {
        if(!transactionOpen) 
            return false;
        if(sqlContext==null) return false;
        transactionOpen = false;
        sqlContext.closeConnection();
        return true;
    }
    
    public Object createModel(String schemaName) {
        return schemaManager.createMap(schemaName);
    }
    
    public ValidationResult validateModel(String schemaName, Object data) {
        return schemaManager.validate(schemaName, data);
    }
    
    public void validate(String schemaName, Object data) {
        ValidationResult vr = schemaManager.validate(schemaName, data);
        if(vr.hasErrors()) 
            throw new RuntimeException(vr.toString());
    }
    
    /**
     * add a map serializer also later.
     */
    public SchemaSerializer getSerializer() {
        return schemaManager.getSerializer();
    }
    

    public SchemaManager getSchemaManager() {
        return schemaManager;
    }
    
    public Aggregator getAggregator() {
        return new Aggregator(this);
    }
    
    public Indexer getIndexer() {
        return new Indexer(this);
    }
    
    
    /**
     * This is a generic save routine merged as one call.
     * create = if true will insert the record.
     * update = if true will update the record.
     * if create=true and update=false, this is insert only. you get the picture.
     */
    public Object save( String schemaName, Object data) {
        return save(schemaName, data, null);
    }
    
    public Object save( String schemaName, Object data, Map vars) {
        return save(schemaName, data,true,true,null);
    }
    
    public Object save( String schemaName, Object data, boolean create, boolean update ) {
        return save( schemaName, data, create, update, null);
    }
    
    public Object save( String schemaName, Object data, boolean create, boolean update, Map vars) {
        if(create==true && update==true) {
            Object test = read(schemaName, data, vars);
            if(test==null) {
                return create(schemaName, data, vars );
            }
            else {
                return update(schemaName, data, vars);
            }
        }
        else if(create==true  && update==false) {
            return create(schemaName, data, vars );
        }
        else if(create==false  && update==true) {
            Object test = read(schemaName, data, vars);
            if(test==null)
                throw new RuntimeException("Record for update does not exist");
            return update(schemaName, data, vars);
        }
        else {
            return data;
        }
    }

    public boolean isTransactionOpen() {
        return transactionOpen;
    }
    
    
    
}
