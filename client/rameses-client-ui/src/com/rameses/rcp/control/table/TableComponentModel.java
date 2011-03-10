/*
 * TableComponentModel.java
 *
 * Created on June 30, 2010, 10:54 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.common.PropertyResolver;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableComponentModel extends AbstractTableModel implements TableControlModel {
    
    private AbstractListModel listModel;
    private List<Column> columnList = new ArrayList();
    
    
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
        if ( item.getItem() != null ) {
            String name = columnList.get(columnIndex).getName();
            return resolver.getProperty(item, name);
        }
        return null;
    }
    
    public String getColumnName(int column) {
        return columnList.get(column).getCaption();
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
}
