/*
 * ListItem.java
 *
 * This class is re-used by GridComponent and SubItemController
 */

package com.rameses.rcp.common;


public class ListItem {
    
    private Object item;
    private int state;
    private AbstractListModel parent;
    private int index;
    private int rownum;
    private boolean selected;
    
    //new addition. this refers to the calling code
    private Object root;
    
    public ListItem() {
    }
    
    public final void setItem(Object newitem) {
        if(item==null && newitem==null) return;
        if(item!=null && item.equals(newitem)) return;
        try {
            //fire only replace if the previous item is not null.
            parent.replaceSelectedItem( item, newitem );
            this.item = newitem;
        } catch(Exception e) {
            MsgBox.err(e);
        }
    }
    
    public final Object getItem() {
        return item;
    }
    
    //this method is called only by the AbstractListModel ONLY.
    //during reload
    public final void loadItem(Object item) {
        this.item = item;
    }
    
    public AbstractListModel getParent() {
        return parent;
    }
    
    public void setParent(AbstractListModel parent) {
        this.parent = parent;
    }
    
    public int getState() {
        return state;
    }
    
    public void setState(int state) {
        this.state = state;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getRownum() {
        return rownum;
    }
    
    public void setRownum(int rowindex) {
        this.rownum = rowindex;
    }
    
    public boolean isSelected() {
        return selected;
    }
    
    public void setSelected(boolean selected) {
        if( item!=null) {
            this.selected = selected;
            parent.checkItem(this.item, selected);
        }
    }
    
    public void initSelected() {
        this.selected = false;
    }
    
    public final Object getRoot() {
        return root;
    }
    
    public final void setRoot(Object root) {
        this.root = root;
    }
    
    
    
}
