/*
 * SqlDialect.java
 *
 */

package com.rameses.sql;

public interface SqlDialect {
    
    String getCreateStatement(Schema t);
    String getUpdateStatement(Schema t);
    String getRemoveStatement(Schema t);
    String getFindStatement(Schema t);
    
    
}
