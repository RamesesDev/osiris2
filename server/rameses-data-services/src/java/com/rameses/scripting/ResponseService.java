/*
 * DateService.java
 *
 * Created on April 18, 2009, 8:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.interfaces.ResponseServiceLocal;
import com.rameses.util.DateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(ResponseServiceLocal.class)
public class ResponseService implements ResponseServiceLocal {

    @PersistenceContext(unitName="systemPU")
    private EntityManager em;

    public void registerData(String requestId, Object data) {
        ResponseBean b = new ResponseBean(requestId);
        b.setObjectData(data);
        Date expiry = DateUtil.add(new Date(), "1h");
        b.setExpiryDate( expiry );
        em.persist(b);
    }

    public void removeStaleObjects() {
        System.out.println("removing stale objects");
    }

    public List getResponseData(String requestId) {
        List result = new ArrayList();
        List<ResponseBean> removeObjects = new ArrayList<ResponseBean>();
        Query q = em.createQuery("select o from " + ResponseBean.class.getName() + " o where o.requestId=:r");
        q.setParameter("r", requestId);
        for(Object o: q.getResultList()) {
            ResponseBean b = (ResponseBean)o;
            result.add( b.getObjectData() );
            removeObjects.add( b );
        }
        
        //removing data now
        for(ResponseBean b: removeObjects) {
            em.remove( b );
        }
        return result;
    }
    
   
}
