/*
 * DateService.java
 *
 * Created on April 18, 2009, 8:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.interfaces.AsyncResponseServiceLocal;
import com.rameses.util.DateUtil;
import java.util.Date;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(AsyncResponseServiceLocal.class)
public class AsyncResponseService implements AsyncResponseServiceLocal {
    
    @PersistenceContext(unitName="systemPU")
    private EntityManager em;
    
    @Resource
    private SessionContext sessionCtx;
    
    public void pushResponse(String requestId, Object data) {
        ResponseQueue b = new ResponseQueue(requestId);
        b.setObjectData(data);
        Date expiry = DateUtil.add(new Date(), "1h");
        b.setExpiryDate( expiry );
        em.persist(b);
        em.flush();
    }
    
    public Object getPollData(String requestId) {
        try {
            Query q = em.createQuery("select o from " + ResponseQueue.class.getName() + " o where o.requestId=:r");
            q.setParameter("r", requestId);
            q.setFirstResult(0);
            q.setMaxResults(1);
            ResponseQueue b = (ResponseQueue) q.getSingleResult();
            Object retVal = b.getObjectData();
            em.remove(b);
            return retVal;
        } catch(Exception e) {
            return null;
        }
    }
    
    
    
}
