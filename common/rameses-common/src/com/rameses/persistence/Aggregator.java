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
import com.rameses.schema.DefaultSchemaHandler;
import com.rameses.schema.SchemaElement;
import com.rameses.schema.SchemaManager;
import com.rameses.schema.SchemaScanner;
import com.rameses.schema.SchemaUtil;
import com.rameses.schema.SimpleField;
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
    
    private static final String INSERT_REPLACE = "INSERT_REPLACE";
    private static final String INSERT_IGNORE = "INSERT_IGNORE";
    private static final String INSERT_UPDATE = "INSERT_UPDATE";
    
    private String mode = INSERT_UPDATE;
    private SchemaManager schemaManager;
    private DefaultEntityManager em;
    private AggregateHandler handler = new BasicAggregatorHandler();
    
    public Aggregator(DefaultEntityManager em) {
        this.em = em;
        this.schemaManager = em.getSchemaManager();
    }
    
    
    public Object execute(String schemaName, Map param) {
        Object targetModel = em.read( schemaName, param );
        if(targetModel==null || mode.equalsIgnoreCase(INSERT_REPLACE) ) {
            targetModel = em.createModel(schemaName);
            
            SchemaElement element = schemaManager.getElement(schemaName);
            NewFieldUpdater fieldUpdater = new NewFieldUpdater(param, targetModel, schemaManager.getConf().getPropertyResolver());
            SchemaScanner scanner = schemaManager.newScanner();
            scanner.scan( element.getSchema(), element, param, fieldUpdater );
            
            if(!handler.proceed(targetModel,param)) return targetModel;
            return em.create( schemaName, targetModel );
        } 
        
        else {
            if(!handler.proceed(targetModel,param)) return targetModel;
            if(mode.equalsIgnoreCase(INSERT_IGNORE)) return targetModel;
            //create schema handler
            SchemaElement element = schemaManager.getElement(schemaName);
            FieldUpdater fieldUpdater = new FieldUpdater(param, targetModel, schemaManager.getConf().getPropertyResolver());
            SchemaScanner scanner = schemaManager.newScanner();
            scanner.scan( element.getSchema(), element, param, fieldUpdater );
            return em.update( schemaName, targetModel );
        }
    }
    
    
    private boolean checkExcludes( String excludeScript, Map source, Map target ) {
        return false;
    }
    
    private class NewFieldUpdater extends DefaultSchemaHandler {
        private Object targetModel;
        private Object sourceModel;
        private PropertyResolver resolver;
        
        public NewFieldUpdater(Object sourceModel, Object targetModel, PropertyResolver resolver) {
            this.sourceModel = sourceModel;
            this.targetModel = targetModel;
            this.resolver = resolver;
        }
        
        public void processField(SimpleField f, String refname, Object value) {
            refname = status.getFixedFieldName( f );
            String srcfield = (String)f.getProperties().get( "sourcefield" );
            if(srcfield!=null) {
                value = resolver.getProperty(sourceModel, srcfield);
            }
            
            String aggtype = (String) f.getProperties().get("aggregate");
            if(aggtype!=null) {
                if(aggtype.equalsIgnoreCase("count")) {
                    value = SchemaUtil.formatType(f,"1");
                }
                else if(value==null) {
                    value = SchemaUtil.formatType(f,"0");
                }
            }
            
            resolver.setProperty( targetModel,refname,value );
        }
    }
    
    private class FieldUpdater extends DefaultSchemaHandler {
        private Object targetModel;
        private Object sourceModel;
        private PropertyResolver resolver;
        
        public FieldUpdater(Object sourceModel, Object targetModel, PropertyResolver resolver) {
            this.sourceModel = sourceModel;
            this.targetModel = targetModel;
            this.resolver = resolver;
        }
        
        public void processField(SimpleField f, String refname, Object value) {
            if(f.getProperties().get("primary")!=null) return;
            
            //we need to correct this. this might be a bug in the schema scanner.
            refname = status.getFixedFieldName( f );

            String srcfield = (String)f.getProperties().get( "sourcefield" );
            if(srcfield!=null) {
                value = resolver.getProperty(sourceModel, srcfield);
            }
            String aggtype = (String) f.getProperties().get("aggregate");
            Object newValue = SchemaUtil.formatType( f, value );
            Object oldValue = SchemaUtil.formatType( f, resolver.getProperty( targetModel, refname ) );
            
            Object testVal = (oldValue!=null) ? oldValue : newValue;
            Class clazz = SchemaUtil.getFieldClass( f, testVal );
            
            newValue = handler.compare(aggtype, clazz, oldValue, newValue, f.getProperties());
            if(   (newValue==null && oldValue!=null) ||
                    newValue!=null && !newValue.equals(oldValue)) {
                resolver.setProperty( targetModel, refname, newValue );
            }
        }
    }
    
    public String getMode() {
        return mode;
    }
    
    public void setMode(String mode) {
        this.mode = mode;
    }
    
    
    /**
     * looks up schema for elements marked as aggregatefor
     */
    public void fire(String schemaName, Object data) {
        SchemaElement e = em.getSchemaManager().getElement( schemaName );
        fire( schemaName, "aggregatefor", e.getName(), data ); 
    }

    public void fire(String schemaName, String attr, String attrValue, Object data) {
        SchemaManager sm = em.getSchemaManager();
        List<SchemaElement> elements = sm.lookup(schemaName, attr,attrValue);
        String prefix = schemaName;
        if(prefix.indexOf(":")>0) prefix = prefix.substring(0, prefix.indexOf(":"));
        for(SchemaElement se: elements) {
            em.create( prefix + ":" + se.getName(), data );
        }
    }
    
    
}
