/*
 * NamedQueryProvider.java
 *
 * Created on July 22, 2010, 3:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

/**
 *
 * @author elmo
 */
public interface NamedQueryProvider {
    String getStatement(String name);
}
