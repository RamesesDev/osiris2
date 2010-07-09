package com.rameses.dms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

public abstract class SqlDialect {
    
    private DataSource datasource;
    
    //identify first if SqlDialect is source or target...
    public SqlDialect() {
        
    }
    
    public abstract String getName();
    public abstract String getDataType(DataType d);
    public abstract String getCreateTable(TableInstance t);
    public abstract String getDropTable(TableInstance t);
    public abstract String getSourceListSql(TableInstance t);
    public abstract String getTargetListSql(TableInstance t);
    public abstract String getInsertSql(TableInstance t);
    
    //overridable
    public boolean isPagingSupported() {
        return false;
    }
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    
    //Overridable
    public DataSource getDatasource() {
        return datasource;
    }
    
    public void setDatasource(DataSource datasource) {
        this.datasource = datasource;
    }
    //</editor-fold>
    
    public void createTarget(TableInstance t, Connection conn) throws Exception {
        //execute
        PreparedStatement ps = null;
        try {
            String sql = this.getCreateTable(t);
            ps = conn.prepareStatement(sql);
            ps.execute();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {ps.close();} catch(Exception ign){;}
        }
    }
    
    public void dropTarget(TableInstance t, Connection conn) throws Exception {
        //execute
        PreparedStatement ps = null;
        try {
            String sql = this.getDropTable(t);
            ps = conn.prepareStatement(sql);
            ps.execute();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {ps.close();} catch(Exception ign){;}
        }
    }
    
    private Map createMapRecord(ResultSet rs, List<Field> fields,boolean isSource) throws Exception {
        Map map = new HashMap();
        for(Field f : fields) {
            String fldName = (isSource)?f.getSourcefield():f.getFieldname();
            map.put(f.getName(), rs.getObject(fldName));
        }
        return map;
    }
    
    
    public void fillParameters( PreparedStatement ps, Map params, List<String> parameterNames ) throws Exception {
        int startIndex = 1;
        for(String pName: parameterNames) {
            ps.setObject(startIndex++, params.get(pName));
        }
    }
    
    public void fetchSourceList(TableInstance t, Connection conn, MigrationListener ml) throws Exception {
        fetchList(t,conn,ml,true);
    }
    
    public void fetchTargetList(TableInstance t, Connection conn,MigrationListener ml) throws Exception {
        fetchList(t,conn,ml,false);
    }
    
    private void fetchList(TableInstance t, Connection conn, MigrationListener listener, boolean isSource) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            listener.start( t );
            String sql = (isSource) ? this.getSourceListSql(t) : this.getTargetListSql(t);
            //we need to fix the sql to substitute $P parameters with ?
            
            List<String> parameterNames = new ArrayList<String>();
            StringBuffer sb = new StringBuffer();
            SqlUtil.parseStatement(sql, sb, parameterNames );
            sql = sb.toString();

            int batchSize = t.getBatchSize();

            ps = conn.prepareStatement(sql);
            
            //if the sql dialect supports paging in sql, pagingParams is not null
            if(isPagingSupported()) {
                System.out.println("support paging");
                int start = 0;
                //loop batch
                boolean hasMoreRecords = true;
                while(hasMoreRecords) {
                    Map params = new HashMap();
                    params.putAll( t.getParams() );
                    params.put("start", start);
                    params.put("limit", batchSize);
                    fillParameters( ps, params, parameterNames );
                    rs = ps.executeQuery();
                    listener.startBatch(start, batchSize);
                    hasMoreRecords = false;
                    int counter = 0;
                    while(rs.next()) {
                        hasMoreRecords = true;
                        counter++;
                        listener.fetchRow( createMapRecord(rs, t.getFields(), isSource) );
                    }
                    listener.endBatch();
                    ps.clearParameters();
                    if(counter<batchSize) break;
                    start = start + batchSize;
                }
            }
            else {
                System.out.println("start non paging...");
                fillParameters( ps, t.getParams(), parameterNames );                
                rs = ps.executeQuery();
                rs.setFetchDirection(ResultSet.FETCH_FORWARD);
                rs.setFetchSize(batchSize);
                int counter = batchSize;
                int start = 0;
                while(rs.next()) {
                    if(counter>=batchSize) {
                        counter = 0;
                        listener.startBatch(start, batchSize);
                    }
                    listener.fetchRow(createMapRecord(rs, t.getFields(), isSource) );
                    start++;
                    counter++;
                    if(counter>=batchSize) {
                        listener.endBatch();
                    }
                }
                if(start>0 && (counter<batchSize)) {
                    listener.endBatch();
                } 
            }
        } 
        catch (Exception e) {
            throw new IllegalStateException(e);
        } 
        finally {
            listener.stop();
            try {rs.close();} catch(Exception ign){;}
            try {ps.close();} catch(Exception ign){;}
        }
    }
    
    
}
