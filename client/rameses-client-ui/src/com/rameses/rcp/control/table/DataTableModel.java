/*
 * DataTableModel.java
 *
 * Created on January 31, 2011
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.common.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class DataTableModel extends AbstractTableModel implements TableControlModel {
    
    private AbstractListModel listModel;
    private List<Column> columnList = new ArrayList();
    private String varStatus;
    
    
    public void setListModel(AbstractListModel model) {
        listModel = model;
        columnList.clear();
        if ( listModel == null ) return;
        
        indexColumns();
    }
    
    private void indexColumns() {
        for ( Column col : listModel.getColumns() ) {
            if ( col.isVisible() ) {
                columnList.add(col);
            }
        }
    }
    
    public void reIndexColumns() {
        columnList.clear();
        indexColumns();
    }
    
    public AbstractListModel getListModel() {
        return listModel;
    }
    
    public int getRowCount() {
        return listModel.getItemList().size();
    }
    
    public Column getColumn(int index) {
        if (index >= 0 && index < columnList.size())
            return columnList.get(index);
        
        return null;
    }
    
    public int getColumnCount() {
        return columnList.size();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
        ListItem item = listModel.getItemList().get(rowIndex);
        if ( item != null ) {
            String name = columnList.get(columnIndex).getName();
            if( !ValueUtil.isEmpty(name) ) {
                if( varStatus == null); //do nothing
                else if( name.equals(varStatus) ) {
                    return item;
                } else if( name.startsWith(varStatus + ".") ) {
                    return resolver.getProperty(item, name.substring(name.indexOf(".")+1));
                }
                
                if( item.getItem() != null ) {
                    return resolver.getProperty(item.getItem(), name);
                }
            }
            
            return item.getItem();
        }
        return null;
    }
    
    public String getColumnName(int column) {
        return columnList.get(column).getCaption();
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public String getVarStatus() {
        return varStatus;
    }
    
    public void setVarStatus(String varStatus) {
        this.varStatus = varStatus;
    }
    
}
