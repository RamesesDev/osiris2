/*
 * DBScriptService.java
 * Created on December 21, 2011, 9:46 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.service.jdbc;

import java.util.Map;

/**
 *
 * @author jzamss
 */
public interface DBService {
    Map getResultSet(String statement, Object parameters) throws Exception;
}
