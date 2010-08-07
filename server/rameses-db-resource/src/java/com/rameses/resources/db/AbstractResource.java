/*
 * AbstractResource.java
 *
 * Created on August 5, 2010, 7:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resources.db;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractResource implements Serializable {
    
    /** Creates a new instance of AbstractResource */
    public AbstractResource() {
    }
    
    @Id
    @Column(length=50)
    private String name;
    
    
    @Lob
    private String content;
    
    @Column(length=255)
    private String category;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
}
