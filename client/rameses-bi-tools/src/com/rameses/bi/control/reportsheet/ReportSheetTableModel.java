/*
 * ReportSheetTableModel.java
 *
 * Created on January 31, 2011
 * @author jaycverg
 */

package com.rameses.bi.control.reportsheet;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.common.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;

public class ReportSheetTableModel extends AbstractTableModel {
    
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
                if( name.startsWith("_status") ) {
                    Map bean = new HashMap();
                    bean.put("_status", item);
                    return resolver.getProperty(bean, name);
                } else if( item.getItem() != null ) {
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
