/*
 * DBPermissionInfo.java
 *
 * Created on August 4, 2010, 6:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.resources.db;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="res_template")
public class ResourceTemplate extends AbstractResource {

    public ResourceTemplate() {
    }

    
}

