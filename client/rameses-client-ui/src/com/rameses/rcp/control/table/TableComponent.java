package com.rameses.rcp.control.table;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.ListModelListener;
import com.rameses.rcp.common.SubListModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ChangeLog;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import java.awt.event.FocusListener;
import javax.swing.JButton;
import javax.swing.JRootPane;

/**
 *
 * @author jaycverg
 */
public class TableComponent extends JTable implements ListModelListener {
    
    private static final String COLUMN_POINT = "COLUMN_POINT";
    
    private DefaultTableModel tableModel;
    private Map<Integer, JComponent> editors = new HashMap();
    private Binding itemBinding = new Binding();
    private TableListener tableListener;
    private AbstractListModel listModel;
    
    private boolean readonly;
    private boolean required;
    private boolean editingMode;
    private boolean editorBeanLoaded;
    private boolean rowCommited = true;
    private JComponent currentEditor;
    private KeyEvent currentKeyEvent;
    
    //row background color options
    private Color evenBackground;
    private Color oddBackground;
    private Color errorBackground = Color.PINK;
    
    //row foreground color options
    private Color evenForeground;
    private Color oddForeground;
    private Color errorForeground;
    
    
    public TableComponent() {
        initComponents();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initComponents  ">
    private void initComponents() {
        tableModel = new DefaultTableModel();
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(TableManager.getHeaderRenderer());
        addKeyListener(new TableKeyAdapter());
        
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
        
        TableEnterAction ea = new TableEnterAction(this);
        registerKeyboardAction(ea, ea.keyStroke, JComponent.WHEN_FOCUSED);
        
        //row editing ctrl+Z support
        KeyStroke ctrlZ = KeyStroke.getKeyStroke("ctrl Z");
        registerKeyboardAction(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                if ( !rowCommited ) {
                    int row = getSelectedRow();
                    ChangeLog log = itemBinding.getChangeLog();
                    if ( log.hasChanges() ) {
                        log.undo();
                        tableModel.fireTableRowsUpdated(row, row);
                    }
                    //clear row editing flag of everything is undone
                    if ( !log.hasChanges() ) {
                        rowCommited = true;
                        tableListener.cancelRowEdit();
                    }
                }
            }
            
        }, ctrlZ, JComponent.WHEN_FOCUSED);
        
        setAutoResize(true);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setListModel(AbstractListModel listModel) {
        this.listModel = listModel;
        listModel.setListener(this);
        tableModel.setListModel(listModel);
        setModel(tableModel);
        buildColumns();
    }
    
    public AbstractListModel getListModel() {
        return listModel;
    }
    
    public void setListener(TableListener listener) {
        this.tableListener = listener;
    }
    
    public boolean isRequired() {
        return required;
    }
    
    public boolean isEditingMode() {
        return editingMode;
    }
    
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
    
    public boolean isReadonly() {
        return readonly;
    }
    
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
    
    public Color getEvenBackground() {
        return evenBackground;
    }
    
    public void setEvenBackground(Color evenBackground) {
        this.evenBackground = evenBackground;
    }
    
    public Color getOddBackground() {
        return oddBackground;
    }
    
    public void setOddBackground(Color oddBackground) {
        this.oddBackground = oddBackground;
    }
    
    public Color getErrorBackground() {
        return errorBackground;
    }
    
    public void setErrorBackground(Color errorBackground) {
        this.errorBackground = errorBackground;
    }
    
    public Color getEvenForeground() {
        return evenForeground;
    }
    
    public void setEvenForeground(Color evenForeground) {
        this.evenForeground = evenForeground;
    }
    
    public Color getOddForeground() {
        return oddForeground;
    }
    
    public void setOddForeground(Color oddForeground) {
        this.oddForeground = oddForeground;
    }
    
    public Color getErrorForeground() {
        return errorForeground;
    }
    
