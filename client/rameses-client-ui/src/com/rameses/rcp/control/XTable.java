/*
 * XTable.java
 *
 * Created on July 1, 2010, 4:50 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.common.PropertyResolver;
import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.control.table.TableUtil;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import com.rameses.rcp.control.table.TableListener;
import com.rameses.rcp.control.table.ListScrollBar;
import com.rameses.rcp.control.table.RowHeaderView;
import com.rameses.rcp.control.table.TableBorder;
import com.rameses.rcp.control.table.TableComponent;
import com.rameses.rcp.framework.ClientContext;
import java.awt.Color;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BorderFactory;
import javax.swing.UIManager;


@Deprecated
public class XTable extends JPanel implements UIInput, TableListener, Validatable, FocusListener {
    
    private TableComponent table;
    private ListScrollBar scrollBar;
    private RowHeaderView rowHeaderView;
    private JScrollPane scrollPane;
    
    private AbstractListModel listModel;
    private String[] depends;
    private Binding binding;
    private int index;
    private String handler;
    private ActionMessage actionMessage = new ActionMessage();
    private boolean dynamic;
    private boolean showRowHeader;
    
    
    public XTable() {
        init();
    }
    
    //-- channel events to TableComponent
    public void addMouseListener(MouseListener l) {
        table.addMouseListener(l);
    }
    
    public void removeMouseListener(MouseListener l) {
        table.removeMouseListener(l);
    }
    
    public void addKeyListener(KeyListener l) {
        table.addKeyListener(l);
    }
    
    public void removeKeyListener(KeyListener l) {
        table.removeKeyListener(l);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initialize table  ">
    private void init() {
        table = new TableComponent();
        scrollBar = new ListScrollBar();
        
        //--create and decorate scrollpane for the JTable
        scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation();
                if ( rotation == 0 ) return;
                
                if ( rotation < 0 )
                    listModel.moveBackRecord();
                else
                    listModel.moveNextRecord();
            }
        });
        
        super.setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(new ScrollBarPanel(scrollBar), BorderLayout.EAST);
        setBorder(new TableBorder());
        
        //--default table properties
        setGridColor(new Color(217,216,216));
        setShowRowHeader(false);
        setShowHorizontalLines(false);
        setRowMargin(0);
        setShowVerticalLines(true);
        setAutoResize(true);
        
        if ( table.getEvenBackground() == null ) {
            Color bg = (Color) UIManager.get("Table.evenBackground");
            if ( bg == null ) bg = table.getBackground();
            table.setEvenBackground(bg);
        }
        
        if ( table.getEvenForeground() == null ) {
            Color fg = (Color) UIManager.get("Table.evenForeground");
            if ( fg != null ) table.setEvenForeground(fg);
        }
        
        if ( table.getOddBackground() == null ) {
            Color bg = (Color) UIManager.get("Table.oddBackground");
            if ( bg == null ) bg = new Color(225, 232, 246);
            table.setOddBackground(bg);
        }
        
        if ( table.getOddForeground() == null ) {
            Color fg = (Color) UIManager.get("Table.oddForeground");
            if ( fg != null ) table.setOddForeground(fg);
        }
        
        //--design time display
        if ( Beans.isDesignTime() ) {
            if ( rowHeaderView != null )
                rowHeaderView.setRowCount(1);
            
            setPreferredSize(new Dimension(200,80));
            table.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] { {null, null} },
                    new String [] { "Title 1", "Title 2" }
            ));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  UIInput properties  ">
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
            if( dynamic )
                listModel.load();
            else
                listModel.refresh();
        }
    }
    
    public void load() {
        if ( handler != null ) {
            Object obj = UIControlUtil.getBeanValue(this, handler);
            if ( obj instanceof AbstractListModel ) {
                listModel = (AbstractListModel) obj;
                table.setListModel(listModel);
                table.setListener(this);
                table.setBinding(binding);
                scrollBar.setListModel(listModel);
                
                if ( rowHeaderView != null )
                    rowHeaderView.setRowCount( listModel.getRows() );
            }
            
        }
    }
    
    public Object getValue() {
        if ( Beans.isDesignTime() ) return null;
        
        return listModel.getSelectedItem();
    }
    
    public void setValue(Object value) {}
    
    public boolean isNullWhenEmpty() {
        return true;
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  table listener methods  ">
    public void refreshList() {
        scrollBar.adjustValues();
        rowChanged();
    }
    
    public void openItem() {
        ListItem selectedItem = listModel.getSelectedItem();
        if( selectedItem!=null && selectedItem.getItem()!=null) {
            try {
                Object outcome = listModel.openSelectedItem();
                if ( outcome == null ) return;
                
                ControlSupport.fireNavigation(this, outcome);
                
            } catch(Exception ex){
                MsgBox.err(new IllegalStateException("XTable::openItem", ex));
            }
        }
    }
    
    public void rowChanged() {
        String name = getName();
        if ( !ValueUtil.isEmpty(name) ) {
            PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
            ListItem oldValue = (ListItem) resolver.getProperty(binding.getBean(), name);
            ListItem newValue = listModel.getSelectedItem();
            if( !ValueUtil.isEqual(oldValue, newValue) ) {
                resolver.setProperty(binding.getBean(), name, newValue.clone());
                binding.notifyDepends(this);
            }
        }
        
        if ( rowHeaderView != null )
            rowHeaderView.clearEditing();
    }
    
    public void editCellAt(int rowIndex, int colIndex) {
        if ( rowHeaderView != null )
            rowHeaderView.editRow(rowIndex);
    }
    
    public void cancelRowEdit() {
        if ( rowHeaderView != null )
            rowHeaderView.clearEditing();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  validatable properties  ">
    public String getCaption() {
        return getName();
    }
    
    public void setCaption(String caption) {
    }
    
    public boolean isRequired() {
        return table.isRequired();
    }
    
    public void setRequired(boolean required) {
    }
    
    public void validateInput() {
        String errmsg = listModel.getErrorMessages();
        actionMessage.clearMessages();
        if ( errmsg != null ) {
            actionMessage.addMessage(null, errmsg, null);
        }
    }
    
    public ActionMessage getActionMessage() {
        return actionMessage;
    }
    
    public void setReadonly(boolean readonly) {
        table.setReadonly(readonly);
    }
    
    public boolean isReadonly() {
        return table.isReadonly();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public void setName(String name) {
        super.setName(name);
        if ( table != null ) table.setName(name);
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
    public String getHandler() { return handler; }
    public void setHandler(String handler) { this.handler = handler; }
    
    public boolean isDynamic() { return dynamic; }
    public void setDynamic(boolean dynamic) { this.dynamic = dynamic; }
    
    public void setShowHorizontalLines(boolean show) { table.setShowHorizontalLines(show); }
    public boolean isShowHorizontalLines() { return table.getShowHorizontalLines(); }
    
    public void setShowVerticalLines(boolean show) { table.setShowVerticalLines(show); }
    public boolean isShowVerticalLines() { return table.getShowVerticalLines(); }
    
    public void setAutoResize(boolean autoResize) { table.setAutoResize(autoResize); }
    public boolean isAutoResize() { return table.isAutoResize(); }
    
    public void setRequestFocus(boolean focus) {
        if ( focus ) table.requestFocus();
    }
    
    public void requestFocus() { table.requestFocus(); }
    
    public void focusGained(FocusEvent e) { table.grabFocus(); }
    public void focusLost(FocusEvent e) {}
    
    public Color getEvenBackground() { return table.getEvenBackground(); }
    public void setEvenBackground(Color evenBackground) { table.setEvenBackground( evenBackground ); }
    
    public Color getOddBackground() { return table.getOddBackground(); }
    public void setOddBackground(Color oddBackground) { table.setOddBackground( oddBackground ); }
    
    public Color getErrorBackground() { return table.getErrorBackground(); }
    public void setErrorBackground(Color errorBackground) { table.setErrorBackground( errorBackground ); }
    
    public Color getEvenForeground() { return table.getEvenForeground(); }
    public void setEvenForeground(Color evenForeground) { table.setEvenForeground( evenForeground ); }
    
    public Color getOddForeground() { return table.getOddForeground(); }
    public void setOddForeground(Color oddForeground) { table.setOddForeground( oddForeground ); }
    
    public Color getErrorForeground() { return table.getErrorForeground(); }
    public void setErrorForeground(Color errorForeground) { table.setErrorForeground( errorForeground ); }
    
    public boolean isImmediate() { return true; }
    
    public boolean isShowRowHeader() { return showRowHeader; }
    public void setShowRowHeader(boolean showRowHeader) {
        this.showRowHeader = showRowHeader;
        
        if ( showRowHeader ) {
            Color gridColor = table.getGridColor();
            scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, TableUtil.getTableCornerComponent(gridColor));
            scrollPane.setRowHeaderView( (rowHeaderView = new RowHeaderView(table)) );
            rowHeaderView.setRowCount( table.getRowCount() );
        } else {
            scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, null);
            scrollPane.setRowHeaderView( (rowHeaderView = null) );
        }
    }
    
    public int getColumnMargin() { return table.getColumnModel().getColumnMargin(); }
    public void setColumnMargin(int margin) { table.getColumnModel().setColumnMargin(margin); }
    
    public int getRowMargin() { return table.getRowMargin(); }
    public void setRowMargin(int margin) { table.setRowMargin(margin); }
    
    public Color getGridColor() { return table.getGridColor(); }
    public void setGridColor(Color color) { table.setGridColor(color); }
    
    public boolean isEnabled()        { return table.isEnabled(); }
    public void setEnabled(boolean e) { table.setEnabled(e); }
    
    public int getRowHeight()       { return table.getRowHeight(); }
    public void setRowHeight(int h) { table.setRowHeight(h); }
    
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="  ScrollBarPanel (class)  ">
    private class ScrollBarPanel extends JPanel {
        
        ScrollBarPanel(JScrollBar scrollBar) {
            Dimension ps = scrollBar.getPreferredSize();
            setPreferredSize(ps);
            setLayout(new BorderLayout());
            
            scrollBar.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    String propName = evt.getPropertyName();
                    if ( "visible".equals(propName) ) {
                        Boolean visible = (Boolean) evt.getNewValue();;
                        setVisible(visible.booleanValue());
                    }
                }
            });
            
            setVisible( scrollBar.isVisible() );
            add(TableUtil.getTableCornerComponent(table.getGridColor()), BorderLayout.NORTH);
            add(scrollBar, BorderLayout.CENTER);
        }
        
    }
    //</editor-fold>
    
    
}
