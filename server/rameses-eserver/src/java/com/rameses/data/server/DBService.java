package com.rameses.data.server;

import com.rameses.interfaces.DBServiceLocal;
import com.rameses.interfaces.DBHandler;
import com.rameses.interfaces.IFetchHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.sql.DataSource;


@Stateless
@Local(DBServiceLocal.class)
public class DBService implements DBServiceLocal {
    
    @Resource
    private SessionContext ctx;
    
    @Resource(mappedName="java:/SqlCache")
    private SqlCacheMBean sqlCache;
    
    private PreparedStatement prepareStatement( Connection conn, String sql, Object params ) throws Exception {
        String xsql = sql;
        Object[] arr = new Object[]{};
        
        if( params != null && (params instanceof Map) ) {
            StringBuffer sb = new StringBuffer();
            List parms = new ArrayList();
            Map map = (Map)params;
            SqlUtil.parseStatement(sql, sb, map, parms);
            xsql = sb.toString();
            arr = parms.toArray(new Object[]{});
        } else if( params instanceof Object[] ) {
            arr = (Object[])params;
        }
        
        PreparedStatement ps = conn.prepareStatement(xsql);
        for (int i=0; i<arr.length; i++) {
            ps.setObject(i+1, arr[i]);
        }
        return ps;
    }
    
