package com.rameses.data.server;

import java.util.List;
import java.util.Map;
import javax.ejb.SessionContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;

public class EntityService  {
    
    private SessionContext ctx;
    
    private EntityManager getEm(String name) {
        return (EntityManager) ctx.lookup(name);
    }
    
    public List query(String ejbql, Object params, String unitName) throws Exception {
        EntityManager em = getEm(unitName);
        Query q = em.createQuery( ejbql );
        if(params!=null && (params instanceof Object[]) ) {
            Object[] arr = (Object[])params;
            for( int i=0; i<arr.length; i++) {
                q.setParameter(i+1, arr[i]);
            }
        }
        else if( params!=null && (params instanceof Map) ) {
            Map map = (Map)params;
            for( Object o: map.entrySet()) {
                Map.Entry me = (Map.Entry)o;
                q.setParameter( me.getKey()+"", me.getValue() );
            }
        }
        return q.getResultList();
    }

    public Object findEntity(Object id, Class clz, String unitName) throws Exception {
        EntityManager em = getEm(unitName);
        return em.find(clz,id);
    }

    public Object findEntity(String ejbql, Object params, String unitName) throws Exception {
        EntityManager em = getEm(unitName);
        Query q = em.createQuery( ejbql );

        if(params!=null && (params instanceof Object[]) ) {
            Object[] arr = (Object[])params;
            for( int i=0; i<arr.length; i++) {
                q.setParameter(i+1, arr[i]);
            }
        }
        else if( params!=null && (params instanceof Map) ) {
            Map map = (Map)params;
            for( Object o: map.entrySet()) {
                Map.Entry me = (Map.Entry)o;
                q.setParameter( me.getKey()+"", me.getValue() );
            }
        }
        return q.getSingleResult();
    }

    public Object createEntity(Object entity, Object id, String unitName) throws Exception {
        //find first. throw error if already exist
        EntityManager em = getEm(unitName);
        if( id == null ) id = EntityUtil.getId(entity);
        Object test = em.find( entity.getClass(), id );
        if( test != null ) {
            throw new Exception( "Entity " + id + " already exists");
        }
        em.persist(entity);
        return entity;
    }

    public Object updateEntity(Object entity, Object id, String unitName) throws Exception {
        EntityManager em = getEm(unitName);
        if( id == null ) id = EntityUtil.getId(entity);
        Object test = em.find( entity.getClass(), id );
        if( test == null ) {
            throw new Exception( "Entity " + id + " does not exist");
        }
        em.merge(entity);
        return entity;
    }

    public Object createOrUpdateEntity(Object entity, Object id, String unitName) throws Exception {
        EntityManager em = getEm(unitName);
        if( id == null ) id = EntityUtil.getId(entity);
        Object test = em.find( entity.getClass(), id );
        if( test == null ) {
            em.persist(entity);
        }
        else {
            em.merge( entity );
        }
        return entity;
    }

    public void removeEntity(Object entity, Object id, String unitName) throws Exception {
        EntityManager em = getEm(unitName);
        if( id == null ) id = EntityUtil.getId(entity);
        Object test = em.find( entity.getClass(), id );
        if( test == null ) {
            throw new Exception( "Entity " + id + " does not exist");
        }
        em.remove( test );
    }
    
    
    
}
