/*
 * RuleServiceMBean.java
 *
 * Created on July 14, 2010, 8:20 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

import javax.ejb.Timer;

/**
 *
 * @author elmo
 */
public interface RuleServiceMgmtLocal {
    void startManager();
    void timeout(Timer t);
}
