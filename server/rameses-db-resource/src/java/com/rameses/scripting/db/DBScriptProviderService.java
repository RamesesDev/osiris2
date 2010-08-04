/*
 * DBScriptProviderService.java
 *
 * Created on December 6, 2009, 12:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.scripting.db;

import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;


@Stateless
@Local(DBScriptProviderServiceLocal.class)
public class DBScriptProviderService implements DBScriptProviderServiceLocal {
    
    @PersistenceContext(unitName="dbscriptPU")
    private EntityManager em;
    
    /** Creates a new instance of DBScriptProviderService */
    public DBScriptProviderService() {
    }
    
    public byte[] getInfo(String name, String className) {
        try {
            StringBuffer sb = new StringBuffer("select o.code from ");
            sb.append( className + " o ");
            sb.append(" where o.name=:name");
            Object o = em.createQuery(sb.toString()).setParameter("name",name).getSingleResult();
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

    public byte[] getAllInterceptors() {
        try {
            StringBuffer sb = new StringBuffer("select o.name from " );
            sb.append( DBScriptInfo.class.getName() + " o ");
            sb.append(" where o.category=:category");
            List list = em.createQuery(sb.toString()).setParameter("category","interceptor").getResultList();
            
            StringBuffer data = new StringBuffer();
            for(Object o: list ) {
                data.append( o + "\n");
            }
            return data.toString().getBytes();
        } 
        catch(NoResultException nre) {
            return null;
        } 
        catch (Exception e) {
            throw new IllegalStateException(e);
        } 
    }
    

    
}
