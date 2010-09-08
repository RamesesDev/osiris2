/*
 * XActionBar.java
 *
 * Created on July 23, 2010, 1:21 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControlSupport;
import com.rameses.rcp.ui.UIComposite;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.common.ExpressionResolver;
import com.rameses.rcp.framework.UIController;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;


public class XActionBar2 extends JPanel implements UIComposite 
{   
    private Binding binding;
    private int index;
    private String[] depends;
    private boolean useToolBar;
    private boolean dynamic;
    private int spacing = 5;
    
    //private JToolBar toolBar = new JToolBar();
    //private FlowLayout panelLayout = new FlowLayout(FlowLayout.LEFT, 1, 1);
    private List<XButton> buttons = new ArrayList();
    
    
    public XActionBar2() 
    {
        super.setLayout(new Layout());
        setUseToolBar(true);
    }
    
    public void setLayout(LayoutManager mgr) {;} 
    
    /*
    public Dimension getPreferredSize() {
        if ( useToolBar ) {
            return toolBar.getPreferredSize();
        } else {
            return super.getPreferredSize();
        }
    }
    
    public void setPreferredSize(Dimension d) {
        if ( useToolBar ) {
            toolBar.setPreferredSize(d);
        } else {
            super.setPreferredSize(d);
        }
    }
    
    public Component add(Component comp) {
        if ( useToolBar ) {
            toolBar.add(comp);
        } else {
            super.add(comp);
        }
        
        return comp;
    }
    
    public void removeAll() {
        if ( useToolBar ) {
            toolBar.removeAll();
        } else {
            super.removeAll();
        }
    }
    */
    
    public void refresh() {
        if ( dynamic ) {
            buildButtons();
        }
        buildToolbar();
    }
    
    public void load() {
        if ( !dynamic ) {
            buildButtons();
        }
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }    
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void buildButtons() {
        buttons.clear();
        List<Action> actions = new ArrayList();
        
        //--get actions defined from the code bean
        Object value = null;
        try {
            value = UIControlUtil.getBeanValue(this);
        } catch(Exception e) {;}
        
        if ( value == null ) {
            //do nothing
        } else if ( value.getClass().isArray() ) {
            for(Action aa: (Action[]) value) {
                actions.add(aa);
            }
        } else if ( value instanceof Collection ) {
            actions.addAll( (Collection) value );
        }
        
        //--get actions defined from the action provider
        ActionProvider actionProvider = ClientContext.getCurrentContext().getActionProvider();
        if ( actionProvider != null ) {
            UIController controller = binding.getController();
            List<Action> aa = actionProvider.getActionsByType(getName(), controller);
            if ( aa != null ) {
                actions.addAll(aa);
            }
        }
        
        if ( actions.size() == 0 ) return;
        
        Collections.sort(actions);
        
        for( Action action: actions ) {
            //check permission
            String permission = action.getPermission();
            if(permission!=null && binding.getController().getName()!=null) {
                permission = binding.getController().getName()+"."+permission;
            }
            boolean allowed = ControlSupport.isPermitted(permission);
            if ( !allowed ) continue;
            
            XButton btn = new XButton();
            btn.setName( action.getName() );
            btn.setText( action.getCaption() );
            btn.setIndex( action.getIndex() );
            btn.setToolTipText( action.getTooltip() );
            btn.setIcon( ControlSupport.getImageIcon( action.getIcon() ) );
            btn.setUpdate( action.isUpdate() );
            btn.setImmediate( action.isImmediate() );
            btn.setMnemonic( action.getMnemonic() );
            btn.setBinding( binding );
            btn.putClientProperty("visibleWhen", action.getVisibleWhen());
            
            Map params = action.getParameters();
            if ( params != null && params.size() > 0 ) {
                btn.getParams().putAll( params );
            }
            
            buttons.add(btn);
        }
    }
    
    private void buildToolbar() {
        removeAll();
        
        ExpressionResolver expResolver = ClientContext.getCurrentContext().getExpressionResolver();
        for( XButton btn: buttons ) {
            String expression = (String) btn.getClientProperty("visibleWhen");
            if ( !ValueUtil.isEmpty(expression) ) {
                Object visible = expResolver.evaluate(binding.getBean(), expression);
                if ( !"true".equals(visible+"") ) {
                    continue;
                }
            }
            if ( !btn.isVisible() ) continue;
            
            add(btn);
        }
        
        SwingUtilities.updateComponentTreeUI(this);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
    public int getSpacing() { return spacing; }
    public void setSpacing(int spacing) { this.spacing = spacing; }
    
    public List<? extends UIControl> getControls() {
        return buttons;
    }
    
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
    
    public boolean isUseToolBar() {
        return useToolBar;
    }
    
    public void setUseToolBar(boolean useToolBar) {
        /*
        this.useToolBar = useToolBar;
        if ( useToolBar ) {
            super.removeAll();
            super.setLayout( new BorderLayout() );
            toolBar.removeAll();
            super.add( toolBar, BorderLayout.CENTER );
        } else {
            super.removeAll();
            super.setLayout( panelLayout );
        }
        
        if ( Beans.isDesignTime() ) {
            if ( !useToolBar ) {
                setOpaque(true);
                setBackground( new Color(214,218,222));
            }
            
            add(new JButton("Button 1"));
            add(new JButton("Button 2"));
        }
        */
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    /*
    public int getHorizontalAlignment() {
        return panelLayout.getAlignment();
    }
    
    public void setHorizontalAlignment(int horizontalAlignment) {
        panelLayout.setAlignment( horizontalAlignment );
    }
     */
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc=" Layout (Class) ">    
    private class Layout implements LayoutManager
    {
        public void addLayoutComponent(String name, Component comp) {;}
        public void removeLayoutComponent(Component comp) {;}

        public Dimension getLayoutSize(Container parent)
        {
            synchronized (parent.getTreeLock())
            {
                int w=0, h=0;
                
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++)
                {
                    if (!comps[i].isVisible()) continue;
                    
                    Dimension dim = comps[i].getPreferredSize();
                    w += (dim.width + getSpacing());
                    h = Math.max(h, dim.height); 
                }
                return new Dimension(w,h);
            }
        }
        
        public Dimension preferredLayoutSize(Container parent) {
            return getLayoutSize(parent); 
        } 

        public Dimension minimumLayoutSize(Container parent) { 
            return getLayoutSize(parent); 
        } 

        public void layoutContainer(Container parent) 
        { 
            synchronized (parent.getTreeLock()) 
            { 
                Insets margin = parent.getInsets(); 
                int x = margin.left;
                int y = margin.top;
                int w = parent.getWidth() - (margin.left + margin.right); 
                int h = parent.getHeight() - (margin.top + margin.bottom); 
              
                Component[] comps = parent.getComponents();
                for (int i=0; i<comps.length; i++)
                {
                    if (!comps[i].isVisible()) continue;
                    
                    Dimension dim = comps[i].getPreferredSize();
                    comps[i].setBounds(x, y, dim.width, h);
                    x += (dim.width + getSpacing());  
                }                
            } 
        } 
    } 
    //</editor-fold>
    
}
