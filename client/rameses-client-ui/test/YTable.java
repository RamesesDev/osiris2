import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.Column;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.ListModelListener;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.util.PropertyResolver;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
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
import javax.swing.text.JTextComponent;
import com.rameses.rcp.control.table.TableManager;
import com.rameses.rcp.control.table.DefaultTableModel;

/**
 *
 * @author jaycverg
 */
public class YTable extends JTable implements UIControl, ListModelListener {
    
    private static final String COLUMN_POINT = "COLUMN_POINT";
    
    private String[] depends;
    private Binding binding;
    private int index;
    private DefaultTableModel tableModel;
    private Map<String, JComponent> editors = new HashMap();
    private Binding itemBinding = new Binding();
    private String handler;
    
    private AbstractListModel listModel;
    private boolean editingMode = false;
    private boolean beanLoaded = false;
    private boolean bottom = false;
    private boolean top = false;
    private JComponent currentEditor;
    private KeyEvent currentKeyEvent;
    
    
    
    public YTable() {
        tableModel = new DefaultTableModel();
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setDefaultRenderer(TableManager.getHeaderRenderer());
        addKeyListener(KEY_LISTENER);
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  UIControl Properties  ">
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public void refresh() {
        if ( listModel != null ) {
            listModel.refresh();
        }
    }
    
    public void load() {
        if ( handler != null ) {
            PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
            Object obj = resolver.getProperty(binding.getBean(), handler);
            if ( obj instanceof AbstractListModel ) {
                listModel = (AbstractListModel) obj;
            }
            listModel.setListener(this);
            tableModel.setListModel(listModel);
        }
        setModel(tableModel);
        initTable();
    }
    
    private void initTable() {
        removeAll(); //remove all editors
        editors.clear(); //clear column editors
        int length = tableModel.getColumnCount();
        for ( int i=0; i<length; i++ ) {
            Column dc = tableModel.getColumn(i);
            TableCellRenderer cellRenderer = TableManager.getCellRenderer(dc.getType());
            getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
            
            if ( !dc.isEditable() ) continue;
            
            if (!editors.containsKey(dc.getName())) {
                JComponent editor = TableManager.createCellEditor(dc.getType());
                editor.setVisible(false);
                editor.setBounds(-10, -10, 10, 10);
                editor.addFocusListener( new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        hideEditor(true);
                    }
                    
                });
                InputVerifier verifier = editor.getInputVerifier();
                editor.putClientProperty(InputVerifier.class, verifier);
                editor.setInputVerifier(null);
                editor.setName(dc.getName());
                
                UIInput input = (UIInput) editor;
                input.setBinding(itemBinding);
                itemBinding.register(input);
                if ( input instanceof Validatable ) {
                    Validatable vi = ((Validatable) input);
                    vi.setRequired(dc.isRequired());
                    vi.setCaption(dc.getCaption());
                }
                
                editors.put(dc.getName(), editor);
                add(editor);
            }
        }
        itemBinding.init(); //initialize item binding
    }
    
    public int compareTo(Object o) {
        if ( o == null || !(o instanceof UIControl) ) return 0;
        return this.index = ((UIControl) o).getIndex();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  JTable overrides  ">
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
        if (!dc.isEditable()) return false;
        
        JComponent editor = editors.get(dc.getName());
        if ( editor == null ) return false;
        
        Rectangle bounds = getCellRect(rowIndex, colIndex, false);
        editor.putClientProperty(COLUMN_POINT, new Point(colIndex, rowIndex));
        editor.setBounds(bounds);
        
        addKeyboardAction(editor, KeyEvent.VK_ENTER, true);
        addKeyboardAction(editor, KeyEvent.VK_ESCAPE, false);
        
        UIInput input = (UIInput) editor;
        boolean refreshed = false;
        if ( !beanLoaded ) {
            Object bean = tableModel.getListModel().getSelectedItem();
            itemBinding.setBean(bean);
            itemBinding.refresh();
            beanLoaded = true;
            refreshed = true;
        }
        
        if ( !(e instanceof MouseEvent) && isPrintableKey() ) {
            input.setValue( currentKeyEvent );
        } else if ( e instanceof MouseEvent || isEditKey() ) {
            if ( !refreshed ) input.refresh();
            highLight(editor);
        } else {
            return false;
        }
        
        editor.setInputVerifier( (InputVerifier) editor.getClientProperty(InputVerifier.class));
        editor.setVisible(true);
        editor.grabFocus();
        editingMode = true;
        currentEditor = editor;
        return false;
    }
    
    private boolean isPrintableKey() {
        KeyEvent ke = currentKeyEvent;
        int kc = ke.getKeyCode();
        
        return !ke.isActionKey() && kc != KeyEvent.VK_ESCAPE
                && kc != KeyEvent.VK_BACK_SPACE && kc != KeyEvent.VK_DELETE;
    }
    
    private boolean isEditKey() {
        int kc = currentKeyEvent.getKeyCode();
        
        return kc == KeyEvent.VK_F2 || kc == KeyEvent.VK_INSERT
                || kc == KeyEvent.VK_BACK_SPACE;
    }
    
    private void highLight(JComponent comp) {
        if ( comp instanceof JTextComponent )
            ((JTextComponent) comp).selectAll();
    }
    
    private void addKeyboardAction(JComponent comp, int key, boolean commit) {
        final Point point = (Point) comp.getClientProperty(COLUMN_POINT);
        final JComponent editorComp = comp;
        final boolean isCommit = commit;
        
        comp.registerKeyboardAction( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if ( isCommit ) {
                    focusNextCellFrom( point.y, point.x );
                } else {
                    hideEditor(editorComp, point.y, point.x, isCommit);
                }
            }
        }, KeyStroke.getKeyStroke(key, 0), JComponent.WHEN_FOCUSED);
    }
    
    private void hideEditor(boolean commit) {
        if ( !editingMode || currentEditor == null ) return;
        Point point = (Point) currentEditor.getClientProperty(COLUMN_POINT);
        hideEditor(currentEditor, point.y, point.x, commit);
    }
    
    private void hideEditor(JComponent editor, int rowIndex, int colIndex, boolean commit) {
        if ( !commit ) {
            editor.setInputVerifier(null);
        } else {
            if ( editor instanceof Validatable ) {
                ((Validatable) editor).getActionMessage().clearMessages();
            }
            ActionMessage ac = new ActionMessage();
            itemBinding.validate(ac);
            if ( ac.hasMessages() ) {
                tableModel.setErrorMessage(rowIndex, ac.toString());
            } else {
                tableModel.removeErrorMessage(rowIndex);
            }
        }
        
        editor.setVisible(false);
        editingMode = false;
        currentEditor = null;
        editor.setInputVerifier(null);
        
        tableModel.fireTableRowsUpdated(rowIndex, rowIndex);
        grabFocus();
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
            beanLoaded = false;
            listModel.setSelectedItem(rowIndex);
        }
    }
    
    private void focusNextCellFrom(int rowIndex, int colIndex) {
        int rowIndex0 = rowIndex;
        int colIndex0 = colIndex;
        if (colIndex0+1 < tableModel.getColumnCount()) {
            this.changeSelection(rowIndex0, colIndex0+1, false, false);
        } else if (rowIndex0+1 < tableModel.getRowCount()) {
            this.changeSelection(rowIndex0+1, 0, false, false);
        } else {
            this.changeSelection(0, 0, false, false);
        }
    }
    
    protected void processKeyEvent(KeyEvent e) {
        currentKeyEvent = e;
        super.processKeyEvent(e);
    }
    //</editor-fold>
    
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public void refreshList() {
        ListItem item = listModel.getSelectedItem();
        int col = getSelectedColumn();
        tableModel.fireTableDataChanged();
        if(item!=null) {
            super.setRowSelectionInterval(item.getIndex(),item.getIndex());
            if ( col >= 0 ) super.setColumnSelectionInterval(col, col);
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
    
    public void refreshSelectedItem() {
        
    }
    
    public void movePrevRecord() {
        if ( getSelectedRow() == 0 && !tableModel.hasError() ) {
            listModel.moveBackRecord();
        }
    }
    
    public void moveNextRecord() {
        if ( getSelectedRow() == getRowCount() - 1 && !tableModel.hasError() ) {
            listModel.moveNextRecord();
        }
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  KEY_LISTENER  ">
    private static final KeyListener KEY_LISTENER = new KeyAdapter() {
        
        public void keyPressed(KeyEvent e) {
            if ( !(e.getSource() instanceof YTable) ) return;
            YTable table = (YTable) e.getSource();
            
            if ( e.getKeyCode() == KeyEvent.VK_DOWN) {
                table.moveNextRecord();
            } else if ( e.getKeyCode() == KeyEvent.VK_UP ) {
                table.movePrevRecord();
            }
        }
        
    };
    //</editor-fold>
    
//
//    //<editor-fold defaultstate="collapsed" desc="  KeyActionSupport (class)  ">
//    private class KeyActionSupport implements ActionListener {
//
//        private List<ActionListener> listeners = new ArrayList();
//
//        KeyActionSupport(JComponent comp, int key) {
//            KeyStroke ks = KeyStroke.getKeyStroke(key, 0);
//            ActionListener oldListener = comp.getActionForKeyStroke(ks);
//            comp.registerKeyboardAction(this, ks, 0);
//            addListener(oldListener);
//        }
//
//        public void actionPerformed(ActionEvent e) {
//            for ( ActionListener l: listeners ) {
//                l.actionPerformed(e);
//            }
//        }
//
//        public void addListener(ActionListener listener) {
//            if ( listener != null ) listeners.add(listener);
//        }
//
//    }
//    //</editor-fold>
//
}
