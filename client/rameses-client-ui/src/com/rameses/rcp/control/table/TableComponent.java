package com.rameses.rcp.control.table;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.ListModelListener;
import com.rameses.rcp.common.SubListModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import com.rameses.rcp.control.XTable;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JRootPane;

/**
 *
 * @author jaycverg
 */
public class TableComponent extends JTable implements ListModelListener {
    
    private static final String COLUMN_POINT = "COLUMN_POINT";
    private static final KeyListener KEY_LISTENER = new TableKeyAdapter();
    
    private DefaultTableModel tableModel;
    private Map<Integer, JComponent> editors = new HashMap();
    private Binding itemBinding = new Binding();
    private TableListener listener;
    private AbstractListModel listModel;
    
    private boolean required = false;
    private boolean editingMode = false;
    private boolean editorBeanLoaded = false;
    private boolean rowCommited = true;
    private JComponent currentEditor;
    private KeyEvent currentKeyEvent;
    
    private XTable grid;
    
    
    public TableComponent(XTable grid) {
        this.grid = grid;
        tableModel = new DefaultTableModel();
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(TableManager.getHeaderRenderer());
        addKeyListener(KEY_LISTENER);
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                hideEditor(false);
            }
        });
        
        int cond = super.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0);
        getInputMap(cond).put(enter, "selectNextColumnCell");
        
        KeyStroke shiftEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 1);
        getInputMap(cond).put(shiftEnter, "selectPreviousColumnCell");
        
        EnterAction ea = new EnterAction(this);
        registerKeyboardAction(ea, ea.keyStroke, JComponent.WHEN_FOCUSED);
        
        setAutoResizeMode(super.AUTO_RESIZE_LAST_COLUMN);
    }
    
    public void setListModel(AbstractListModel listModel) {
        this.listModel = listModel;
        listModel.setListener(this);
        tableModel.setListModel(listModel);
        setModel(tableModel);
        initTable();
    }
    
    public AbstractListModel getListModel() {
        return listModel;
    }
    
    public void setListener(TableListener listener) {
        this.listener = listener;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public boolean isEditingMode() {
        return editingMode;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initialize table  ">
    private void initTable() {
        removeAll(); //remove all editors
        editors.clear(); //clear column editors map
        required = false; //reset flag to false
        int length = tableModel.getColumnCount();
        
        for ( int i=0; i<length; i++ ) {
            Column col = tableModel.getColumn(i);
            TableCellRenderer cellRenderer = TableManager.getCellRenderer(col.getType());
            TableColumn tableCol = getColumnModel().getColumn(i);
            tableCol.setCellRenderer(cellRenderer);
            applyColumnProperties(tableCol, col);
            
            if ( !(listModel instanceof SubListModel) ) {
                col.setEditable(false);
                continue;
            }
            
            if ( !col.isEditable() ) continue;
            if ( editors.containsKey(i) ) continue;
            
            JComponent editor = TableManager.createCellEditor(col);
            editor.setVisible(false);
            editor.setBounds(-10, -10, 10, 10);
            editor.addFocusListener( new EditorFocusSupport() );
            
            if ( TableManager.hideOnEnter(editor) ) {
                addKeyboardAction(editor, KeyEvent.VK_ENTER, true);
            }
            
            addKeyboardAction(editor, KeyEvent.VK_TAB, true);
            addKeyboardAction(editor, KeyEvent.VK_ESCAPE, false);
            
            UIInput input = (UIInput) editor;
            editor.setName(col.getName());
            input.setBinding(itemBinding);
            itemBinding.register(input);
            
            if ( input instanceof Validatable ) {
                Validatable vi = ((Validatable) input);
                vi.setRequired(col.isRequired());
                vi.setCaption(col.getCaption());
                
                if ( vi.isRequired() ) required = true;
            }
            
            editors.put(i, editor);
            add(editor);
        }
        itemBinding.init(); //initialize item binding
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
    
    
    //<editor-fold defaultstate="collapsed" desc="  JTable properties  ">
    public void setTableHeader(JTableHeader tableHeader) {
        super.setTableHeader(tableHeader);
        tableHeader.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                hideEditor(false);
            }
        });
    }
    
    public boolean editCellAt(int rowIndex, int colIndex, EventObject e) {
        if (e instanceof MouseEvent) {
            MouseEvent me = (MouseEvent) e;
            if (me.getClickCount() != 2) return false;
        }
        
        Column dc = tableModel.getColumn(colIndex);
        if (dc == null) return false;
        if (!dc.isEditable()) {
            if ( e instanceof MouseEvent) openItem();
            return false;
        }
        
        ListItem item = listModel.getSelectedItem();
        if ( item.getItem() == null ) return false;
        
        JComponent editor = editors.get(colIndex);
        if ( editor == null ) return false;
        
        listener.editCellAt(rowIndex, colIndex);
        showEditor(editor, rowIndex, colIndex, e);
        return false;
    }
    
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        int oldRowIndex = getSelectedRow();
        if (editingMode) {
            Point point = (Point) currentEditor.getClientProperty(COLUMN_POINT);
            if (rowIndex != point.y || columnIndex != point.x) {
                hideEditor(currentEditor, point.y, point.x, true);
            }
        }
        
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
        
        if ( rowIndex != oldRowIndex ) {
            rowChanged();
        }
    }
    
    protected void processKeyEvent(KeyEvent e) {
        currentKeyEvent = e;
        super.processKeyEvent(e);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void openItem() {
        if ( listener != null ) listener.openItem();
    }
    
    private boolean isPrintableKey() {
        KeyEvent ke = currentKeyEvent;
        
        if ( ke.isActionKey() || ke.isControlDown() || ke.isAltDown() ) return false;
        switch( ke.getKeyCode() ) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_ENTER:
                return false;
        }
        
        return true;
    }
    
    private boolean isEditKey() {
        switch ( currentKeyEvent.getKeyCode() ) {
            case KeyEvent.VK_F2:
            case KeyEvent.VK_INSERT:
            case KeyEvent.VK_BACK_SPACE:
                return true;
        }
        
        return false;
    }
    
    private void highLight(JComponent comp) {
        if ( comp instanceof JTextComponent )
            ((JTextComponent) comp).selectAll();
    }
    
    private void addKeyboardAction(JComponent comp, int key, boolean commit) {
        KeyBoardAction kba = new KeyBoardAction(comp, key, commit);
        comp.registerKeyboardAction(kba, kba.keyStroke, JComponent.WHEN_FOCUSED);
    }
    
    private void focusNextCellFrom(int rowIndex, int colIndex) {
        int nextCol = findNextEditableColFrom(colIndex);
        int firstEditable = findNextEditableColFrom(-1);
        SubListModel slm = (SubListModel) listModel;
        
        if ( nextCol >= 0 ) {
            this.changeSelection(rowIndex, nextCol, false, false);
        } else if (rowIndex+1 < tableModel.getRowCount()) {
            this.changeSelection(rowIndex+1, firstEditable, false, false);
        } else {
            ListItem item = slm.getSelectedItem();
            boolean lastRow = !(rowIndex + slm.getTopRow() < slm.getMaxRows());
            
            if ( item.getState() == 0 ) lastRow = false;
            
            if ( !lastRow ) {
                this.changeSelection(rowIndex, firstEditable, false, false);
                moveNextRecord();
            } else {
                this.changeSelection(0, firstEditable, false, false);
                listModel.moveFirstPage();
            }
        }
    }
    
    private int findNextEditableColFrom(int colIndex) {
        for (int i = colIndex + 1; i < tableModel.getColumnCount(); i++ ) {
            if ( editors.get(i) != null ) return i;
        }
        return -1;
    }
    
    private void hideEditor(boolean commit) {
        if ( !editingMode || currentEditor == null ) return;
        Point point = (Point) currentEditor.getClientProperty(COLUMN_POINT);
        hideEditor(currentEditor, point.y, point.x, commit);
    }
    
    private void hideEditor(JComponent editor, int rowIndex, int colIndex, boolean commit) {
        if ( !commit ) {
            editor.setInputVerifier(null);
        }
        
        editor.setVisible(false);
        editingMode = false;
        currentEditor = null;
        editor.setInputVerifier(null);
        
        tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
        grabFocus();
    }
    
    private boolean validateRow(int rowIndex) {
        SubListModel slm = (SubListModel) listModel;
        ActionMessage ac = new ActionMessage();
        itemBinding.validate(ac);
        if ( ac.hasMessages() ) {
            slm.addErrorMessage(rowIndex, ac.toString());
        } else {
            slm.removeErrorMessage(rowIndex);
        }
        
        return !ac.hasMessages();
    }
    
    private void showEditor(JComponent editor, int rowIndex, int colIndex, EventObject e) {
        Rectangle bounds = getCellRect(rowIndex, colIndex, false);
        editor.putClientProperty(COLUMN_POINT, new Point(colIndex, rowIndex));
        editor.setBounds(bounds);
        
        UIInput input = (UIInput) editor;
        boolean refreshed = false;
        if ( !editorBeanLoaded ) {
            Object bean = listModel.getSelectedItem();
            itemBinding.setBean(bean);
            itemBinding.refresh();
            refreshed = true;
        }
        
        if ( e instanceof MouseEvent || isEditKey() ) {
            if ( !refreshed ) input.refresh();
            highLight(editor);
        } else if ( isPrintableKey() ) {
            input.setValue( currentKeyEvent );
        } else {
            return;
        }
        
        InputVerifier verifier = (InputVerifier) editor.getClientProperty(InputVerifier.class);
        if ( verifier == null ) {
            verifier = editor.getInputVerifier();
            editor.putClientProperty(InputVerifier.class, verifier);
        }
        
        editor.setInputVerifier( verifier );
        editor.setVisible(true);
        editor.grabFocus();
        editingMode = true;
        rowCommited = false;
        currentEditor = editor;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  list model listener methods  ">
    public void refreshList() {
        if ( !rowCommited ) rowChanged();
        if ( editingMode ) hideEditor(false);
        
        ListItem item = listModel.getSelectedItem();
        int col = getSelectedColumn();
        tableModel.fireTableDataChanged();
        if(item!=null) {
            super.setRowSelectionInterval(item.getIndex(),item.getIndex());
            if ( col >= 0 ) super.setColumnSelectionInterval(col, col);
            
        }
        if ( listener != null ) {
            listener.refreshList();
        }
        
        editorBeanLoaded = false;
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
    
    public void refreshSelectedItem() {
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  row movements support  ">
    public void movePrevRecord() {
        if ( getSelectedRow() == 0 ) {
            rowChanged();
            listModel.moveBackRecord();
        }
    }
    
    public void moveNextRecord() {
        if ( getSelectedRow() == getRowCount() - 1 ) {
            rowChanged();
            listModel.moveNextRecord();
        }
    }
    
    public void rowChanged() {
        if ( !rowCommited ) {
            ListItem item = listModel.getSelectedItem();
            int oldRowIndex = item.getIndex();
            if ( validateRow(oldRowIndex) && item.getState() == 0 ) {
                listModel.addCreatedItem();
            }
        }
        
        listModel.setSelectedItem(getSelectedRow());
        editorBeanLoaded = false;
        rowCommited = true;
        listener.rowChanged();
    }
    //</editor-fold>
    
    
    
    //<editor-fold defaultstate="collapsed" desc="  EditorFocusSupport (class)  ">
    private class EditorFocusSupport implements FocusListener {
        
        public void focusGained(FocusEvent e) {}
        
        public void focusLost(FocusEvent e) {
            if ( !e.isTemporary() ) {
                hideEditor(true);
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  TableKeyAdapter (class)  ">
    private static class TableKeyAdapter extends KeyAdapter {
        
        public void keyPressed(KeyEvent e) {
            if ( !(e.getSource() instanceof TableComponent) ) return;
            TableComponent table = (TableComponent) e.getSource();
            AbstractListModel model = table.getListModel();
            
            switch( e.getKeyCode() ) {
                case KeyEvent.VK_DOWN:
                    table.moveNextRecord();
                    break;
                case KeyEvent.VK_UP:
                    table.movePrevRecord();
                    break;
                case KeyEvent.VK_PAGE_DOWN:
                    model.moveNextPage();
                    break;
                case KeyEvent.VK_PAGE_UP:
                    model.moveBackPage();
                    break;
                case KeyEvent.VK_DELETE:
                    model.removeSelectedItem();
                    break;
                case KeyEvent.VK_ENTER:
                    if ( e.isControlDown() ) table.openItem();
                    break;
                case KeyEvent.VK_HOME:
                    if ( e.isControlDown() ) model.moveFirstPage();
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  KeyBoardAction (class) ">
    private class KeyBoardAction implements ActionListener {
        
        KeyStroke keyStroke;
        private boolean commit;
        private ActionListener origAction;
        
        KeyBoardAction(JComponent comp, int key, boolean commit) {
            this.commit = commit;
            this.keyStroke = KeyStroke.getKeyStroke(key, 0);
            origAction = comp.getActionForKeyStroke(keyStroke);
        }
        
        public void actionPerformed(ActionEvent e) {
            JComponent comp = (JComponent) e.getSource();
            Point point = (Point) comp.getClientProperty(COLUMN_POINT);
            
            if ( origAction != null ) origAction.actionPerformed(e);
            
            if ( commit ) {
                focusNextCellFrom( point.y, point.x );
            } else {
                hideEditor(comp, point.y, point.x, false);
            }
            
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  EnterAction (class)  ">
    private class EnterAction implements ActionListener {
        
        KeyStroke keyStroke;
        private JComponent component;
        private ActionListener origAction;
        
        EnterAction(JComponent component) {
            this.component = component;
            keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
            origAction = component.getActionForKeyStroke(keyStroke);
        }
        
        public void actionPerformed(ActionEvent e) {
            JRootPane rp = component.getRootPane();
            if (rp != null && rp.getDefaultButton() != null ) {
                JButton btn = rp.getDefaultButton();
                btn.doClick();
            } else {
                origAction.actionPerformed(e);
            }
        }
    }
    //</editor-fold>
}