    public Object getSchema(String statement, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareStatement(conn, statement, null);
            rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            List list = new ArrayList<Map>();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            for (int i=0; i<columnCount; i++) {
                HashMap map = new HashMap();
                map.put( "name", meta.getColumnLabel(i+1) );
                map.put( "caption", meta.getColumnLabel(i+1) );
                map.put( "source", meta.getColumnName(i+1) );
                map.put( "width", meta.getColumnDisplaySize(i+1) );
                map.put( "type", loader.loadClass(meta.getColumnClassName(i+1)) );
                map.put( "precision", meta.getPrecision(i+1) );
                map.put( "scale", meta.getScale(i+1) );
                map.put( "description", null );
                list.add( map );
            }
            return list;
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try { rs.close(); } catch(Exception ign){;}
            try { ps.close(); } catch(Exception ign){;}
        }
    }
    
    public Object getSchema(String statement, String datasource) {
        Connection conn = null;
        try {
            conn = getConnection(datasource);
            return  getSchema(statement, conn);
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { conn.close(); } catch(Exception ign) {;}
        }
    }
    
    public Object getData(String statement, Object params, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map> list = null;
        try {
            ps = prepareStatement( conn, statement, params);
            rs = ps.executeQuery();
            
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            
            list = new ArrayList<Map>();
            while (rs.next()) {
                Map data = new HashMap();
                for (int i=0; i<columnCount; i++) {
                    String name = meta.getColumnName(i+1);
                    data.put(name, rs.getObject(name));
                }
                list.add(data);
            }
            return list;
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { rs.close(); }catch(Exception ign) {;}
            try { ps.close(); }catch(Exception ign) {;}
        }
    }
    
    public Object getData(String statement, Object params, String datasource) {
        Connection conn = null;
        try {
            conn = getConnection(datasource);
            return getData(statement,params,conn);
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { conn.close(); }catch(Exception ign) {;}
        }
    }
    
    public Object postData(String statement, Object params, Connection conn) {
        PreparedStatement ps = null;
        try {
            ps = prepareStatement(conn, statement, params );
            return ps.executeUpdate();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { ps.close(); } catch(Exception ign) {;}
        }
    }
    
    public Object postData(String statement, Object params, String datasource) {
        Connection conn = null;
        try {
            conn = getConnection(datasource);
            return  postData(statement,params,conn);
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { conn.close(); } catch(Exception ign) {;}
        }
    }
    
    public void fetchData(String statement, Object params, Connection conn, IFetchHandler handler) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = prepareStatement( conn, statement, params);
            rs = ps.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int columnCount = meta.getColumnCount();
            while (rs.next()) {
                Map data = new HashMap();
                for (int i=0; i<columnCount; i++) {
                    String name = meta.getColumnName(i+1);
                    data.put(name, rs.getObject(name));
                }
                handler.fetch(data);
            }
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { rs.close(); }catch(Exception ign) {;}
            try { ps.close(); }catch(Exception ign) {;}
        }
    }
    
    public void fetchData(String statement, Object params, String datasource, IFetchHandler handler) {
        Connection conn = null;
        try {
            conn = getConnection(datasource);
            fetchData(statement,params,conn,handler);
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { conn.close(); }catch(Exception ign) {;}
        }
    }
    
    public Object execute(String name, Map params) {
        Connection conn = null;
        try {
            SqlCacheBean s = sqlCache.get(name);
            String ds = SqlUtil.substituteValues(s.getDatasource(), params);
            conn = getConnection(ds);
            return executeExecutor(s, params,conn);
        } 
        catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try {conn.close();} catch(Exception ign){;}
        }
    }
    
    public Object execute(String name, Map params, Map connMap) {
        SqlCacheBean s = sqlCache.get(name);
        String ds = SqlUtil.substituteValues(s.getDatasource(), params);        
        Connection conn = (Connection)connMap.get(ds);        
        return executeExecutor(s, params,conn);
    }
    
    //originally this code was in the SqlExecutor
    private Object executeExecutor( SqlCacheBean se, Map params,Connection conn ) {
        String sql = SqlUtil.substituteValues( se.getStatement(), params );
        Integer batchSize = se.getBatchSize();
        String method = se.getMethod();
        String handler = se.getHandler();
        if(batchSize != null) {
            //executing batch is a little tricky. find first if there are 
            //items marked as list or batch. If not, find first value that 
            //contains a List
            
            List list = null;
            if( params.containsKey("list")) {
                list = (List)params.get("list");
            }
            else if( params.containsKey("batch")) {
                list = (List)params.get("batch");
            }
            else {
                for(Object o : params.values() ) {
                    if( o instanceof List ) {
                        list = (List)o;
                        break;
                    }
                }
            }
            executeBatch(sql, list, conn, batchSize.intValue() );
            return null;
        }
        else if( method.equalsIgnoreCase("insert") || method.equalsIgnoreCase("update") ) {
            return postData( sql, params, conn );
        }
        else {
            Object handle = null;
            if( handler != null && handler.trim().length() > 0 ) {
                String hname = SqlUtil.substituteValues(handler, params);
                handle = params.get(hname);
            }
            if( (handle != null) && (handle instanceof IFetchHandler)  ) {
                IFetchHandler fetchHandler = (IFetchHandler)handle;
                fetchData(sql, params, conn, fetchHandler);
                return null;
            }
            else {
                return getData( sql, params, conn );
            }
        }
    }
    
    
    //special method for executing batch statements
    public void executeBatch(String statement, List params, Connection conn, int batchSize) {
        PreparedStatement ps = null;
        try {
            //parse the statement
            List paramNames = new ArrayList();
            StringBuffer sb = new StringBuffer();
            SqlUtil.parseStatement(statement, sb, paramNames);
            String sql = sb.toString();
            ps = conn.prepareStatement( sql );
            int listSize = params.size();
            for( int start = 0; start < listSize; start = (start + batchSize) ) {
                int sz = ((start+batchSize) > listSize ) ? listSize : start+batchSize;
                List subList = params.subList(start, sz);
                fillBatch( ps, subList, paramNames );
                ps.executeBatch();
            }
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { ps.close(); } catch(Exception ign) {;}
        }
    }
    
    public void executeBatch(String statement, List params, String datasource, int batchSize) {
        Connection conn = null;
        try {
            conn = getConnection(datasource);
            executeBatch(statement, params,conn,batchSize);
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        } finally {
            try { conn.close(); } catch(Exception ign) {;}
        }
    }
    
    private void fillBatch(PreparedStatement ps, List list, List paramNames) throws Exception {
        for( Object o : list ) {
            if( o instanceof Map ) {
                Map m = (Map)o;
                for( int i =0; i<paramNames.size(); i++ ) {
                    String fldName = (String) paramNames.get(i);
                    ps.setObject(i+1, m.get( fldName ) );
                }
            } else {
                Object[] arr = (Object[])o;
                for(int i=0; i<arr.length; i++ ) {
                    ps.setObject( i+1, arr[i] );
                }
            }
            ps.addBatch();
        }
    }
    
    public Connection getConnection(String datasource) {
        try {
            DataSource ds = (DataSource)ctx.lookup( datasource );
            if( ds == null )
                throw new Exception( "Datasource " + datasource + " does not exist!");
            return ds.getConnection();
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public Map getConnectionMap(List datasources) {
        Map<String,Connection> map = new HashMap<String,Connection>();
        for(Object o: datasources) {
            String s = (String)o;
            try {
                map.put(s, getConnection(s));
            } catch(Exception e) {
                throw new IllegalStateException(e);
            }
        }
        return map;
    }

    //group transactions
    private void setAutoCommit( Map connMap, boolean autoCommit ) {
        for(Object o: connMap.values()) {
            try { ((Connection)o).setAutoCommit(autoCommit); } catch(Exception ign){;}
        }
    }
    
    private void commitAll( Map connMap ) {
        for(Object o: connMap.values()) {
            try { ((Connection)o).commit(); } catch(Exception ign){;}
        }
    }
    
    private void rollBackAll( Map connMap ) {
        for(Object o: connMap.values()) {
            try { ((Connection)o).rollback(); } catch(Exception ign){;}
        }
    }

    public Object execChain(List dataUnits, List datasources, boolean transactional) throws Exception{
        Map connMap =  getConnectionMap(datasources);
        if(transactional) setAutoCommit(connMap, false);
        Map inputs = new HashMap();
        try {
            for(Object o: dataUnits) {
                DBHandler du = (DBHandler)o;
                du.execute(inputs,connMap);
            }
            if(transactional) commitAll(connMap);
        } catch(Exception ex) {
            if(transactional) rollBackAll(connMap);            
            throw ex;
        } finally {
            closeConnectionMap(connMap);
        }
        return inputs;
    }
    
    public void closeConnectionMap(Map connMap) {
        for(Object o: connMap.values()) {
            Connection c = (Connection)o;
            try {c.close(); } catch(Exception ign){;}
        }
    }

}
