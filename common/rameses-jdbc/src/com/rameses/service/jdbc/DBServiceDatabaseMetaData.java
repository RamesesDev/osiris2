/*
 * DBServiceDatabaseMetaData.java
 * Created on December 29, 2011, 9:10 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author jzamss
 */
public class DBServiceDatabaseMetaData implements DatabaseMetaData {
    
    private String serviceName = "DBMetaDataService";
    private DBServiceConnection conn;
    private Map conf;
    private Map meta;
    private DBMetaDataService metaService;
    
    DBServiceDatabaseMetaData(DBServiceConnection c, Map conf) {
        this.conn = c;
        this.conf = conf;
        this.metaService = new DBMetaDataServiceImpl(conf);
    }
    
    private boolean getBoolean(String n) {
        System.out.println("getBoolean " + n);
        if( meta == null ) {
            meta = metaService.getMetaData();
        }
        if( !meta.containsKey(n) ) return false;
        return Boolean.parseBoolean( meta.get(n)+"" );
    }
    
    private String getString(String n) {
        System.out.println( "getString " + n);
        if( meta == null ) {
            meta = metaService.getMetaData();
        }
        if( !meta.containsKey(n) ) return null;
        return meta.get(n)+"";
    }
    
    private int getInt(String n) {
        System.out.println( "getInt " + n);
        if( meta == null ) {
            meta = metaService.getMetaData();
        }
        if( !meta.containsKey(n) ) return 0;
        return Integer.parseInt(meta.get(n)+"");
    }
    
    public boolean allProceduresAreCallable() throws SQLException {
        return getBoolean("allProceduresAreCallable");
    }
    
    public boolean allTablesAreSelectable() throws SQLException {
        return getBoolean("allTablesAreSelectable");
    }
    
    public String getURL() throws SQLException {
        return getString("url");
    }
    
    public String getUserName() throws SQLException {
        return getString("userName");
    }
    
    public boolean isReadOnly() throws SQLException {
        return getBoolean("readOnly");
    }
    
    public boolean nullsAreSortedHigh() throws SQLException {
        return getBoolean("nullsAreSortedHigh");
    }
    
    public boolean nullsAreSortedLow() throws SQLException {
        return getBoolean("nullsAreSortedLow");
    }
    
    public boolean nullsAreSortedAtStart() throws SQLException {
        return getBoolean("nullsAreSortedAtStart");
    }
    
    public boolean nullsAreSortedAtEnd() throws SQLException {
        return getBoolean("nullsAreSortedAtEnd");
    }
    
    public String getDatabaseProductName() throws SQLException {
        return getString("databaseProductName");
    }
    
    public String getDatabaseProductVersion() throws SQLException {
        return getString("databaseProductVersion");
    }
    
    public String getDriverName() throws SQLException {
        return getString("driverName");
    }
    
    public String getDriverVersion() throws SQLException {
        return getString("driverVersion");
    }
    
    public int getDriverMajorVersion() {
        return getInt("driverMajorVersion");
    }
    
    public int getDriverMinorVersion() {
        return getInt("driverMinorVersion");
    }
    
    public boolean usesLocalFiles() throws SQLException {
        return getBoolean("usesLocalFiles");
    }
    
    public boolean usesLocalFilePerTable() throws SQLException {
        return getBoolean("usesLocalFilePerTable");
    }
    
    public boolean supportsMixedCaseIdentifiers() throws SQLException {
        return getBoolean("supportsMixedCaseIdentifiers");
    }
    
    public boolean storesUpperCaseIdentifiers() throws SQLException {
        return getBoolean("storesUpperCaseIdentifiers");
    }
    
    public boolean storesLowerCaseIdentifiers() throws SQLException {
        return getBoolean("storesLowerCaseIdentifiers");
    }
    
    public boolean storesMixedCaseIdentifiers() throws SQLException {
        return getBoolean("storesMixedCaseIdentifiers");
    }
    
