/*
 * AbstractListModel.java
 *
 * Created on January 14, 2010, 8:17 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.rcp.common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author elmo
 */
public abstract class AbstractListModel {
    
    protected ListItem selectedItem;
    protected String selectedColumn;
    protected ListModelListener listener;
    private int rows = 10;
    
    
    //This is the real list needed by the grid. Not overridable
    protected List<ListItem> items = new ArrayList<ListItem>();
    
    protected List dataList;
    
    /**
     * this contains items that have been selected
     */
    protected Set checkedItems = new HashSet();
    
    /**
     * Override this if you want a more customized ListItem.
     * One may need to customize if it contains ListItem additional
     * codes.
     */
    public ListItem createListItem() {
        return new ListItem();
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public final ListItem getSelectedItem() {
        return this.selectedItem;
    }
    
    public final void setSelectedItem(ListItem o) {
        this.selectedItem = o;
        refreshSelectedItem();
    }
    
    public final void refreshSelectedItem() {
        if(listener!=null) {
            listener.refreshSelectedItem();
        }
    }
    
    public final void setSelectedItem(int i) {
        if(i>=0 && i < items.size() ) {
            setSelectedItem( items.get(i) );
        }
    }
    
    public Column[] getColumns() {
        return columns;
    }
    
    public void setColumns(Column[] columns) {
        this.columns = columns;
    }
    
    public final String getSelectedColumn() {
        return selectedColumn;
    }
    
    public final void setSelectedColumn(String selectedColumn) {
        this.selectedColumn = selectedColumn;
    }
    
    public ListModelListener getListener() {
        return listener;
    }
    
    public void setListener(ListModelListener listener) {
        this.listener = listener;
    }
    
    
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    
    //</editor-fold>
    
    
    /**
     * This method is only called once. when initiating.
     * most do not have an implementation. Most notably used
     * by AsyncListModel.
     */
    public void init() {
        
    }
    
    /**
     * This method is called to rebuild the
     * entire list and resets everything to 0.
     */
    public void load() {
        dataList = null;
        selectedItem = null;
        refresh();
    }
    
    /**
     * this method is contrary to the load method, does not reset the list
     */
    public void refresh() {
        fetch();
        if(listener!=null) {
            listener.refreshList();
            listener.refreshSelectedItem();
        }
    }
    
    /**
     * this method is called only by the direct subclasses
     * like ListModel, PageListModel.
     */
    protected abstract void fetch();
    
    /**
     * This method is called to build the ListItem objects based on
     * the getRows(). If the items size is already the same as getRows(),
     * there will be no need to rebuild the items.
     */
    public final void buildListItems() {
        if( items.size() == getRows() ) {
            return;
        }
        
        items.clear();
        int _rows = getRows();
        for(int i=0; i < _rows; i++) {
            ListItem gi = createListItem();
            gi.loadItem(null);
            gi.setParent(this);
            gi.setIndex(i);
            gi.setRownum(i);
            items.add(gi);
        }
    }
    
    /**
     * This method is called by the class implementing the fetch method
     */
    protected final void fillListItems(List list, int toprow) {
        buildListItems();
        int sz = list.size();
        //reserve the last row with a new item.
        boolean newRowadded = false;
        for(int i=0; i< getRows();i++) {
            ListItem li = items.get(i);
            li.setIndex(i);
            li.setRownum(toprow+i);
            li.initSelected();
            if( i < sz ) {
                Object item = list.get(i);
                li.loadItem( item );
                li.setState(1);
                
                //check if item is selected
                if( checkedItems.contains(item) ) {
                    li.setSelected(true);
                }
            } else {
                li.loadItem(null);
                li.setState(0);
                Object newItem = createItem();
                if(newItem!=null && !newRowadded) {
                    newRowadded = true;
                    li.loadItem(newItem);
                }
            }
        }
    }
    
    
    /**
     * default columns will display item's toString() with a blank header
     */
    private Column[] columns = new Column[] {
        new Column("item", null)
    };
    
    
    /**
     * this method is called when the selectedItem has a state of 0 which means new
     * and the item associated with it is not null. Take note, item becomes null if
     * createItem method is not implemented.
     */
    public final Object addCreatedItem() {
        if(selectedItem==null)
            return null;
        //throw new IllegalStateException("addCreatedItem error. selectedItem must not be null");
        if(selectedItem.getState()==0) {
            Object item = selectedItem.getItem();
            if(item==null)
                throw new IllegalStateException("addCreateItem error. Selected new object is null. createItem must be implemented");
            if(item!=null) addItem( item );
        }
        return null;
    }
    
    /**
     * This method is called as a result of changing the underlying object of the ListItem.
     * This is package level and should not be called except by the ListItem.
     */
    final void replaceSelectedItem(Object oldItem, Object newItem) {
        onReplaceItem( oldItem, newItem );
    }
    
    /**
     * this method is called when double clicking an item in a grid or force called by the
     * developer. When invoked, it calls the onOpenItem method which needs to be implemented
     * by the developer.
     */
    public final Object openSelectedItem() {
        if(selectedItem.getItem()==null) return null;
        return onOpenItem( selectedItem.getItem(), selectedColumn );
    }
    
    /**
     * this method is called when there are changes in the row made and
     *
     */
    public final void updateSelectedItem() {
        if(selectedItem.getItem()!=null) {
            onUpdateItem( selectedItem.getItem() );
        }
    }
    
    /**
     * this method removes the item based on the selected item.
     * It can sucessfully call remove it the selected ListItem has a state==1.
     * which means it already exists. If the method is successfully called
     * it triggers the onRemoveItem method which must be implemented by the developer.
     */
    public final Object removeSelectedItem() {
        ListItem item = getSelectedItem();
        if(item==null) return null;
        if( item.getState() != 1 ) return null;
        if(item.getItem()==null)
            throw new IllegalStateException("remove item error. Cannot remove a null item");
        if(item.getState()==1) {
            removeItem( item.getItem() );
        }
        return null;
    }
    /**
     * this is called to get the list items
     */
    public final List<ListItem> getItemList() {
        return items;
    }
    
    protected void clearList() {
        items.clear();
    }
    
    //call back methods need to be supplied by the client.
    public Object onOpenItem( Object o, String columnName ) {
        //do nothing
        return null;
    }
    
    public void onAddItem(Object o) {
        throw new IllegalStateException("Error add item. onAddItem(Object item) must be implemented.");
    }
    
    public void onUpdateItem(Object o) {
        //do nothing.
    }
    
    public void onRemoveItem(Object o) {
        throw new IllegalStateException("Error remove item. onRemoveItem(Object item) must be implemented.");
    }
    
    public void onReplaceItem( Object oldValue, Object o ) {
        //throw new IllegalStateException("Error ListItem.setItem. onReplaceItem(Object oldItem,Object newItem) must be implemented.");
    }
    
    /**
     * this must be implemented by the developers.
     * The Map parameter contains the parameter values like
     * start and rowsize. Parameter values coming from the model
     * are identified by _ (underscored) prefix. This is to
     * differentiate from other parameters.
     */
    public abstract List fetchList(Map o);
    
    /**
     * This method must be overridden if the developer wants allows
     * the control to allow adding on new items.
     */
    public Object createItem() {
        return null;
    }
    
    public abstract void addItem( Object item );
    public abstract void removeItem( Object item );
    
    
    //key related functions: up and down beyond the list.
    //These are overridable methods
    public abstract void moveNextRecord();
    public abstract void moveBackRecord();
    public abstract void moveFirstPage();
    public abstract void moveNextPage();
    public abstract void moveBackPage();
    public abstract void setTopRow(int row);
    public abstract int getTopRow();
    public abstract int getMaxRows();
    
    /**
     * The implementing model determines how to calculate this value.
     * -1 means row count is not determined.
     */
    public abstract int getRowCount();
    
    
    //overridable. throw exception if there is validation error
    public void validate(ListItem item) {
        
    }
    
    protected List getDataList() {
        return dataList;
    }
    
    
    /**
     * this is called during unbinding
     */
    public void destroy() {
        listener = null;
    }
    
    public final void checkItem( Object item, boolean checked ) {
        if( checked ) {
            checkedItems.add( item );
        } else {
            checkedItems.remove(item);
        }
    }
    
    public StyleRule[] getStyleRules() {
        return null;
    }
    
    public Set getCheckedItems() {
        return checkedItems;
    }
    
    
}
