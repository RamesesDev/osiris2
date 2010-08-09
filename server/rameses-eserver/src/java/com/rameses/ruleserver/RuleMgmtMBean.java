/*
 * RuleMgmtMBean.java
 *
 * Created on July 26, 2010, 12:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.ruleserver;

import com.rameses.eserver.*;
import java.util.List;

public interface RuleMgmtMBean {
    
    void start() throws Exception;
    void stop() throws Exception;
    void deploy();
    void deploy(String ruleBaseName);
    
    String getJndiName();
    void setJndiName(String jndiName);

    Object createObject(String ruleBase, String className);
    void reload(String ruleBase);
    void fireRule(String ruleBaseName, List facts);
    
}