    public void setErrorForeground(Color errorForeground) {
        this.errorForeground = errorForeground;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  buildColumns  ">
    private void buildColumns() {
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
            
            addKeyboardAction(editor, KeyEvent.VK_ENTER, true);
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
    
    private void addKeyboardAction(JComponent comp, int key, boolean commit) {
        EditorKeyBoardAction kba = new EditorKeyBoardAction(comp, key, commit);
        comp.registerKeyboardAction(kba, kba.keyStroke, JComponent.WHEN_FOCUSED);
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
    
    public boolean editCellAt(int rowIndex, int colIndex, EventObject e) {
        if ( readonly ) return false;
        
        MouseEvent me = null;
        if (e instanceof MouseEvent) {
            me = (MouseEvent) e;
            if (me.getClickCount() != 2) return false;
        }
        
        Column dc = tableModel.getColumn(colIndex);
        if (dc == null) return false;
        
        ListItem item = listModel.getSelectedItem();
        if ( item.getItem() == null ) return false;
        
        JComponent editor = editors.get(colIndex);
        if ( editor == null ) return false;
        
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
        if ( readonly ) return;
        
        if ( tableListener != null ) {
            tableListener.openItem();
        }
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
        if ( comp instanceof JTextComponent ) {
            ((JTextComponent) comp).selectAll();
        }
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
        for (int i = colIndex+1; i < tableModel.getColumnCount(); i++ ) {
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
            ListItem item = listModel.getItemList().get(rowIndex);
            if ( item.getItem() != null ) {
                ((UIInput) editor).refresh();
            }
        }
        
        editor.setVisible(false);
        editor.setInputVerifier(null);
        editingMode = false;
        currentEditor = null;
        
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
            itemBinding.update(); //clear change log
            Object bean = listModel.getSelectedItem();
            itemBinding.setBean(bean);
            itemBinding.refresh();
            refreshed = true;
            editorBeanLoaded = true;
        }
        
        if ( e instanceof MouseEvent || isEditKey() ) {
            if ( !refreshed ) {
                input.refresh();
            }
            highLight(editor);
        } else if ( isPrintableKey() ) {
            input.setValue( currentKeyEvent );
        } else {
            return;
        }
        
        tableListener.editCellAt(rowIndex, colIndex);
        
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
        if ( editingMode ) hideEditor(false);
        if ( !rowCommited ) {
            rowCommited = true;
            rowChanged();
        }
        
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
            ListItem item = (ListItem) itemBinding.getBean();
            int oldRowIndex = item.getIndex();
            if ( validateRow(oldRowIndex) && item.getState() == 0 ) {
                listModel.addCreatedItem();
            }
        }
        
        listModel.setSelectedItem(getSelectedRow());
        editorBeanLoaded = false;
        rowCommited = true;
        tableListener.rowChanged();
    }
    
    public void cancelRowEdit() {
        if ( !rowCommited ) {
            itemBinding.getChangeLog().undoAll();
            rowCommited = true;
            int row = getSelectedRow();
            tableModel.fireTableRowsUpdated(row, row);
            tableListener.cancelRowEdit();
        }
    }
    //</editor-fold>
    
    
    
    //<editor-fold defaultstate="collapsed" desc="  EditorFocusSupport (class)  ">
    private class EditorFocusSupport implements FocusListener {
        
        private boolean fromTempFocus;
        
        public void focusGained(FocusEvent e) {
            if ( fromTempFocus ) {
                if ( editingMode ) {
                    hideEditor(true);
                    JComponent comp = (JComponent) e.getSource();
                    Point point = (Point) comp.getClientProperty(COLUMN_POINT);
                    focusNextCellFrom(point.y, point.x);
                }
                fromTempFocus = false;
            }
        }
        
        public void focusLost(FocusEvent e) {
            fromTempFocus = e.isTemporary();
            
            if ( !e.isTemporary() ) {
                hideEditor(true);
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  EditorKeyBoardAction (class) ">
    private class EditorKeyBoardAction implements ActionListener {
        
        KeyStroke keyStroke;
        private boolean commit;
        private ActionListener[] listeners;
        
        EditorKeyBoardAction(JComponent comp, int key, boolean commit) {
            this.commit = commit;
            this.keyStroke = KeyStroke.getKeyStroke(key, 0);
            
            //hold only action on enter key
            //this is usually used by lookup
            if ( key == KeyEvent.VK_ENTER && comp instanceof JTextField ) {
                JTextField jtf = (JTextField) comp;
                listeners = jtf.getActionListeners();
            }
        }
        
        public void actionPerformed(ActionEvent e) {
            if ( listeners != null && listeners.length > 0 ) {
                for ( ActionListener l: listeners) {
                    l.actionPerformed(e);
                }
                
            } else {
                JComponent comp = (JComponent) e.getSource();
                Point point = (Point) comp.getClientProperty(COLUMN_POINT);
                if ( commit ) {
                    focusNextCellFrom( point.y, point.x );
                } else {
                    hideEditor(comp, point.y, point.x, false);
                }
            }
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
                    break;
                case KeyEvent.VK_ESCAPE:
                    cancelRowEdit();
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  TableEnterAction (class)  ">
    private class TableEnterAction implements ActionListener {
        
        KeyStroke keyStroke;
        private JComponent component;
        private ActionListener origAction;
        
        TableEnterAction(JComponent component) {
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
