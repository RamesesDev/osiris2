/*
 * XActionBar.java
 *
 * Created on July 23, 2010, 1:21 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControlSupport;
import com.rameses.rcp.ui.UIComposite;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ExpressionResolver;
import com.rameses.util.ValueUtil;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;


public class XActionBar extends JToolBar implements UIComposite {
    
    private Binding binding;
    private int index;
    private String[] depends;
    
    private List<XButton> buttons = new ArrayList();
    
    
    
    public XActionBar() {
        setFloatable(false);
        
        if ( Beans.isDesignTime() ) {
            add(new JButton("Button 1"));
            add(new JButton("Button 2"));
        }
    }
    
    public void refresh() {
        buildToolbar();
    }
    
    public void load() {
        buildButtons();
        buildToolbar();
    }
    
    private void buildButtons() {
        buttons.clear();
                
        Action[] actions = null;
        Object value = UIControlUtil.getBeanValue(this);
        if ( value == null ) return;
        
        if ( value.getClass().isArray() ) {
            actions = (Action[]) value;
        } else if ( value instanceof Collection ) {
            actions = (Action[]) ((Collection) value).toArray(new Action[]{});
        }
        
        if ( actions == null ) return;
        
        Arrays.sort(actions);
        
        for( Action action: actions ) {
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
            
            buttons.add(btn);
        }
    }
    
    private void buildToolbar() {
        removeAll();

        ExpressionResolver expResolver = ClientContext.getCurrentContext().getExpressionResolver();
        for( XButton btn: buttons ) {
            //check permission here
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
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
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
    //</editor-fold>
    
}
