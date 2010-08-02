/*
 * DBScriptInfo.java
 *
 * Created on December 6, 2009, 12:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="sys_script_conf")
public class DBConfData implements Serializable {
    
    @Id
    @Column(length=50)
    private String name;
    
    @Lob
    private String code;

    public DBConfData() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
