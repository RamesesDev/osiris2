package com.rameses.data.server;

import com.rameses.interfaces.DataServiceLocal;
import com.rameses.interfaces.IFetchHandler;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Stateless
@Local(DataServiceLocal.class)
public class EJBService implements DataServiceLocal {
    
    @Resource
    private SessionContext ctx;
    
    public Object getSchema(String statement, String datasource) {
        return null;
    }
    
    //statement must be an EJBQL statement. _start and _limit are keywrods.
    public Object getData(String statement, Object params, String datasource) {
        EntityManager em = (EntityManager)ctx.lookup(datasource);
        Query q = em.createQuery(statement);
        if(  params != null ) {
            if( params instanceof Map) {
                Map map = (Map)params;
                for(Object o: map.entrySet() ) {
                    Map.Entry me = (Map.Entry)o;
                    String k = me.getKey() + "";
                    if(! k.matches("_start|_limit")) {
                        q.setParameter( me.getKey()+"", me.getValue() );
                    }
                }
                Integer start = (Integer) map.get("_start");
                Integer limit = (Integer) map.get("_limit");
                if( start != null ) q.setFirstResult( start );
                if( limit != null ) q.setMaxResults( limit );
            } 
            else if( params instanceof Object[] ) {
                Object[] arr = (Object[])params;
                for(int i=0; i<arr.length;i++) {
                    q.setParameter(i+1, arr[i]);
                }
            }
        }
        return q.getResultList();
    }
    
    public Object postData(String statement, Object params, String datasource) {
        EntityManager em = (EntityManager)ctx.lookup(datasource);
        try {
            //check the methods : create, update, createOrUpdate, remove
            if( statement.endsWith(".create") ) {
                em.persist(params);
            } 
            else if( statement.endsWith(".update")) {
                Object id = EntityUtil.getId(params);
                Object test = em.find( params.getClass(), id );
                if( test == null ) {
                    throw new Exception( "Record does not exist for " + id );
                }
                em.merge( params );
            } 
            else if( statement.endsWith(".createOrUpdate")) {
                Object id = EntityUtil.getId(params);
                Object test = em.find( params.getClass(), id );
                if( test == null ) {
                    em.persist(params);
                }
                else {
                    em.merge( params );    
                }
            } 
            else if(statement.endsWith(".remove")) {
                Object id = EntityUtil.getId(params);
                Object test = em.find( params.getClass(), id );
                if( test != null ) em.remove(test);
            } 
            return params;
            
        } 
        catch(Exception ex) {
            throw new IllegalStateException( ex);
        }
    }

  
    public void fetchData(String statement, Object params, String datasource, IFetchHandler handler) {
        EntityManager em = (EntityManager)ctx.lookup(datasource);
        Query q = em.createQuery(statement);
        
        if(  params != null ) {
            if( params instanceof Map) {
                Map map = (Map)params;
                for(Object o: map.entrySet() ) {
                    Map.Entry me = (Map.Entry)o;
                    String k = me.getKey() + "";
                    if(! k.matches("_start|_limit")) {
                        q.setParameter( me.getKey()+"", me.getValue() );
                    }
                }
                Integer start = (Integer) map.get("_start");
                Integer limit = (Integer) map.get("_limit");
                if( start != null ) q.setFirstResult( start );
                if( limit != null ) q.setMaxResults( limit );
            } 
            else if( params instanceof Object[] ) {
                Object[] arr = (Object[])params;
                for(int i=0; i<arr.length;i++) {
                    q.setParameter(i+1, arr[i]);
                }
            }
        }
        for( Object o: q.getResultList() ) {
            handler.fetch(o);
        }
    }

    public Object execute(String name, Map params) {
        throw new IllegalStateException("EJBService.execute not yet supported");
    }

    public void executeBatch(String statement, List params, String datasource, int batchSize) {
        throw new IllegalStateException("EJBService.executeBatch not yet supported");
    }

    public Object execStatement(String sql, Map params, String datasource) {
        throw new IllegalStateException("EJBService.execute not yet supported");
    }

   

    
    
}
