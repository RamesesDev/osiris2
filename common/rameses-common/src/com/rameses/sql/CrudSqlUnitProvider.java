/*
 * CrudSqlCacheProvider.java
 *
 * Created on August 3, 2010, 3:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;

/**
 * This class parses the following to its desired field
 * <table name="tableName">
 *      name1=fieldName1 *    //* represents primary keys
 *      name2=fieldName2
 *      name3=fieldName3
 * </table>
 */
public class CrudSqlUnitProvider extends SimpleSqlUnitProvider {
    
    private final static String CREATE  = "create";
    private final static String READ  = "read";
    private final static String UPDATE  = "update";
    private final static String DELETE  = "delete";
    private final static String LIST  = "list";
    
    public String getType() {
        return "crud";
    }

    public SqlUnit getSqlUnit(String name) {
        //fix first the name
        //fix first the name
        String action = null;
        String ext = "."+getType();
        if( name.endsWith("_"+CREATE+ext) ) action = CREATE;
        else if( name.endsWith("_"+READ+ext) ) action = READ;
        else if( name.endsWith("_"+UPDATE+ext) ) action = UPDATE;
        else if( name.endsWith("_"+DELETE+ext) ) action = DELETE;
        else if( name.endsWith("_"+LIST+ext) ) action = LIST;
        if( action == null )
            throw new RuntimeException("Please explicitly provide an action for " + name );
        
        String alias = name.substring(0, name.lastIndexOf( "_" + action+ext));
        String resName = alias +  ext;
        
        InputStream is = null;
        try {
            is = getConf().getResourceProvider().getResource(resName);
            
            //String action = name
            BasicSqlCrudParser cp = new BasicSqlCrudParser();
            CrudModel crudModel = cp.parse( is );
            if(action.equals(CREATE))
                return CrudSqlBuilder.getInstance().getCreateSqlUnit(crudModel);
            else if(action.equals(READ))
                return CrudSqlBuilder.getInstance().getReadSqlUnit(crudModel);
            else if(action.equals(UPDATE))
                return CrudSqlBuilder.getInstance().getUpdateSqlUnit(crudModel);
            else if(action.equals(DELETE))
                return CrudSqlBuilder.getInstance().getDeleteSqlUnit(crudModel);
            else if(action.equals(LIST))
                return CrudSqlBuilder.getInstance().getListSqlUnit(crudModel, alias);
            else
                throw new RuntimeException("Crud action " + action + " is not supported!");
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try { is.close(); } catch(Exception e) {;}
        }

        
        
    }
    
    
    
}
