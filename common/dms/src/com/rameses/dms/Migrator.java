package com.rameses.dms;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Map;
import javax.sql.DataSource;

/**
 * This class migrates the data based on the schema definition
 */
public class Migrator {
    
    private DataSet dataset;
    private MigrationListener listener = new DataTransferListener();
    private DataSource sourceDS;
    private DataSource targetDS;
    private SqlDialect sourceDialect;
    private SqlDialect targetDialect;
    private Connection sourceConn = null;
    private Connection targetConn = null;
    private String defaultDialect = "mysql";
    private PrintWriter logger = new PrintWriter(System.out);
    
    public Migrator(DataSet ds) {
        if(ds==null)
            throw new IllegalStateException("Migrator error dataSet must nto be null");
        this.dataset = ds;
        //check the source dialect of the dataset
        String srcDialect = ds.getSchema().getSourcedialect();
        if( srcDialect ==null ) srcDialect = defaultDialect;
        sourceDialect = SchemaManager.getDialect(srcDialect);
        
        String tgtDialect = ds.getSchema().getTargetdialect();
        if( tgtDialect ==null ) tgtDialect = defaultDialect;
        targetDialect = SchemaManager.getDialect(tgtDialect);
    }
    
    public void execute() throws Exception {
        if( sourceDS == null )
            throw new Exception("Source DS must be set");
        if( targetDS == null )
            throw new Exception("Target DS must be set");
        if( sourceDialect == null )
            throw new Exception("Source Dialect must be set");
        if( targetDialect == null )
            throw new Exception("Target Dialect must be set");
        try {
            sourceConn = sourceDS.getConnection();
            targetConn = targetDS.getConnection();
            targetDialect.createTarget( dataset.getTableInstance(), targetConn );
            sourceDialect.fetchSourceList( dataset.getTableInstance(), sourceConn, listener );
        } catch(Exception e) {
            throw e;
        } finally {
            try {sourceConn.close();} catch(Exception ign){;}
            try {targetConn.close();} catch(Exception ign){;}
        }
    }
    
    public class DataTransferListener implements MigrationListener {
        
        private TableInstance tableInstance;
        private PreparedStatement ps;
        
        public void start(TableInstance t) {
            try {
                tableInstance = t;
                String sql = targetDialect.getInsertSql(t);
                ps = targetConn.prepareStatement(sql);
            } catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        
        public void startBatch(int start, int batchSize) {
            
        }
        
        public void fetchRow(Object o) {
            try {
                targetDialect.fillParameters(ps, (Map)o, tableInstance.getFieldNames());
                ps.addBatch();
            } catch(Exception ex) {
                logger.print( ex.getMessage() );
                throw new IllegalStateException(ex);
            }
        }
        
        public void endBatch() {
            try {
                ps.executeBatch();
            } catch(Exception ex) {
                logger.print("ERROR " + ex.getMessage());
                throw new IllegalStateException(ex);
            }
        }
        
        public void stop() {
            try { ps.close(); } 
            catch(Exception e){;}
        }
    }
    
    public DataSource getSourceDS() {
        return sourceDS;
    }
    
    public void setSourceDS(DataSource sourceDS) {
        this.sourceDS = sourceDS;
    }
    
    public DataSource getTargetDS() {
        return targetDS;
    }
    
    public void setTargetDS(DataSource targetDS) {
        this.targetDS = targetDS;
    }

    public String getDefaultDialect() {
        return defaultDialect;
    }

    public void setDefaultDialect(String defaultDialect) {
        this.defaultDialect = defaultDialect;
    }

    public PrintWriter getLogger() {
        return logger;
    }

    public void setLogger(PrintWriter logger) {
        this.logger = logger;
    }

   
    
}
