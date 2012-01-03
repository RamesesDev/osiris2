/*
 * DBScriptService.java
 * Created on December 21, 2011, 9:46 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import java.util.Map;

/**
 *
 * @author jzamss
 */
public interface DBMetaDataService {
    
    Map getTableTypes() throws Exception;
    Map getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws Exception;
    Map getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws Exception;
    
    
    Map getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws Exception;
    Map getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws Exception;
    
    Map getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws Exception;
    Map getVersionColumns(String catalog, String schema, String table) throws Exception;
    Map getPrimaryKeys(String catalog, String schema, String table) throws Exception;
    Map getImportedKeys(String catalog, String schema, String table) throws Exception;
    Map getExportedKeys(String catalog, String schema, String table) throws Exception;
    Map getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws Exception;
    Map getTypeInfo() throws Exception ;
    Map getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws Exception;
        
    
    Map getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws Exception;
    Map getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws Exception;
    Map getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws Exception;
    
    Map getSchemas() throws Exception;
    Map getCatalogs() throws Exception;
    
    Map getMetaData();
    
}
