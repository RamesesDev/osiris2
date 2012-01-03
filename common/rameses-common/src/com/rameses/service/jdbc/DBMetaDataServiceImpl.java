/*
 * DefaultScriptService.java
 * Created on December 21, 2011, 9:51 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import com.rameses.service.ScriptServiceContext;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class DBMetaDataServiceImpl implements DBMetaDataService {
    
    private Map conf;
    private String serviceName = "DBMetaDataService";
    private DBMetaDataService metaService;
    
    /** Creates a new instance of DefaultScriptService */
    public DBMetaDataServiceImpl(Map c) {
        conf = c;
        ScriptServiceContext ssc = new ScriptServiceContext(conf);
        metaService = ssc.create(serviceName, DBMetaDataService.class);
    }
    
    public DBMetaDataServiceImpl(Map c, String serviceName) {
        conf = c;
        this.serviceName = serviceName;
        ScriptServiceContext ssc = new ScriptServiceContext(conf);
        metaService = ssc.create(serviceName, DBMetaDataService.class);
    }

    public Map getTableTypes() throws Exception {
        return metaService.getTableTypes();
    }

    public Map getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws Exception {
        return metaService.getTables(catalog, schemaPattern, tableNamePattern, types);
    }

    public Map getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws Exception {
        return metaService.getColumns(catalog,schemaPattern,tableNamePattern,columnNamePattern );
    }

    public Map getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws Exception {
        return metaService.getColumnPrivileges(catalog,schema,table,columnNamePattern );
    }

    public Map getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws Exception {
        return metaService.getTablePrivileges(catalog,schemaPattern,tableNamePattern );
    }

    public Map getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws Exception {
        return metaService.getBestRowIdentifier(catalog,schema,table,scope,nullable );
    }

    public Map getVersionColumns(String catalog, String schema, String table) throws Exception {
        return metaService.getVersionColumns(catalog,schema,table );
    }

    public Map getPrimaryKeys(String catalog, String schema, String table) throws Exception {
        return metaService.getPrimaryKeys(catalog,schema,table );
    }

    public Map getImportedKeys(String catalog, String schema, String table) throws Exception {
        return metaService.getImportedKeys(catalog,schema,table );
    }

    public Map getExportedKeys(String catalog, String schema, String table) throws Exception {
        return metaService.getExportedKeys(catalog,schema,table );
    }

    public Map getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws Exception {
        return metaService.getCrossReference(primaryCatalog,primarySchema,primaryTable, foreignTable,foreignSchema,foreignTable);
    }
    

    public Map getTypeInfo() throws Exception {
        return metaService.getTypeInfo();
    }

    public Map getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws Exception {
        return metaService.getIndexInfo(catalog,schema,table, unique,approximate);
    }

    public Map getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws Exception {
        return metaService.getSuperTypes(catalog,schemaPattern,typeNamePattern);
    }

    public Map getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws Exception {
        return metaService.getSuperTables(catalog,schemaPattern,tableNamePattern);
    }

    public Map getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws Exception {
        return metaService.getAttributes(catalog,schemaPattern,typeNamePattern, attributeNamePattern);
    }

    public Map getSchemas() throws Exception {
        return metaService.getSchemas();
    }

    public Map getCatalogs() throws Exception {
        return metaService.getCatalogs();
    }

    public Map getMetaData() {
        return metaService.getMetaData();
    }
    
   
    
}
