/*
 * TableControl.java
 *
 * Created on January 31, 2011, 11:17 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import com.rameses.rcp.common.AbstractListModel;
import java.awt.Color;
import javax.swing.table.TableModel;


public interface TableControl {
    
    TableModel getModel();
    AbstractListModel getListModel();
    public Color getEvenBackground();
    public Color getOddBackground();
    public Color getErrorBackground();
    public Color getEvenForeground();
    public Color getOddForeground();
    public Color getErrorForeground();
    
    
}
