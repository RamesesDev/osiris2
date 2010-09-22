/*
 * AbstractCalendarModel.java
 *
 * Created on September 21, 2010, 1:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.control.date;

import javax.swing.JComponent;

/**
 *
 * @author Windhel
 */

public abstract class AbstractCalendarModel {
    
    public abstract void showCalendar();
    
    public abstract void hideCalendar();
    
    public abstract String getSelectedValue();
    
    public abstract JComponent getCalendar();
}
