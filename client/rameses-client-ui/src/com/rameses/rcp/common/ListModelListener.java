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
    
    /**
     * This is called when the model fires its rebuildColumns method.
     * This is called when the list of collumns changed.
     */
    void rebuildColumns();
    
    /**
     * These methods are used as fetch callback handlers
     * when the fetch method is called asynchronously
     */
    void fetchStart();
    void fetchEnd();
    
}
