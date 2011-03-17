package com.rameses.rcp.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * This class makes use of a cached list.
 */
public abstract class SubListModel extends PageListModel {

    
    protected void fetch() {
        if( dataList ==null) {
            Map map = new HashMap();
            map.put("_search", search);
            dataList = fetchList(map);
            if(dataList==null) dataList = new ArrayList();
            
            maxRows = dataList.size()-1;
            if(isAllocNewRow()) maxRows = maxRows + 1;
        }
        
        
        //reset the force load.
        List subList = null;
        if( dataList.size() > 0 ) {
            int tail = toprow + getRows();
            if( tail > dataList.size() ) tail = dataList.size();
            subList = dataList.subList(toprow, tail);
        } else {
            subList = new ArrayList();
        }
        fillListItems(subList,toprow);
        
        if(selectedItem!=null) {
            pageIndex = (selectedItem.getRownum()/ getRows())+1;
        } else {
            pageIndex = 1;
            setSelectedItem(0);
        }
        pageCount = ((maxRows+1) / getRows()) + (((maxRows+1) % getRows()>0)?1:0);
    }
    
    public final void addItem(Object item) {
        if( item instanceof ListItem )
            throw new IllegalStateException("SubListModel.addItem error. Item passed must not be a ListItem");
        
        onAddItem(item);
        dataList = null;
        refresh();
    }
    
    
    public final void removeItem(Object item) {
        if( item instanceof ListItem )
            throw new IllegalStateException("SubListModel.removeItem error. Item passed must not be a ListItem");
        
        onRemoveItem( item );
        dataList = null;
        refresh();
        //check the selectedItem if 0, then move to previous
        if(getSelectedItem().getIndex()>0 && getSelectedItem().getState()==0) {
            setSelectedItem( items.get(getSelectedItem().getIndex()-1) );
        }
    }
    
}
