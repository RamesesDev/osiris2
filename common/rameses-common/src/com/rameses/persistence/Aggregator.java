/*
 * Aggregator.java
 *
 * Created on September 1, 2010, 5:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.persistence;

import com.rameses.common.PropertyResolver;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.schema.SchemaUtil;
import com.rameses.schema.SimpleField;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * this class accepts the DefaultEntityManager
 * the aggregator is used for several functions that are common in data migration
 * modes are:
 *    insert replace
 *    insert ignore
 *    insert update
 */
public class Aggregator {
    
    private SchemaManager schemaManager;
    private EntityManager em;
    private BasicAggregatorHandler handler = new BasicAggregatorHandler();
    
    public Aggregator(EntityManager em) {
        this.em = em;
        this.schemaManager = em.getSchemaManager();
    }
    
    public Object execute(String schemaName, Map param)  {
        return execute(schemaName, param, null, true);
    }
    
    public Object execute(String schemaName, Map param, AggregateFilter filter)  {
        return execute(schemaName, param, filter, true);
    }
    
    public Object execute(String schemaName, Map param, AggregateFilter filter, boolean persist)  {
        try {
            Object targetModel = em.read( schemaName, param );
            if(filter!=null && !filter.accept(targetModel, param)) {
                return null;
            }
            SchemaElement element = schemaManager.getElement(schemaName);
            SqlContext sqlContext = em.getSqlContext();
            if(targetModel==null ) {
                targetModel = em.createModel(schemaName);
                NewFieldUpdater fieldUpdater = new NewFieldUpdater(schemaManager, sqlContext, targetModel);
                SchemaScanner scanner = schemaManager.newScanner();
                scanner.scan( element.getSchema(), element, param, fieldUpdater );
                if(persist) EntityManagerUtil.executeQueue(fieldUpdater.getQueue(),sqlContext,null,em.isTransactionOpen(),em.isDebug());
            } else {
                //create schema handler
                FieldUpdater fieldUpdater = new FieldUpdater(schemaManager, sqlContext, targetModel);
                SchemaScanner scanner = schemaManager.newScanner();
                scanner.scan( element.getSchema(), element, param, fieldUpdater );
                if(persist) EntityManagerUtil.executeQueue(fieldUpdater.getQueue(),sqlContext,null,em.isTransactionOpen(),em.isDebug());
            }
            return targetModel;
            
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    
    private boolean checkExcludes( String excludeScript, Map source, Map target ) {
        return false;
    }
    
    // <editor-fold defaultstate="collapsed" desc="NEW FIELD UPDATER">
    private class NewFieldUpdater extends CreatePersistenceHandler {
        private Object targetModel;
        private PropertyResolver resolver;
        
        public NewFieldUpdater( SchemaManager schemaManager, SqlContext ctx, Object targetModel) {
            super(schemaManager, ctx, targetModel );
            this.targetModel = targetModel;
            this.resolver = schemaManager.getConf().getPropertyResolver();
        }
        
        public void processField(SimpleField f, String refname, Object value) {
            if(!stack.empty()) {
                
                //we need to correct this. this might be a bug in the schema scanner.
                refname = status.getFixedFieldName( f );
                
                String aggtype = (String) f.getProperties().get("aggregate");
                if(aggtype!=null) {
                    if(aggtype.equalsIgnoreCase("count")) {
                        value = SchemaUtil.formatType(f,"1");
                    } else if(value==null) {
                        value = SchemaUtil.formatType(f,"0");
                    }
                }
                //if there are changes update it.
                DbElementContext dbec = stack.peek();
                String sname = dbec.correctName( f.getName() );
                SqlExecutor se = (SqlExecutor)dbec.getSqlTxn();
                
                //check if serializer exists
                Object rvalue = super.passSerializer(f, value, refname );
                se.setParameter( sname , rvalue );
                //update the target for returning
                resolver.setProperty( targetModel, refname, rvalue );
            }
        }
    }
    //</editor-fold>
    
    
    private class FieldUpdater extends UpdatePersistenceHandler {
        
        private Object targetModel;
        private PropertyResolver resolver;
        
        public FieldUpdater(SchemaManager schemaManager, SqlContext ctx, Object targetModel) {
            super(schemaManager, ctx, targetModel );
            this.targetModel = targetModel;
            this.resolver = schemaManager.getConf().getPropertyResolver();
        }
        
        public void processField(SimpleField f, String refname, Object value) {
            if(!stack.empty()) {
                
                refname = status.getFixedFieldName( f );
                
                //we need to determine first if field is updatable.
                boolean updatable = false;
                String _updatable = (String )f.getProperties().get("updatable");
                if( _updatable !=null ) {
                    try {  updatable = Boolean.parseBoolean(_updatable); }  catch(Exception e) {;}
                }
                String aggtype = (String) f.getProperties().get("aggregate");
                if(aggtype != null ) updatable = true;
                
                if( f.getProperties().get("primary")!=null) updatable = false;
                
                //get the old and new values.
                Object oldValue = resolver.getProperty( targetModel, refname );
                Object newValue = SchemaUtil.formatType( f, value );
                if( updatable ) {
                    Object testVal = (oldValue!=null) ? oldValue : newValue;
                    Class clazz = SchemaUtil.getFieldClass( f, testVal );
                    newValue = handler.compare(aggtype, clazz, oldValue, newValue, f.getProperties());
                } else {
                    newValue = oldValue;
                }
                
                //if there are changes update it.
                DbElementContext dbec = stack.peek();
                String sname = dbec.correctName( f.getName() );
                SqlExecutor se = (SqlExecutor)dbec.getSqlTxn();
                
                //check if serializer exists
                Object rvalue = super.passSerializer(f, newValue, refname );
                se.setParameter( sname , rvalue );
                //update the target for returning
                resolver.setProperty( targetModel, refname, rvalue );
            }
        }
    }
    
    
    
    public List<String> getAggregateFor( String schemaName ) {
        SchemaManager sm = em.getSchemaManager();
        SchemaElement e = em.getSchemaManager().getElement( schemaName );
        List<SchemaElement> elements = sm.lookup(schemaName, "aggregatefor",e.getName());
        String prefix = schemaName;
        if(prefix.indexOf(":")>0) prefix = prefix.substring(0, prefix.indexOf(":"));
        List<String> list = new ArrayList();
        for(SchemaElement se: elements) {
            list.add( prefix + ":" + se.getName() );
        }
        return list;
    }
    
    public void fire( List<String> aggregators, Map data ) {
        for(String s : aggregators) {
            execute( s, data );
        }
    }
    
}
