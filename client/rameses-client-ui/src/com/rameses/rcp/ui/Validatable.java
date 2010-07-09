/*
 * UIValidatable.java
 *
 * Created on June 21, 2010, 3:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.ui;

import com.rameses.rcp.util.ActionMessage;

/**
 *
 * @author jaycverg
 */
public interface Validatable extends Comparable {
    
    String getCaption();
    void setCaption(String caption);
    boolean isRequired();
    void setRequired(boolean required);
    void validateInput();
    ActionMessage getActionMessage();
    
}
