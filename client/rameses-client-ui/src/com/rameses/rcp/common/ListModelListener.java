package com.rameses.rcp.common;

public interface ListModelListener 
{
    /**
     * selectedRowPos refers to the row that should be refreshed
     * after refreshing the list. if -1 it means refresh whatever
     * is the current row.
     */
    void refreshList();
    
    /**
     * this is called to refresh only the affected row, rows
     */
    void refreshItemUpdated(int row);
    void refreshItemAdded(int fromRow, int toRow);
    void refreshItemRemoved(int fromRow, int toRow);
    void refreshSelectedItem();
    
}
