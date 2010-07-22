/*
 * TableListener.java
 *
 * Created on July 2, 2010, 10:11 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;


public interface TableListener {
    
    void refreshList();
    void openItem();
    void rowChanged();
    void editCellAt(int rowIndex, int colIndex);
    void cancelRowEdit();
    
}
