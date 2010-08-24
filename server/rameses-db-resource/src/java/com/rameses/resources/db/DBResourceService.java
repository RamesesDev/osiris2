/*
 * DBScriptProviderService.java
 *
 * Created on December 6, 2009, 12:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.resources.db;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Stateless
@Local(DBResourceServiceLocal.class)
public class DBResourceService implements DBResourceServiceLocal {
    
    @PersistenceContext(unitName="dbscriptPU")
    private EntityManager em;
    
    /** Creates a new instance of DBScriptProviderService */
    public DBResourceService() {
    }
    
    public byte[] getResource(Class clazz, String name) {
        try {
            String ql = "select o.content from " + clazz.getName() + " o where o.name=:name";
            Object o = em.createQuery(ql).setParameter("name",name).getSingleResult();
            if(o!=null) 
                return o.toString().getBytes();
            else
                return null;
        } 
        catch(NoResultException nre) {
            return null;
        } 
        catch (Exception e) {
            throw new IllegalStateException(e);
        } 
    } 
    
    public byte[] getTemplateResource(String name) {
        return getResource(ResourceTemplate.class, name);
    }

    public byte[] getScriptResource(String name) {
        return getResource(ResourceScript.class, name);
    }

    
    public byte[] getDsResource(String name) {
        return getResource(ResourceDs.class, name);
    }

    //collective resources
    public List getInterceptors() {
        try {
            StringBuffer sb = new StringBuffer("select o.name from " );
            sb.append( ResourceScript.class.getName() + " o ");
            sb.append(" where o.category=:category");
            return em.createQuery(sb.toString()).setParameter("category","interceptor").getResultList();
        } 
        catch(NoResultException nre) {
            return null;
        } 
        catch (Exception e) {
            throw new IllegalStateException(e);
        } 
    }

    public List getDsList() {
        String ql = "select o.name from " + ResourceDs.class.getName() + " o ";
        return em.createQuery(ql).getResultList();
    }

    
}
