/*
 * DateService.java
 *
 * Created on April 18, 2009, 8:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.system;

import com.rameses.interfaces.DateServiceLocal;
import java.util.Date;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(DateServiceLocal.class)
public class DateService implements DateServiceLocal {
    
    @PersistenceContext(unitName="systemPU")
    private EntityManager em;
    
    public Date getServerDate() {
        Long id = new Long(1);
        DateBean db = em.find(DateBean.class, id);
        if( db == null ) {
            System.out.println("initializing the Date Bean");
            db = new DateBean();
            db.setId(id);
            em.persist(db);
        }
        
        Query q = em.createQuery("select current_timestamp() from DateBean");
        return (Date) q.getSingleResult();
    }

   
}
