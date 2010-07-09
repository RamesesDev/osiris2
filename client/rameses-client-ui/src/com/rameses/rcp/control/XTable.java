/*
 * XTable.java
 *
 * Created on July 1, 2010, 4:50 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.AbstractListModel;
import com.rameses.rcp.common.ListItem;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.SubListModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
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
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import com.rameses.rcp.control.table.TableListener;
import com.rameses.rcp.control.table.ListScrollBar;
import com.rameses.rcp.control.table.TableComponent;
import com.rameses.rcp.control.table.TableHeaderBorder;

public class XTable extends JPanel implements UIInput, TableListener, Validatable {
    
    private TableComponent table = new TableComponent(this);
    private ListScrollBar scrollBar = new ListScrollBar();
    
    private AbstractListModel listModel;
    private String[] depends;
    private Binding binding;
    private int index;
    private String handler;
    private ActionMessage actionMessage = new ActionMessage();
    private boolean dynamic;
    
    
    public XTable() {
        init();
    }
    
    //<editor-fold defaultstate="collapsed" desc="  initialize table  ">
    private void init() {
        JScrollPane jsp = new JScrollPane(table);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jsp.setBorder(BorderFactory.createEmptyBorder());
        
        super.setLayout(new BorderLayout());
        add(jsp, BorderLayout.CENTER);
        add(new ScrollBarPanel(scrollBar), BorderLayout.EAST);
        setBorder(new TableBorder());
        
        if ( Beans.isDesignTime() ) {
            setPreferredSize(new Dimension(200,80));
            table.setModel(new javax.swing.table.DefaultTableModel(
                    new Object [][] { {null, null} },
                    new String [] { "Title 1", "Title 2" }
            ));
        }
    }
    //</editor-fold>
    
    
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
    
    //<editor-fold defaultstate="collapsed" desc="  UIInput impl  ">
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
            }
            
        }
    }
    
    public Object getValue() {
        return listModel.getSelectedItem();
    }
    
    public void setValue(Object value) {
    }
    
    public boolean isNullWhenEmpty() {
        return true;
    }
    
    public int compareTo(Object o) {
        if ( o == null || !(o instanceof UIControl) ) return 0;
        return this.index = ((UIControl) o).getIndex();
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  grid handler impl  ">
    public void refreshList() {
        scrollBar.adjustValues();
        rowChanged();
    }
    
    public void openItem() {
        ListItem selectedItem = listModel.getSelectedItem();
        if( selectedItem!=null && selectedItem.getItem()!=null) {
            try {
                System.out.println("selected item is " + selectedItem.getItem());
            } catch(Exception ex){
                MsgBox.err(ex);
            }
        }
    }
    
    public void rowChanged() {
        if ( !ValueUtil.isEmpty(getName()) ) {
            UIInputUtil.updateBeanValue(this);
        }
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
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  TableBorder (class)  ">
    private static class TableBorder extends AbstractBorder implements UIResource {
        
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
            
            JLabel label = new JLabel(" ");
            
            Border bb = new TableHeaderBorder();
            Border eb = BorderFactory.createEmptyBorder(2,5,2,1);
            label.setBorder( BorderFactory.createCompoundBorder(bb, eb) );
            
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
            add(label, BorderLayout.NORTH);
            add(scrollBar, BorderLayout.CENTER);
        }
        
    }
    //</editor-fold>
    
}
