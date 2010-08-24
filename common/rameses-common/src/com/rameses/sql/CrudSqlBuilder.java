/*
 * CrudSqlCacheBuilder.java
 *
 * Created on August 12, 2010, 8:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

/**
 *
 * @author elmo
 */
public abstract class CrudSqlBuilder {
    
    public abstract SqlUnit getCreateSqlUnit( CrudModel cp );
    public abstract SqlUnit getReadSqlUnit( CrudModel cp );
    public abstract SqlUnit getUpdateSqlUnit( CrudModel cp );
    public abstract SqlUnit getDeleteSqlUnit( CrudModel cp );
    public abstract SqlUnit getListSqlUnit( CrudModel cp , String xalias);
    
    
    private static CrudSqlBuilder instance;
    
    public static CrudSqlBuilder getInstance() {
        if(instance==null) {
            instance = new DefaultCrudSqlBuilder();
        }
        return instance;
    }

    
}
