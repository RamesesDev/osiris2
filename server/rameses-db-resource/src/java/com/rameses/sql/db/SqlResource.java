/*
 * DBScriptInfo.java
 *
 * Created on December 6, 2009, 12:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql.db;

import com.rameses.resources.db.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="res_sql")
public class SqlResource extends AbstractResource {
    
    public SqlResource() {
    }
    
}
