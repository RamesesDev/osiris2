/*
 * XTable.java
 *
 * Created on July 1, 2010, 4:50 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.SubListModel;
import com.rameses.rcp.control.table.TableManager;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.ui.UIInput;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.rcp.util.UIInputUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.metal.MetalLookAndFeel;
import com.rameses.rcp.control.table.TableListener;
import com.rameses.rcp.control.table.ListScrollBar;
import com.rameses.rcp.control.table.TableComponent;
import com.rameses.rcp.control.table.TableHeaderBorder;
import com.rameses.rcp.framework.ClientContext;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class XTable extends JPanel implements UIInput, TableListener, Validatable, FocusListener {
    
    private TableComponent table = new TableComponent();
    private ListScrollBar scrollBar = new ListScrollBar();
    private RowHeaderView rowHeaderView = new RowHeaderView();
    
    private AbstractListModel listModel;
    private String[] depends;
    private Binding binding;
    private int index;
    private String handler;
    private ActionMessage actionMessage = new ActionMessage();
    private boolean dynamic;
    private String onAfterUpdate;
    
    
    public XTable() {
        init();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initialize table  ">
    private void init() {
        JScrollPane jsp = new JScrollPane(table);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setCorner(JScrollPane.UPPER_LEFT_CORNER, TableManager.getTableCornerComponent());
        jsp.setRowHeaderView(rowHeaderView);
        jsp.setBorder(BorderFactory.createEmptyBorder());
        
        jsp.addMouseWheelListener(new MouseWheelListener() {
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
        add(jsp, BorderLayout.CENTER);
        add(new ScrollBarPanel(scrollBar), BorderLayout.EAST);
        setBorder(new TableBorder());
        
        if ( Beans.isDesignTime() ) {
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
            if ( dynamic )
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
                scrollBar.setListModel(listModel);
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
                NavigationHandler nh = ClientContext.getCurrentContext().getNavigationHandler();
                NavigatablePanel navPanel = UIControlUtil.getParentPanel(this, null);
                nh.navigate(navPanel, this, outcome);
                
            } catch(Exception ex){
                throw new IllegalStateException("XTable::openItem", ex);
            }
        }
    }
    
    public void rowChanged() {
        if ( !ValueUtil.isEmpty(getName()) ) {
            UIInputUtil.updateBeanValue(this, false); //we don't need to add entry to change log
        }
        rowHeaderView.clearEditing();
    }
    
    public void editCellAt(int rowIndex, int colIndex) {
        rowHeaderView.editRow(rowIndex);
    }
    
    public void cancelRowEdit() {
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
        if ( listModel instanceof SubListModel ) {
            SubListModel slm = (SubListModel) listModel;
            String errmsg = slm.getErrorMessages();
            actionMessage.clearMessages();
            if ( errmsg != null ) {
                actionMessage.addMessage(null, errmsg, null);
            }
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
    public void setLayout(LayoutManager mgr) {;}
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    public void setShowHorizontalLines(boolean show) {
        table.setShowHorizontalLines(show);
    }
    
    public boolean isShowHorizontalLines() {
        return table.getShowHorizontalLines();
    }
    
    public void setShowVerticalLines(boolean show) {
        table.setShowVerticalLines(show);
    }
    
    public boolean isShowVerticalLines() {
        return table.getShowVerticalLines();
    }
    
    public void setAutoResize(boolean autoResize) {
        table.setAutoResize(autoResize);
    }
    
    public boolean isAutoResize() {
        return table.isAutoResize();
    }
    
    public String getOnAfterUpdate() {
        return onAfterUpdate;
    }
    
    public void setOnAfterUpdate(String onAfterUpdate) {
        this.onAfterUpdate = onAfterUpdate;
    }
    
    public void setRequestFocus(boolean focus) {
        if ( focus ) table.requestFocus();
    }
    
    public void requestFocus() {
        table.grabFocus();
    }
    
    public void focusGained(FocusEvent e) {
        table.grabFocus();
    }
    
    public void focusLost(FocusEvent e) {}
    
    public Color getEvenBackground() {
        return table.getEvenBackground();
    }
    
    public void setEvenBackground(Color evenBackground) {
        table.setEvenBackground( evenBackground );
    }
    
    public Color getOddBackground() {
        return table.getOddBackground();
    }
    
    public void setOddBackground(Color oddBackground) {
        table.setOddBackground( oddBackground );
    }
    
    public Color getErrorBackground() {
        return table.getErrorBackground();
    }
    
    public void setErrorBackground(Color errorBackground) {
        table.setErrorBackground( errorBackground );
    }
    
    public Color getEvenForeground() {
        return table.getEvenForeground();
    }
    
    public void setEvenForeground(Color evenForeground) {
        table.setEvenForeground( evenForeground );
    }
    
    public Color getOddForeground() {
        return table.getOddForeground();
    }
    
    public void setOddForeground(Color oddForeground) {
        table.setOddForeground( oddForeground );
    }
    
    public Color getErrorForeground() {
        return table.getErrorForeground();
    }
    
    public void setErrorForeground(Color errorForeground) {
        table.setErrorForeground( errorForeground );
    }
    
    public boolean isImmediate() {
        return true;
    }
    //</editor-fold>
    
    
    
    //<editor-fold defaultstate="collapsed" desc="  TableBorder (class)  ">
    public static class TableBorder extends AbstractBorder {
        
        private static final Insets insets = new Insets(1, 1, 2, 2);
        
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            
            g.translate( x, y);
            
            g.setColor( MetalLookAndFeel.getControlDarkShadow() );
            g.drawRect( 0, 0, w-2, h-2 );
            g.setColor( MetalLookAndFeel.getControlHighlight() );
            
            g.drawLine( w-1, 1, w-1, h-1);
            g.drawLine( 1, h-1, w-1, h-1);
            
            g.setColor( MetalLookAndFeel.getControl() );
            g.drawLine( w-2, 2, w-2, 2 );
            g.drawLine( 1, h-2, 1, h-2 );
            
            g.translate( -x, -y);
            
        }
        
        public Insets getBorderInsets(Component c)       {
            return insets;
        }
    }
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
            add(TableManager.getTableCornerComponent(), BorderLayout.NORTH);
            add(scrollBar, BorderLayout.CENTER);
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  RowHeaderView (class)  ">
    private class RowHeaderView extends JPanel {
        
        private int rowCount;
        private int currentRow = -1;
        
        RowHeaderView() {
            setLayout(new RowHeaderLayout());
        }
        
        public void setRowCount(int rowCount) {
            if ( this.rowCount == rowCount ) return;
            this.rowCount = rowCount;
            
            removeAll();
            JComponent label = null;
            for (int i = 0; i < rowCount; ++i) {
                add(new RowHeader());
            }
            SwingUtilities.updateComponentTreeUI(this);
        }
        
        public void clearEditing() {
            if ( currentRow != -1 ) {
                RowHeader rh = (RowHeader) getComponent(currentRow);
                rh.edit(false);
            }
            currentRow = -1;
        }
        
        public void editRow(int row) {
            if ( currentRow != row ) {
                clearEditing();
            }
            RowHeader rh = (RowHeader) getComponent(row);
            rh.edit(true);
            currentRow = row;
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  RowHeaderLayout (Class) ">
    private class RowHeaderLayout implements LayoutManager {
        
        public void addLayoutComponent(String name, Component comp) {;}
        public void removeLayoutComponent(Component comp) {;}
        
        public Dimension preferredLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }
        
        public Dimension minimumLayoutSize(Container parent) {
            return getLayoutSize(parent);
        }
        
        public void layoutContainer(Container parent) {
            synchronized (parent.getTreeLock()) {
                Insets margin = parent.getInsets();
                int x = margin.left;
                int y = margin.top;
                int w = parent.getWidth() - (margin.left + margin.right);
                int h = parent.getHeight() - (margin.top + margin.bottom);
                
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++) {
                    if (!(comps[i] instanceof RowHeader)) continue;
                    
                    int rh = XTable.this.table.getRowHeight(i);
                    comps[i].setBounds(x, y, w, rh);
                    y += rh;
                }
            }
        }
        
        private Dimension getLayoutSize(Container parent) {
            synchronized (parent.getTreeLock()) {
                int w = 0;
                int h = 0;
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++) {
                    if (!(comps[i] instanceof RowHeader)) continue;
                    
                    Dimension dim = comps[i].getPreferredSize();
                    w = Math.max(w, dim.width);
                    h += XTable.this.table.getRowHeight(i);
                }
                
                Insets margin = parent.getInsets();
                w += (margin.left + margin.right);
                h += (margin.top + margin.bottom);
                return new Dimension(w,h);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  RowHeader (class)  ">
    private class RowHeader extends JLabel {
        
        public RowHeader() {
            setOpaque(true);
            setBorder(new TableHeaderBorder(new Insets(2,0,0,0)));
            setPreferredSize(new Dimension(23,23));
            setHorizontalAlignment(SwingConstants.CENTER);
            setForeground(Color.BLUE);
            setFont(new Font("Courier", Font.PLAIN, 11));
            //edit(true);
        }
        
        public void setText(String text) {;}
        
        public void edit(boolean b) {
            if (b)
                super.setText("<html><b>*</b></html>");
            else
                super.setText("");
        }
    }
    //</editor-fold>
}
