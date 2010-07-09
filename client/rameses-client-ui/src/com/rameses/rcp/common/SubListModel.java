package com.rameses.rcp.common;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.PropertyResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * This class makes use of a cached list.
 */
public abstract class SubListModel extends PageListModel {
    
    private Column primaryColumn;
    private Map<Object, String> errorMessages = new HashMap();
    
    
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
        if(getSelectedItem().getIndex()>=0 && getSelectedItem().getState()==0) {
            setSelectedItem( items.get(getSelectedItem().getIndex()-1) );
        }
    }
    
    public void addErrorMessage(int liIndex, String message) {
        ListItem li = getItemList().get(liIndex);
        if ( li.getItem() == null ) return;
        
        errorMessages.put(getRowId(li), message);
    }
    
    public String getErrorMessage(int liIndex) {
        ListItem li = getItemList().get(liIndex);
        if ( li.getItem() == null ) return null;
        
        return errorMessages.get( getRowId(li) );
    }
    
    public void removeErrorMessage(int liIndex) {
        ListItem li = getItemList().get(liIndex);
        if ( li.getItem() == null ) return;
        
        errorMessages.remove( getRowId(li) );
    }
    
    public String getErrorMessages() {
        if ( errorMessages.size() == 0 ) return null;
        
        StringBuffer sb = new StringBuffer();
        boolean first = true;
        for (Map.Entry me: errorMessages.entrySet()) {
            if ( !first ) sb.append("\n");
            else first = false;
            sb.append("row " + me.getKey() + ": " + me.getValue());
        }
        
        return sb.toString();
    }
    
    private Object getRowId(ListItem li) {
        Column pc = getPrimaryColumn();
        if ( pc != null ) {
            PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
            String name = pc.getName();
            Object value = resolver.getProperty(li, name);
            if ( value != null ) return value;
        }
        
        return li.getItem();
    }
    
    public Column getPrimaryColumn() {
        if ( primaryColumn == null ) {
            Column[] cols = getColumns();
            if ( cols != null && cols.length > 0 ) {
                primaryColumn = cols[0];
                for ( Column c : cols ) {
                    if ( c.isPrimary() ) {
                        primaryColumn = c;
                        break;
                    }
                }
            }
        }
        return primaryColumn;
    }
}
