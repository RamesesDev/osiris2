/*
 * ResponseBean.java
 *
 * Created on July 5, 2010, 12:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name="sys_response")
public class ResponseBean implements Serializable {
    
    @Id
    @Column(length=50)
    private String objid;
    
    @Column(length=50)
    private String requestId;
    
    @Lob
    private byte[] data;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    
    public ResponseBean() {
        this.objid = new UID()+"";
    }
    
    public ResponseBean(String reqId) {
        this.objid = new UID()+"";
        this.requestId = reqId;
    }
    
    public String getObjid() {
        return objid;
    }
    
    public String getRequestId() {
        return requestId;
    }
    
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    
    public byte[] getData() {
        return data;
    }
    
    
    public void setObjectData(Object obj ) {
        ByteArrayOutputStream b = null;
        ObjectOutputStream oos = null;
        try {
            b = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(b);
            oos.writeObject( obj );
            this.data = b.toByteArray();
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
        finally {
            try { oos.close(); } catch(Exception ign){;}
            try { b.close(); } catch(Exception ign){;}
        }
    }
    
    public Object getObjectData() {
        ByteArrayInputStream b = null;
        ObjectInputStream ois = null;
        try {
            b = new ByteArrayInputStream(data);
            ois = new ObjectInputStream(b);
            return ois.readObject();
        } catch(Exception e) {
            throw new IllegalStateException(e);
        }
        finally {
            try { ois.close(); } catch(Exception ign){;}
            try { b.close(); } catch(Exception ign){;}
        }
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    
    
}
