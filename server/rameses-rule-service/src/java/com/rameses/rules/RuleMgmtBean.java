/*
 * RuleMgmtBean.java
 *
 * Created on April 19, 2009, 5:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="sys_rule_mgmt")
public class RuleMgmtBean implements Serializable {
    
    @Id
    @Column(length=50)
    private String name;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtrule;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtmodified;
    
    public RuleMgmtBean() {
    }
    
    public RuleMgmtBean(String name) {
        this.name = name;
        Date d = new Date();
        this.setDtmodified(d);
        this.setDtrule(d);
    }

    public String getName() {
        return name;
    }

    public Date getDtmodified() {
        return dtmodified;
    }

    public void setDtmodified(Date dtmodified) {
        this.dtmodified = dtmodified;
    }

    public Date getDtrule() {
        return dtrule;
    }

    public void setDtrule(Date dtrule) {
        this.dtrule = dtrule;
    }

    

}