    public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
        return getBoolean("supportsMixedCaseQuotedIdentifiers");
    }
    
    public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
        return getBoolean("storesUpperCaseQuotedIdentifiers");
    }
    
    public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
        return getBoolean("storesLowerCaseQuotedIdentifiers");
    }
    
    public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
        return getBoolean("storesMixedCaseQuotedIdentifiers");
    }
    
    public String getIdentifierQuoteString() throws SQLException {
        return getString("identifierQuoteString");
    }
    
    public String getSQLKeywords() throws SQLException {
        return getString("sQLKeywords");
    }
    
    public String getNumericFunctions() throws SQLException {
        return getString("numericFunctions");
    }
    
    public String getStringFunctions() throws SQLException {
        return getString("stringFunctions");
    }
    
    public String getSystemFunctions() throws SQLException {
        return getString("systemFunctions");
    }
    
    public String getTimeDateFunctions() throws SQLException {
        return getString("timeDateFunctions");
    }
    
    public String getSearchStringEscape() throws SQLException {
        return getString("searchStringEscape");
    }
    
    public String getExtraNameCharacters() throws SQLException {
        return getString("extraNameCharacters");
    }
    
    public boolean supportsAlterTableWithAddColumn() throws SQLException {
        return getBoolean("supportsAlterTableWithAddColumn");
    }
    
    public boolean supportsAlterTableWithDropColumn() throws SQLException {
        return getBoolean("supportsAlterTableWithDropColumn");
    }
    
    public boolean supportsColumnAliasing() throws SQLException {
        return getBoolean("supportsColumnAliasing");
    }
    
    public boolean nullPlusNonNullIsNull() throws SQLException {
        return getBoolean("nullPlusNonNullIsNull");
    }
    
    public boolean supportsConvert() throws SQLException {
        return getBoolean("supportsConvert");
    }
    
    public boolean supportsConvert(int fromType, int toType) throws SQLException {
        JOptionPane.showMessageDialog(null,"supportsConvert" );
        throw new SQLException("supportsConvert not supported");
    }
    
    public boolean supportsTableCorrelationNames() throws SQLException {
        return getBoolean("supportsTableCorrelationNames");
    }
    
    public boolean supportsDifferentTableCorrelationNames() throws SQLException {
        return getBoolean("supportsDifferentTableCorrelationNames");
    }
    
    public boolean supportsExpressionsInOrderBy() throws SQLException {
        return getBoolean("supportsExpressionsInOrderBy");
    }
    
    public boolean supportsOrderByUnrelated() throws SQLException {
        return getBoolean("supportsOrderByUnrelated");
    }
    
    public boolean supportsGroupBy() throws SQLException {
        return getBoolean("supportsGroupBy");
    }
    
    public boolean supportsGroupByUnrelated() throws SQLException {
        return getBoolean("supportsGroupByUnrelated");
    }
    
    public boolean supportsGroupByBeyondSelect() throws SQLException {
        return getBoolean("supportsGroupByBeyondSelect");
    }
    
    public boolean supportsLikeEscapeClause() throws SQLException {
        return getBoolean("supportsLikeEscapeClause");
    }
    
    public boolean supportsMultipleResultSets() throws SQLException {
        return getBoolean("supportsMultipleResultSets");
    }
    
    public boolean supportsMultipleTransactions() throws SQLException {
        return getBoolean("supportsMultipleTransactions");
    }
    
    public boolean supportsNonNullableColumns() throws SQLException {
        return getBoolean("supportsNonNullableColumns");
    }
    
    public boolean supportsMinimumSQLGrammar() throws SQLException {
        return getBoolean("supportsMinimumSQLGrammar");
    }
    
    public boolean supportsCoreSQLGrammar() throws SQLException {
        return getBoolean("supportsCoreSQLGrammar");
    }
    
    public boolean supportsExtendedSQLGrammar() throws SQLException {
        return getBoolean("supportsExtendedSQLGrammar");
    }
    
    public boolean supportsANSI92EntryLevelSQL() throws SQLException {
        return getBoolean("supportsANSI92EntryLevelSQL");
    }
    
    public boolean supportsANSI92IntermediateSQL() throws SQLException {
        return getBoolean("supportsANSI92IntermediateSQL");
    }
    
    public boolean supportsANSI92FullSQL() throws SQLException {
        return getBoolean("supportsANSI92FullSQL");
    }
    
    public boolean supportsIntegrityEnhancementFacility() throws SQLException {
        return getBoolean("supportsIntegrityEnhancementFacility");
    }
    
    public boolean supportsOuterJoins() throws SQLException {
        return getBoolean("supportsOuterJoins");
    }
    
    public boolean supportsFullOuterJoins() throws SQLException {
        return getBoolean("supportsFullOuterJoins");
    }
    
    public boolean supportsLimitedOuterJoins() throws SQLException {
        return getBoolean("supportsLimitedOuterJoins");
    }
    
    public String getSchemaTerm() throws SQLException {
        return getString("schemaTerm");
    }
    
    public String getProcedureTerm() throws SQLException {
        return getString("procedureTerm");
    }
    
    public String getCatalogTerm() throws SQLException {
        return getString("catalogTerm");
    }
    
    public boolean isCatalogAtStart() throws SQLException {
        return getBoolean("catalogAtStart");
    }
    
    public String getCatalogSeparator() throws SQLException {
        return getString("catalogSeparator");
    }
    
    public boolean supportsSchemasInDataManipulation() throws SQLException {
        return getBoolean("supportsSchemasInDataManipulation");
    }
    
    public boolean supportsSchemasInProcedureCalls() throws SQLException {
        return getBoolean("supportsSchemasInProcedureCalls");
    }
    
    public boolean supportsSchemasInTableDefinitions() throws SQLException {
        return getBoolean("supportsSchemasInTableDefinitions");
    }
    
    public boolean supportsSchemasInIndexDefinitions() throws SQLException {
        return getBoolean("supportsSchemasInIndexDefinitions");
    }
    
    public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
        return getBoolean("supportsSchemasInPrivilegeDefinitions");
    }
    
    public boolean supportsCatalogsInDataManipulation() throws SQLException {
        return getBoolean("supportsCatalogsInDataManipulation");
    }
    
    public boolean supportsCatalogsInProcedureCalls() throws SQLException {
        return getBoolean("supportsCatalogsInProcedureCalls");
    }
    
    public boolean supportsCatalogsInTableDefinitions() throws SQLException {
        return getBoolean("supportsCatalogsInTableDefinitions");
    }
    
    public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
        return getBoolean("supportsCatalogsInIndexDefinitions");
    }
    
    public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
        return getBoolean("supportsCatalogsInPrivilegeDefinitions");
    }
    
    public boolean supportsPositionedDelete() throws SQLException {
        return getBoolean("supportsPositionedDelete");
    }
    
    public boolean supportsPositionedUpdate() throws SQLException {
        return getBoolean("supportsPositionedUpdate");
    }
    
    public boolean supportsSelectForUpdate() throws SQLException {
        return getBoolean("supportsSelectForUpdate");
    }
    
    public boolean supportsStoredProcedures() throws SQLException {
        return getBoolean("supportsStoredProcedures");
    }
    
    public boolean supportsSubqueriesInComparisons() throws SQLException {
        return getBoolean("supportsSubqueriesInComparisons");
    }
    
    public boolean supportsSubqueriesInExists() throws SQLException {
        return getBoolean("supportsSubqueriesInExists");
    }
    
    public boolean supportsSubqueriesInIns() throws SQLException {
        return getBoolean("supportsSubqueriesInIns");
    }
    
    public boolean supportsSubqueriesInQuantifieds() throws SQLException {
        return getBoolean("supportsSubqueriesInQuantifieds");
    }
    
    public boolean supportsCorrelatedSubqueries() throws SQLException {
        return getBoolean("supportsCorrelatedSubqueries");
    }
    
    public boolean supportsUnion() throws SQLException {
        return getBoolean("supportsUnion");
    }
    
    public boolean supportsUnionAll() throws SQLException {
        return getBoolean("supportsUnionAll");
    }
    
    public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
        return getBoolean("supportsOpenCursorsAcrossCommit");
    }
    
    public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
        return getBoolean("supportsOpenCursorsAcrossRollback");
    }
    
    public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
        return getBoolean("supportsOpenStatementsAcrossCommit");
    }
    
    public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
        return getBoolean("supportsOpenStatementsAcrossRollback");
    }
    
    public int getMaxBinaryLiteralLength() throws SQLException {
        return getInt("maxBinaryLiteralLength");
    }
    
    public int getMaxCharLiteralLength() throws SQLException {
        return getInt("maxCharLiteralLength");
    }
    
    public int getMaxColumnNameLength() throws SQLException {
        return getInt("maxColumnNameLength");
    }
    
    public int getMaxColumnsInGroupBy() throws SQLException {
        return getInt("maxColumnsInGroupBy");
    }
    
    public int getMaxColumnsInIndex() throws SQLException {
        return getInt("maxColumnsInIndex");
    }
    
    public int getMaxColumnsInOrderBy() throws SQLException {
        return getInt("maxColumnsInOrderBy");
    }
    
    public int getMaxColumnsInSelect() throws SQLException {
        return getInt("maxColumnsInSelect");
    }
    
    public int getMaxColumnsInTable() throws SQLException {
        return getInt("MaxColumnsInTable");
    }
    
    public int getMaxConnections() throws SQLException {
        return getInt("maxConnections");
    }
    
    public int getMaxCursorNameLength() throws SQLException {
        return getInt("maxCursorNameLength");
    }
    
    public int getMaxIndexLength() throws SQLException {
        return getInt("maxIndexLength");
    }
    
    public int getMaxSchemaNameLength() throws SQLException {
        return getInt("maxSchemaNameLength");
    }
    
    public int getMaxProcedureNameLength() throws SQLException {
        return getInt("maxProcedureNameLength");
    }
    
    public int getMaxCatalogNameLength() throws SQLException {
        return getInt("maxCatalogNameLength");
    }
    
    public int getMaxRowSize() throws SQLException {
        return getInt("maxRowSize");
    }
    
    public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
        return getBoolean("doesMaxRowSizeIncludeBlobs");
    }
    
    public int getMaxStatementLength() throws SQLException {
        return getInt("maxStatementLength");
    }
    
    public int getMaxStatements() throws SQLException {
        return getInt("maxStatements");
    }
    
    public int getMaxTableNameLength() throws SQLException {
        return getInt("maxTableNameLength");
    }
    
    public int getMaxTablesInSelect() throws SQLException {
        return getInt("maxTablesInSelect");
    }
    
    public int getMaxUserNameLength() throws SQLException {
        return getInt("maxUserNameLength");
    }
    
    public int getDefaultTransactionIsolation() throws SQLException {
        return getInt("defaultTransactionIsolation");
    }
    
    public boolean supportsTransactions() throws SQLException {
        return getBoolean("supportsTransactions");
    }
    
    public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
        JOptionPane.showMessageDialog(null,"supportsTransactionIsolationLevel" );
        throw new SQLException("supportsTransactionIsolationLevel not supported");
    }
    
    public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
        return getBoolean("supportsDataDefinitionAndDataManipulationTransactions");
    }
    
    public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
        return getBoolean("supportsDataManipulationTransactionsOnly");
    }
    
    public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
        return getBoolean("dataDefinitionCausesTransactionCommit");
    }
    
    public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
        return getBoolean("dataDefinitionIgnoredInTransactions");
    }
    
    public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
        JOptionPane.showMessageDialog(null, "getProcedures");
        throw new SQLException("not supported");
    }
    
    public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
        JOptionPane.showMessageDialog(null, "getProcedureColumns");
        throw new SQLException("not supported");
    }
    
    public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
        try {
            Map m = metaService.getTables(catalog,schemaPattern,tableNamePattern,types);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getSchemas() throws SQLException {
        try {
            Map m = metaService.getSchemas();
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getCatalogs() throws SQLException {
        try {
            Map m = metaService.getCatalogs();
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getTableTypes() throws SQLException {
        try {
            Map m = metaService.getTableTypes();
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
        try {
            Map m = metaService.getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern );
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
        try {
            Map m = metaService.getColumnPrivileges( catalog,schema,table,columnNamePattern);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        try {
            Map m = metaService.getTablePrivileges(catalog, schemaPattern,tableNamePattern);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
        try {
            Map m = metaService.getBestRowIdentifier(catalog, schema, table,scope, nullable);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
        try {
            Map m = metaService.getVersionColumns(catalog, schema, table);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
        try {
            Map m = metaService.getPrimaryKeys(catalog, schema, table);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
        try {
            Map m = metaService.getImportedKeys(catalog, schema, table);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
        try {
            Map m = metaService.getExportedKeys(catalog, schema, table);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
        try {
            Map m = metaService.getCrossReference(primaryCatalog, primarySchema, primaryTable, foreignCatalog,foreignSchema, foreignTable);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getTypeInfo() throws SQLException {
        try {
            Map m = metaService.getTypeInfo();
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getIndexInfo(String catalog, String schema, String table, boolean unique, boolean approximate) throws SQLException {
        try {
            Map m = metaService.getIndexInfo(catalog, schema, table,  unique, approximate);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public boolean supportsResultSetType(int type) throws SQLException {
        JOptionPane.showMessageDialog(null, "supportsResultSetType");
        throw new SQLException("supportsResultSetType not supported");
    }
    
    public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
        JOptionPane.showMessageDialog(null, "supportsResultSetConcurrency");
        throw new SQLException("supportsResultSetConcurrency not supported");
    }
    
    public boolean ownUpdatesAreVisible(int type) throws SQLException {
        JOptionPane.showMessageDialog(null, "ownUpdatesAreVisible");
        throw new SQLException("ownUpdatesAreVisible not supported");
    }
    
    public boolean ownDeletesAreVisible(int type) throws SQLException {
        JOptionPane.showMessageDialog(null, "ownDeletesAreVisible");
        throw new SQLException("ownDeletesAreVisible not supported");
    }
    
    public boolean ownInsertsAreVisible(int type) throws SQLException {
         JOptionPane.showMessageDialog(null, "ownInsertsAreVisible");
        throw new SQLException("ownInsertsAreVisible not supported");
    }
    
    public boolean othersUpdatesAreVisible(int type) throws SQLException {
          JOptionPane.showMessageDialog(null, "othersUpdatesAreVisible");
        throw new SQLException("othersUpdatesAreVisible not supported");
    }
    
    public boolean othersDeletesAreVisible(int type) throws SQLException {
          JOptionPane.showMessageDialog(null, "othersDeletesAreVisible");
        throw new SQLException("othersDeletesAreVisible not supported");
    }
    
    public boolean othersInsertsAreVisible(int type) throws SQLException {
          JOptionPane.showMessageDialog(null, "othersInsertsAreVisible");
        throw new SQLException("othersInsertsAreVisible not supported");
    }
    
    public boolean updatesAreDetected(int type) throws SQLException {
         JOptionPane.showMessageDialog(null, "updatesAreDetected");
        throw new SQLException("updatesAreDetected not supported");
    }
    
    public boolean deletesAreDetected(int type) throws SQLException {
          JOptionPane.showMessageDialog(null, "deletesAreDetected");
        throw new SQLException("deletesAreDetected not supported");
    }
    
    public boolean insertsAreDetected(int type) throws SQLException {
          JOptionPane.showMessageDialog(null, "insertsAreDetected");
        return getBoolean("supportsBatchUpdates");
    }
    
    public boolean supportsBatchUpdates() throws SQLException {
        return getBoolean("supportsBatchUpdates");
    }
    
    public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
        JOptionPane.showMessageDialog(null, "getUDTs");
        throw new SQLException("not supported");
    }
    
    public Connection getConnection() throws SQLException {
        return conn;
    }
    
    public boolean supportsSavepoints() throws SQLException {
        return getBoolean("supportsSavepoints");
    }
    
    public boolean supportsNamedParameters() throws SQLException {
        return getBoolean("supportsNamedParameters");
    }
    
    public boolean supportsMultipleOpenResults() throws SQLException {
        return getBoolean("supportsMultipleOpenResults");
    }
    
    public boolean supportsGetGeneratedKeys() throws SQLException {
        return getBoolean("supportsGetGeneratedKeys");
    }
    
    public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
        try {
            Map m = metaService.getSuperTypes(catalog, schemaPattern, typeNamePattern);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
        try {
            Map m = metaService.getSuperTables(catalog, schemaPattern, tableNamePattern);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
        try {
            Map m = metaService.getAttributes(catalog, schemaPattern, typeNamePattern, attributeNamePattern);
            return new DBServiceResultSet(m);
        } catch(Exception ign) {
            throw new SQLException(ign.getMessage());
        }
    }
    
    public boolean supportsResultSetHoldability(int holdability) throws SQLException {
        throw new SQLException("supportsResultSetHoldability not supported");
    }
    
    public int getResultSetHoldability() throws SQLException {
        return getInt("resultSetHoldability");
    }
    
    public int getDatabaseMajorVersion() throws SQLException {
        return getInt("databaseMajorVersion");
    }
    
    public int getDatabaseMinorVersion() throws SQLException {
        return getInt("databaseMinorVersion");
    }
    
    public int getJDBCMajorVersion() throws SQLException {
        return getInt("jDBCMajorVersion");
    }
    
    public int getJDBCMinorVersion() throws SQLException {
        return getInt("jDBCMinorVersion");
    }
    
    public int getSQLStateType() throws SQLException {
        return getInt("sQLStateType");
    }
    
    public boolean locatorsUpdateCopy() throws SQLException {
        return getBoolean("locatorsUpdateCopy");
    }
    
    public boolean supportsStatementPooling() throws SQLException {
        return getBoolean("supportsStatementPooling");
    }
    
}
