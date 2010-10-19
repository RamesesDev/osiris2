/*
 * SchemaConf.java
 *
 * Created on August 14, 2010, 6:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.schema;

import com.rameses.common.ExpressionResolver;
import com.rameses.common.PropertyResolver;
import com.sun.jmx.remote.util.Service;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author elmo
 */
public class SchemaConf {
    
    public final static String XML_SCHEMA_EXT = ".xml";
    public final static String XML_SCHEMA = "schema";
    
    private PropertyResolver propertyResolver;
    private SchemaResourceProvider resourceProvider = new DefaultSchemaResourceProvider();
    
    private SchemaScriptProvider scriptProvider;

    private List<SchemaProvider> providers ;
    private SchemaManager schemaManager;
    private SchemaSerializer serializer;
    private ExpressionResolver expressionResolver;
    
    /** Creates a new instance of SchemaConf */
    public SchemaConf(SchemaManager sm) {
        this.schemaManager = sm;
    }
    
    public PropertyResolver getPropertyResolver() {
        return propertyResolver;
    }
    
    public void setPropertyResolver(PropertyResolver propertyResolver) {
        this.propertyResolver = propertyResolver;
    }
    
    public SchemaResourceProvider getResourceProvider() {
        return resourceProvider;
    }
    
    public void setResourceProvider(SchemaResourceProvider resourceProvider) {
        this.resourceProvider = resourceProvider;
    }
    
    public List<SchemaProvider> getProviders() {
        if(providers==null) {
            providers = new ArrayList();
            Iterator iter = Service.providers(SchemaProvider.class, Thread.currentThread().getContextClassLoader());
            while(iter.hasNext()) {
                SchemaProvider sp = (SchemaProvider)iter.next();
                sp.setSchemaManager( schemaManager );
                providers.add( sp );
            }
            //add the default schema def provider
            SchemaProvider d = new XmlSchemaProvider();
            d.setSchemaManager(schemaManager);
            providers.add(d);
        }
        return providers;
    }
    
    public void destroy() {
        propertyResolver = null;
        resourceProvider = null;
        if(providers!=null) providers.clear();
        scriptProvider = null;
        schemaManager = null;
        serializer = null;
        expressionResolver = null;
    }

    public SchemaScriptProvider getScriptProvider() {
        return scriptProvider;
    }

    public void setScriptProvider(SchemaScriptProvider scriptProvider) {
        this.scriptProvider = scriptProvider;
    }

    public SchemaSerializer getSerializer() {
        return serializer;
    }

    public void setSerializer(SchemaSerializer serializer) {
        this.serializer = serializer;
    }

    public ExpressionResolver getExpressionResolver() {
        return expressionResolver;
    }

    public void setExpressionResolver(ExpressionResolver expressionResolver) {
        this.expressionResolver = expressionResolver;
    }
    
}
