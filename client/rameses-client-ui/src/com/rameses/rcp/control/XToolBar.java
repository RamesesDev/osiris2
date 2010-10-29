package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author Windhel
 */

public class XToolBar extends JPanel implements UIControl{
    
    private static String ALIGN_RIGHT = "RIGHT";
    private static String ALIGN_CENTER = "CENTER";
    private static int HGAP = 5;
    private static Color UPCLR = new Color(245, 245, 245);
    private static Color LOWCLR = new Color(193, 205, 193);
    
    private Collection<Action> actions;
    private String[] depends;
    private int index;
    private Binding binding;
    private FlowLayout flowLayout = new FlowLayout();
    private String orientation = "LEFT";
    
    public XToolBar() {
        setBorder(new XToolBarBorder());
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
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public String getOrientation() {
        return orientation;
    }
    
    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
    
    //</editor-fold>
    
    public void refresh() {
    }
    
    public void load() {
        setBorder(new EtchedBorder());
        setOpaque(false);
        Object value = UIControlUtil.getBeanValue(this);
        if(value == null) return;
        if(value instanceof Collection)
            actions = (Collection) value;
        if(ALIGN_RIGHT.equals(orientation.toUpperCase()))
            flowLayout.setAlignment(FlowLayout.RIGHT);
        else if(ALIGN_CENTER.equals(orientation.toUpperCase()))
            flowLayout.setAlignment(FlowLayout.CENTER);
        else
            flowLayout.setAlignment(FlowLayout.LEFT);
        flowLayout.setHgap(HGAP);
        setLayout(flowLayout);
        for(Action a : actions) {
            IconButton ib = new IconButton("", a.getIcon());
            ib.setBinding(binding);
            ib.setName(a.getName());
            ib.setPermission(a.getPermission());
            ib.setCaptionClr(getForeground());
            ib.setPreferredSize(new Dimension(40,40));
            if(a.getTooltip() != null)
                ib.setToolTipText(a.getCaption());
            add(ib);
            ib.load();
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    private class XToolBarBorder extends AbstractBorder {
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            
            GradientPaint gradient = new GradientPaint(0, 0, UPCLR, 0, height, LOWCLR);
            g2.setPaint(gradient);
            g2.fillRect(0,0, width-1, height-1);
            
            g2.dispose();
        }
    }
    
}
