package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Windhel
 */

public class XIconPanel extends JPanel implements UIControl {
    
    private static String ALIGN_LEFT = "LEFT";
    private static String ALIGN_RIGHT = "RIGHT";
    private static String ALIGN_CENTER = "CENTER";
    private static String ALIGN_SPECIAL_01 = "SPECIAL01";
    private static String CAPTION_RIGHT = "RIGHT";
    private static String CAPTION_BOTTOM = "BOTTOM";
    
    private String[] depends;
    private int index;
    private Binding binding;
    private FlowLayout flowLayout = new FlowLayout();
    private GridBagConstraints gridBagCons = new GridBagConstraints();
    private String orientation = ALIGN_LEFT;
    private String iconCaptionOrientation = CAPTION_RIGHT;
    
    public XIconPanel() {
        setOpaque(false);
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
    
    public void refresh() {
    }
    
    public void load() {
        buildIcons();
    }
    
    private void buildIcons() {
        if ( ValueUtil.isEmpty(getName()) ) return;
        List<Action> actions = (List) UIControlUtil.getBeanValue(this);
        
        if(ALIGN_RIGHT.equals(orientation.toUpperCase())
        || ALIGN_CENTER.equals(orientation.toUpperCase())
        || ALIGN_LEFT.equals(orientation.toUpperCase())) {
            if(ALIGN_RIGHT.equals(orientation.toUpperCase()))
                flowLayout.setAlignment(FlowLayout.RIGHT);
            else if  (ALIGN_CENTER.equals(orientation.toUpperCase()))
                flowLayout.setAlignment(FlowLayout.CENTER);
            else if (ALIGN_LEFT.equals(orientation.toUpperCase()))
                flowLayout.setAlignment(FlowLayout.LEFT);
            setLayout(flowLayout);            
            for(Action a : actions) {
                IconButton ib = new IconButton(a.getCaption(), a.getIcon());
                ib.setBinding(binding);
                ib.setName(a.getName());
                ib.setPermission(a.getPermission());
                ib.setParams(a.getParameters());
                ib.setCaptionOrientation(iconCaptionOrientation);
                add(ib);
                ib.load();
            }
        } else if(ALIGN_SPECIAL_01.equals(orientation.toUpperCase())){
            //is used for GridBagLayout... row:2 col:5 arrangement
            setLayout(new GridBagLayout());
            for(int y = 0; y < actions.size() - 1; y++) {
                for(int x = 0; x < 2 ; x++) {
                    IconButton ib = new IconButton(actions.get(x + y).getCaption(), actions.get(x + y).getIcon());
                    ib.setBinding(binding);
                    ib.setName(actions.get(x + y).getName());
                    ib.setPermission(actions.get(x + y).getPermission());
                    ib.setParams(actions.get(x + y).getParameters());
                    ib.setCaptionOrientation(iconCaptionOrientation);
                    ib.setFont(getFont());
                    gridBagCons.gridx = x;
                    gridBagCons.gridy = y;
                    gridBagCons.insets = new Insets(5,5,5,5);
                    add(ib, gridBagCons);
                    ib.load();
                }
                y++;
            }
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    public String getIconCaptionOrientation() {
        return iconCaptionOrientation;
    }
    
    public void setIconCaptionOrientation(String iconCaptionOrientation) {
        this.iconCaptionOrientation = iconCaptionOrientation;
    }
}
