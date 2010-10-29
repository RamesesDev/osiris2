/*
 * XComboEnum.java
 *
 * Created on September 7, 2009, 2:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import com.rameses.rcp.framework.AbstractUIInput;

import com.rameses.rcp.framework.ControlSupport;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultListModel;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class XList extends AbstractUIInput implements ListSelectionListener, ListCellRenderer {
    
    private JList listComponent;
    private String expression;
    
    private String items;
    private boolean listDynamic;
    private DefaultListModel model;
    private boolean multiselect = false;
    private boolean updating;
    
    public XList() {
        super();
        super.setShowCaption(false);
    }
    
    
    
    public JComponent getComponent() {
        if(listComponent==null) {
            listComponent = new JList();
            model = new DefaultListModel();
            listComponent.setCellRenderer(this);            
            listComponent.setModel(model);
            super.setLayout(new BorderLayout());
            JScrollPane scrollPane = new JScrollPane();
            scrollPane.setViewportView(listComponent);
            super.add(scrollPane, BorderLayout.CENTER);
        }
        return listComponent;
    }
    
    private void buildList() {
        updating = true;
        if( isEmpty(getName()))
            throw new IllegalStateException("Name in XMultiList must be provided. It must correspond to an array of values");
        
        if( isEmpty(items))
            throw new IllegalStateException("Items is not specified");
        List list = (List)getBeanValue(items);
        model.clear();
        int i = 0;
        for(Object o: list) {
            model.add(i++, o);
        }
        updating =false;
    }
    
    public void load() {
        super.load();
        if(multiselect) {
            listComponent.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }
        else {
            listComponent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        //attach the appropriate event handler
        if(!immediate) {
            listComponent.setInputVerifier(new ListVerifier());
        } else {
            listComponent.addListSelectionListener(this);
        }
        if(!listDynamic) buildList();
    }
    
    public void refresh() {
        if( listDynamic ) {
            buildList();
        }
        super.refresh(getName());
    }
    
    public String getItems() {
        return items;
    }
    
    public void setItems(String items) {
        this.items = items;
    }
    
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public boolean isListDynamic() {
        return listDynamic;
    }
    
    public void setListDynamic(boolean listDynamic) {
        this.listDynamic = listDynamic;
    }
    
    public void setStyle(Map props) {
        ControlSupport.setStyles(props, listComponent);
    }
    
    public void setValue(Object value) {
        updating = true;
        if(value!=null) {
            if(multiselect) {
                if(value.getClass().isArray()) {
                    for(Object o: (Object[])value ) {
                        listComponent.setSelectedValue(o, false);
                    }
                }
            } else {
                listComponent.setSelectedValue(value, false);
            }
        }
        updating = false;
    }
    
    public Object getValue() {
        if(multiselect) {
            return listComponent.getSelectedValues();
        } else {
            return listComponent.getSelectedValue();
        }
    }
    
    public class ListVerifier extends InputVerifier {
        
        public boolean verify(JComponent jComponent) {
            return verifyInput();
        }
        
    }
    
    public boolean isMultiselect() {
        return multiselect;
    }
    
    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }
    
    public void valueChanged(ListSelectionEvent itemEvent) {
        if( !updating && !itemEvent.getValueIsAdjusting()) {
            if (listComponent.getSelectedIndex() != -1) {
                System.out.println("fire selection " + listComponent.getSelectedValue());
                super.fireValueChanged();
            }
        }
    }
    
    public int getLayoutOrientation() {
        return listComponent.getLayoutOrientation();
    }
    
    public void setLayoutOrientation(int layoutOrientation) {
       listComponent.setLayoutOrientation(layoutOrientation);
    }

    public int getVisibleRowCount() {
        return listComponent.getVisibleRowCount();
    }
    
    public void setVisibleRowCount(int i) {
        listComponent.setVisibleRowCount(i);
    }
    
    public void setFixedCellWidth(int w) {
        listComponent.setFixedCellWidth(w);
    }
    
    public int getFixedCellWidth() {
        return listComponent.getFixedCellWidth();
    }
    
    public void setFixedCellHeight(int h) {
        listComponent.setFixedCellHeight(h);
    }
    
    public int getFixedCellHeight() {
        return listComponent.getFixedCellHeight();
    }

    private JLabel cellLabel;
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if( cellLabel == null ) {
            cellLabel = new JLabel();
            cellLabel.setOpaque(true);            
            cellLabel.setSize( list.getFixedCellWidth(), list.getFixedCellHeight() );
        }
        Object val = value;
        if(expression!=null) val = getExpressionValue(value,expression);
        if(cellHasFocus) {
            cellLabel.setBackground(list.getSelectionBackground());
            cellLabel.setForeground(list.getSelectionForeground());
        }
        else {
            cellLabel.setBackground(null);
            cellLabel.setForeground(null);
        }
        cellLabel.setText( val.toString() );
        return cellLabel;
    }
    
    
}
