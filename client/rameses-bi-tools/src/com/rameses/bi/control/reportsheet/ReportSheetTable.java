/*
 * ReportSheetTable.java
 *
 * Created on January 31, 2011
 * @author jaycverg
 */

package com.rameses.bi.control.reportsheet;

import com.rameses.bi.common.ReportSheetModel;
import com.rameses.rcp.common.*;
import com.rameses.rcp.framework.Binding;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class ReportSheetTable extends JTable implements ListModelListener {
    
    private static final String COLUMN_POINT = "COLUMN_POINT";
    
    private ReportSheetTableModel tableModel;
    private ReportSheetListener tableListener;
    private ReportSheetModel listModel;
    private ListSelectionListener selectionListener;
    
    //row background color options
    private Color evenBackground;
    private Color oddBackground;
    private Color errorBackground = Color.PINK;
    
    //row foreground color options
    private Color evenForeground;
    private Color oddForeground;
    private Color errorForeground = Color.BLACK;
    
    private Binding binding;
    
    private boolean multiselect;
    
    
    public ReportSheetTable() {
        initComponents();
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents  ">
    private void initComponents() {
        selectionListener = new SheetSelectionListener();
        
        addKeyListener(new TableKeyAdapter());
        
        int cond = super.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
        getInputMap(cond).put(enter, "selectNextColumnCell");
        
        KeyStroke shiftEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 1);
        getInputMap(cond).put(shiftEnter, "selectPreviousColumnCell");
        
        setMultiselect(true);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setListModel(ReportSheetModel listModel) {
        this.listModel = listModel;
        listModel.setListener(this);
        tableModel = new ReportSheetTableModel(listModel);
        setModel(tableModel);
        buildColumns();
    }
    
    public ReportSheetModel getListModel() { return listModel; }
    
    public Binding getBinding()             { return binding; }
    public void setBinding(Binding binding) { this.binding = binding; }
    
    public void setListener(ReportSheetListener listener) { this.tableListener = listener; }
    
    public boolean isAutoResize() {
        return getAutoResizeMode() != super.AUTO_RESIZE_OFF;
    }
    
    public void setAutoResize(boolean autoResize) {
        if ( autoResize ) {
            setAutoResizeMode(super.AUTO_RESIZE_LAST_COLUMN);
        } else {
            setAutoResizeMode(super.AUTO_RESIZE_OFF);
        }
    }
    
    public Color getEvenBackground()                    { return evenBackground; }
    public void setEvenBackground(Color evenBackground) { this.evenBackground = evenBackground; }
    
    public Color getOddBackground()                   { return oddBackground; }
    public void setOddBackground(Color oddBackground) { this.oddBackground = oddBackground; }
    
    public Color getErrorBackground()                     { return errorBackground; }
    public void setErrorBackground(Color errorBackground) { this.errorBackground = errorBackground; }
    
    public Color getEvenForeground()                    { return evenForeground; }
    public void setEvenForeground(Color evenForeground) { this.evenForeground = evenForeground; }
    
    public Color getOddForeground()                   { return oddForeground; }
    public void setOddForeground(Color oddForeground) { this.oddForeground = oddForeground; }
    
    public Color getErrorForeground()                     { return errorForeground; }
    public void setErrorForeground(Color errorForeground) { this.errorForeground = errorForeground; }
    
    public boolean isMultiselect() { return multiselect; }
    public void setMultiselect(boolean multiselect) {
        if( this.multiselect != multiselect ) {
            this.multiselect = multiselect;
            
            ListSelectionModel old = getSelectionModel();
            old.removeListSelectionListener(selectionListener);
            
            if( multiselect ) {
                setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            } else {
                setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            }
            
            ListSelectionModel model = getSelectionModel();
            model.addListSelectionListener(selectionListener);
        }
    }
    
    public String getVarStatus()            { return tableModel.getVarStatus(); }
    public void setVarStatus(String status) { tableModel.setVarStatus(status); }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  buildColumns  ">
    private void buildColumns() {
        int length = tableModel.getColumnCount();
        for ( int i=0; i<length; i++ ) {
            Column col = tableModel.getColumn(i);
            TableCellRenderer cellRenderer = ReportSheetUtil.getCellRenderer(col.getType());
            TableColumn tableCol = getColumnModel().getColumn(i);
            tableCol.setCellRenderer(cellRenderer);
            applyColumnProperties(tableCol, col);
        }
    }
    
    private void applyColumnProperties(TableColumn tc, Column c) {
        if ( c.getMaxWidth() > 0 ) tc.setMaxWidth( c.getMaxWidth() );
        if ( c.getMinWidth() > 0 ) tc.setMinWidth( c.getMinWidth() );
        
        if ( c.getWidth() > 0 ) {
            tc.setWidth( c.getWidth() );
            tc.setPreferredWidth( c.getWidth() );
        }
        
        tc.setResizable( c.isResizable() );
    }
    //</editor-fold>
    
    
    protected JTableHeader createDefaultTableHeader() {
        return new ReportSheetHeader(columnModel);
    }
    
    public Object getSelectedValue() {
        if( !multiselect ) {
            return listModel.getSelectedItem().getItem();
        } else {
            int[] rows = getSelectedRows();
            List list = new ArrayList(rows.length);
            for(int r : rows) {
                Object item = listModel.getItemList().get(r).getItem();
                if( item != null ) list.add( item );
            }
            return list;
        }
    }
    
    protected void processMouseEvent(MouseEvent me) {
        if ( me.getClickCount() == 2 ) {
            Point p = new Point(me.getX(), me.getY());
            int colIndex = columnAtPoint(p);
            Column dc = tableModel.getColumn(colIndex);
            if ( dc != null && !dc.isEditable() && me.getID() == MouseEvent.MOUSE_PRESSED ) {
                me.consume();
                openItem();
                return;
            }
        }
        
        super.processMouseEvent(me);
    }
    
    private void openItem() {
        if ( tableListener != null ) {
            try {
                tableListener.openItem();
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void refreshList() {
        ListItem item = listModel.getSelectedItem();
        int col = getSelectedColumn();
        tableModel.fireTableDataChanged();
        if(item!=null) {
            super.setRowSelectionInterval(item.getIndex(),item.getIndex());
            if ( col >= 0 ) super.setColumnSelectionInterval(col, col);
            
        }
        if ( tableListener != null ) {
            tableListener.refreshList();
        }
    }
    
    public void refreshItemUpdated(int row) {
        tableModel.fireTableRowsUpdated(row, row);
    }
    
    public void refreshItemAdded(int fromRow, int toRow) {
        tableModel.fireTableRowsInserted(fromRow, toRow);
    }
    
    public void refreshItemRemoved(int fromRow, int toRow) {
        tableModel.fireTableRowsUpdated(fromRow, toRow);
    }
    
    public void refreshSelectedItem() {}
    
    public void rebuildColumns() {
        tableModel = new ReportSheetTableModel(listModel);
        setModel(tableModel);
        buildColumns();
    }
    
    public void movePrevRecord() {
        if ( getSelectedRow() == 0 ) {
            listModel.moveBackRecord();
        }
    }
    
    public void moveNextRecord() {
        if ( getSelectedRow() == getRowCount() - 1 ) {
            listModel.moveNextRecord();
        }
    }
    
    public void rowChanged() {
        listModel.setSelectedItem( getSelectedRow() );
        tableListener.rowChanged();
    }
    
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        listModel.setSelectedColumnIndex(columnIndex);
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  SheetSelectionListener (class)  ">
    private class SheetSelectionListener implements ListSelectionListener {
        
        public void valueChanged(ListSelectionEvent e) {
            if( e.getValueIsAdjusting() ) return;
            rowChanged();
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  TableKeyAdapter (class)  ">
    private class TableKeyAdapter extends KeyAdapter {
        
        public void keyPressed(KeyEvent e) {
            switch( e.getKeyCode() ) {
                case KeyEvent.VK_DOWN:
                    moveNextRecord();
                    break;
                case KeyEvent.VK_UP:
                    movePrevRecord();
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    listModel.moveNextPage();
                    break;
                case KeyEvent.VK_PAGE_UP:
                    listModel.moveBackPage();
                    break;
                case KeyEvent.VK_DELETE:
                    listModel.removeSelectedItem();
                    break;
                case KeyEvent.VK_ENTER:
                    if ( e.isControlDown() ) openItem();
                    break;
                case KeyEvent.VK_HOME:
                    if ( e.isControlDown() ) listModel.moveFirstPage();
            }
        }
        
    }
    //</editor-fold>
    
}
