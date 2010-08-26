package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Windhel
 */

public class XIconPanel extends JPanel implements UIControl {
    
    private static String ALIGN_RIGHT = "RIGHT";
    private static String ALIGN_CENTER = "CENTER";
    
    private String[] depends;
    private int index;
    private Binding binding;
    private FlowLayout flowLayout = new FlowLayout();
    private String orientation = "LEFT";
    
    public XIconPanel() {
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getter / Setter ">
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
    
    public String getOrientation() {
        return orientation;
    }
    
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    //</editor-fold>
    
    public void refresh() {}
    
    public void load() {
        buildIcons();
    }
    
    private void buildIcons() {
        if ( ValueUtil.isEmpty(getName()) ) return;
        
        ActionProvider ap = ClientContext.getCurrentContext().getActionProvider();
        List<Action> actions = ap.getActionsByType(getName(), null);
        
        if(ALIGN_RIGHT.equals(orientation.toUpperCase()))
            flowLayout.setAlignment(FlowLayout.RIGHT);
        else if(ALIGN_CENTER.equals(orientation.toUpperCase()))
            flowLayout.setAlignment(FlowLayout.CENTER);
        else
            flowLayout.setAlignment(FlowLayout.LEFT);
        setLayout(flowLayout);
        
        for(Action a : actions) {
            IconButton ib = new IconButton(a.getCaption(), a.getIcon());
            ib.setBinding(binding);
            ib.setName(a.getName());
            ib.setPermission(a.getPermission());
            ib.setCaptionClr(getForeground());
            ib.setPreferredSize(new Dimension(40,40));
            add(ib);
            ib.load();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
}
