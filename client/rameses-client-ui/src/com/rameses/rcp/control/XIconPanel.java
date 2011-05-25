package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author Windhel
 */

public class XIconPanel extends JPanel implements UIControl {
    
    List list = new ArrayList();
    Map controller1 = new HashMap();
    Map controller2 = new HashMap();
    Map controller3 = new HashMap();
    Map controller4 = new HashMap();
    Map controller5 = new HashMap();
    List categories = new ArrayList();
    
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean buttonBorderPainted;
    private boolean buttonContentFilled;
    private String captionOrientation = "BOTTOM";
    
    public XIconPanel() {
        //setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        buttonBorderPainted = true;
        buttonContentFilled = true;
        
        init();
    }
    
    public void refresh() {}
    
    public void load() {
        List<Action> actions = new ArrayList();
        categories.clear();
        
        Object beanValue = UIControlUtil.getBeanValue(this);
        if(beanValue instanceof Collection) {
            for(Object o : (Collection) beanValue) {
                if(o instanceof Action) actions.add((Action)o);
            }
        }
        
        for(Action a : actions) {
            Map map = a.getProperties();
            if(categories.size() == 0) categories.add(map.get("category"));
            if(!categories.contains(map.get("category"))) categories.add( map.get("category"));
        }
        
        IconCanvas innerpanel = new IconCanvas();
        innerpanel.setOpaque(isOpaque());
        innerpanel.setLayout(new GridLayout(categories.size(),1, 0, 5));
        add(innerpanel, BorderLayout.NORTH);
        
        for(Object category : categories) {
            CategoryPanel innerMostPanel = new CategoryPanel(category.toString());
            innerMostPanel.setOpaque(isOpaque());
            int height = 50;
            int width = 50;
            for(Action action : actions) {
                Map map = action.getProperties();
                if(map.get("category").equals(category)) {
                    IconButton ibtn = new IconButton();
                    ibtn.setHorizontalTextPosition(SwingConstants.TRAILING);
                    ibtn.setVerticalAlignment(SwingConstants.CENTER);
                    ImageIcon img = new ImageIcon(getClass().getResource("/com/rameses/rcp/icons/calendar.png"));
                    
                    if(getCaptionOrientation().equals("LEFT")) {
                        ibtn.setHorizontalTextPosition(SwingConstants.LEFT);
                        height = img.getIconHeight() + 25;
                        width = img.getIconWidth() + getFontMetrics(getFont()).stringWidth(action.getCaption()) + (int)(getFontMetrics(getFont()).stringWidth(action.getCaption())/2) + 5;
                    }
                    if(getCaptionOrientation().equals("RIGHT")) {
                        ibtn.setHorizontalTextPosition(SwingConstants.RIGHT);
                        height = img.getIconHeight() + 25;
                        width = img.getIconWidth() + getFontMetrics(getFont()).stringWidth(action.getCaption()) + (int)(getFontMetrics(getFont()).stringWidth(action.getCaption())/2) + 5;
                    }
                    if(getCaptionOrientation().equals("BOTTOM")) {
                        ibtn.setHorizontalTextPosition(SwingConstants.CENTER);
                        ibtn.setVerticalTextPosition(SwingConstants.BOTTOM);
                        height = img.getIconHeight() + getFontMetrics(getFont()).getHeight() + 25;
                        width = getFontMetrics(getFont()).stringWidth(action.getCaption()) + (int)(getFontMetrics(getFont()).stringWidth(action.getCaption())/2) + 5;
                    }
                    if(getCaptionOrientation().equals("TOP")) {
                        ibtn.setHorizontalTextPosition(SwingConstants.CENTER);
                        ibtn.setVerticalTextPosition(SwingConstants.TOP);
                        height = img.getIconHeight() + getFontMetrics(getFont()).getHeight() + 25;
                        width = getFontMetrics(getFont()).stringWidth(action.getCaption()) + (int)(getFontMetrics(getFont()).stringWidth(action.getCaption())/2) + 5;
                        
                    }
                    ibtn.setPreferredSize(new Dimension(width,height));
                    ibtn.setContentAreaFilled(false);
                    ibtn.setBorderPainted(isButtonBorderPainted());
                    ibtn.setText(action.getCaption());
                    ibtn.setCaption(action.getCaption());
                    ibtn.setIcon(img);
                    ibtn.setBinding(binding);
                    ibtn.setName(action.getName());
                    ibtn.setPermission(action.getPermission());
                    ibtn.setParams(action.getParameters());
                    ibtn.setFocusPainted(false);
                    
                    if ( !action.getClass().getName().equals(Action.class.getName()) ) {
                        ibtn.putClientProperty(Action.class.getName(), action);
                    }
                    
                    //ibtn.setToolTipText(action.getTooltip());
                    innerMostPanel.add(ibtn);
                    ibtn.load();
                }
            }
            innerpanel.add(innerMostPanel);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    //<editor-fold defaultstate="collapsed" desc="init() #initialize component for Design Time">
    public void init() {
        controller1.put("category", "group 1");
        controller1.put("invokerType", "home_menu");
        controller1.put("caption", "group 1 item 1");
        
        controller5.put("category", "group 1");
        controller5.put("invokerType", "home_menu");
        controller5.put("caption", "group 1 item 2");
        
        controller2.put("category", "group 2");
        controller2.put("invokerType", "home_menu");
        controller2.put("caption", "group 2 item 1");
        
        controller3.put("category", "group 3");
        controller3.put("invokerType", "home_menu");
        controller3.put("caption", "group 3 item 1");
        
        controller4.put("category", "group 3");
        controller4.put("invokerType", "home_menu");
        controller4.put("caption", "group 3 item 2");
        
        list.add(controller1);
        list.add(controller2);
        list.add(controller3);
        list.add(controller4);
        list.add(controller5);
        
        for(Object item : list ) {
            Map map = (HashMap) item;
            if(categories.size() == 0) categories.add(map.get("category"));
            if(!categories.contains(map.get("category"))) categories.add( map.get("category"));
        }
        IconCanvas innerpanel = new IconCanvas();
        innerpanel.setLayout(new GridLayout(categories.size(),1));
        add(innerpanel, BorderLayout.NORTH);
        
        for(Object category : categories) {
            CategoryPanel innerMostPanel = new CategoryPanel(category.toString());
            for(Object item : list) {
                Map map = (HashMap) item;
                if(map.get("category").equals(category)) innerMostPanel.add(new Item(map.get("caption").toString()));
            }
            innerpanel.add(innerMostPanel);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="setter/getter">
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    
    public boolean isButtonBorderPainted() { return buttonBorderPainted; }
    public void setButtonBorderPainted(boolean buttonBorderPainted) { this.buttonBorderPainted = buttonBorderPainted; }
    
    public boolean isButtonContentFilled() { return buttonContentFilled; }
    public void setButtonContentFilled(boolean buttonContentFilled) { this.buttonContentFilled = buttonContentFilled; }
    
    public String getCaptionOrientation() { return captionOrientation; }
    public void setCaptionOrientation(String captionOrientation) { this.captionOrientation = captionOrientation; }
    
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="IconCanvas(JPanel)">
    private class IconCanvas extends JPanel {
        public IconCanvas() {
            //setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="CategoryPanel(JPanel)">
    private class CategoryPanel extends JPanel {
        
        private String caption;
        
        public CategoryPanel(String caption) {
            setBorder(new CategoryBorder(caption));
            setLayout(new FlowLayout(FlowLayout.LEFT));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Item(JButton)">
    private class Item extends JButton {
        public Item(String caption) {
            setText(caption);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="CategoryBorder(AbstractBorder)">
    private class CategoryBorder extends AbstractBorder{
        private String caption;
        
        public CategoryBorder(String caption) {
            this.caption = caption;
        }
        
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.decode("#657383"));
            FontMetrics fm = c.getFontMetrics(c.getFont());
            int fontheight = fm.getHeight();
            int fontwidth = fm.stringWidth(caption) + 10;
            int half = (fontheight / 2) + y + 4;
            
            g.drawLine(x,half, x + 4,half);
            g.drawLine(fontwidth, half, width - 1, half);
            g.setFont(c.getFont());
            g.setColor(Color.BLACK);
            g.drawString(caption, x + 8, fontheight + y + 2);
        }
        
        public Insets getBorderInsets(Component c) {
            return new Insets(c.getFontMetrics(c.getFont()).getHeight() + 4, 1, 1, 1);
        }
        
    }
    //</editor-fold>
    
}
