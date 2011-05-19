package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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
    
    private List<XButton> buttons = new ArrayList();
    private Binding binding;
    private String[] depends;
    private int index;
    private boolean sBtnBorderPainted;
    private String sCaptionOrientation = "BOTTOM";
    
    public XIconPanel() {
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
        
        init();
    }
    
    public void init() {
        setBorder(BorderFactory.createEtchedBorder());
        setLayout(new BorderLayout());
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
            if(categories.size() == 0)
                categories.add(map.get("category"));
            
            if(!categories.contains(map.get("category"))) {
                categories.add( map.get("category"));
            }
        }
        InnerPanel innerpanel = new InnerPanel();
        innerpanel.setLayout(new GridLayout(categories.size(),1));
        add(innerpanel, BorderLayout.NORTH);
        
        for(Object category : categories) {
            InnerMostPanel innerMostPanel = new InnerMostPanel(category.toString());
            for(Object item : list) {
                Map map = (HashMap) item;
                if(map.get("category").equals(category)) {
                    innerMostPanel.add(new Item(map.get("caption").toString()));
                }
            }
            innerpanel.add(innerMostPanel);
        }
    }
    
    public void refresh() {
    }
    
    public void load() {
        List<Action> actions = new ArrayList();
        categories.clear();
        
        ActionProvider actionProvider = ClientContext.getCurrentContext().getActionProvider();
        if (actionProvider != null) {
            List<Action> aa = actionProvider.getActionsByType(getName(), null);
            if (aa != null) actions.addAll(aa);
        }
        
        for(Action a : actions) {
            Map map = a.getProperties();
            if(categories.size() == 0) categories.add(map.get("category"));
            if(!categories.contains(map.get("category"))) categories.add( map.get("category"));
        }
        
        InnerPanel innerpanel = new InnerPanel();
        innerpanel.setLayout(new GridLayout(categories.size(),1));
        add(innerpanel, BorderLayout.NORTH);
        
        for(Object category : categories) {
            InnerMostPanel innerMostPanel = new InnerMostPanel(category.toString());
            for(Action a : actions) {
                Map map = a.getProperties();
                IconButton ibtn;
                if(map.get("category").equals(category)) {
                    ibtn = new IconButton(a.getCaption(), a.getIcon());
                    ibtn.setBinding(binding);
                    ibtn.setName(a.getName());
                    ibtn.setPermission(a.getPermission());
                    ibtn.setParams(a.getParameters());
                    ibtn.setCaptionOrientation(getSCaptionOrientation());
                    ibtn.setBtnBorderPainted(getSBtnBorderPainted());
                    ibtn.setToolTipText(a.getTooltip());
                    innerMostPanel.add(ibtn);
                    ibtn.load();
                }
            }
            innerpanel.add(innerMostPanel);
        }
        SwingUtilities.updateComponentTreeUI(this);
    }
    
    //<editor-fold defaultstate="collapsed" desc="setter/getter">
    public boolean getSBtnBorderPainted() { return sBtnBorderPainted;}
    public void setSBtnBorderPainted(boolean sBtnBorderPainted) { this.sBtnBorderPainted = sBtnBorderPainted;}
    
    public String[] getDepends() { return depends; }
    public void setDepends(String[] depends) { this.depends = depends; }
    
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    
    public void setBinding(Binding binding) { this.binding = binding; }
    public Binding getBinding() { return binding; }
    
    public String getSCaptionOrientation() { return sCaptionOrientation; }
    public void setSCaptionOrientation(String sCaptionOrientation) { this.sCaptionOrientation = sCaptionOrientation; }
    
    public int compareTo(Object o) { return UIControlUtil.compare(this, o); }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="InnerPanel(JPanel)">
    private class InnerPanel extends JPanel {
        public InnerPanel() {
            //setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="InnerMostPanel(JPanel)">
    private class InnerMostPanel extends JPanel {
        public InnerMostPanel(String caption) {
            setBorder(BorderFactory.createTitledBorder(caption));
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
}
