/*
 * XPanel.java
 *
 * Created on November 6, 2010, 2:00 PM
 *
 */

package com.rameses.rcp.control;

import com.rameses.common.ExpressionResolver;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.ControlContainer;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import javax.swing.JPanel;

/**
 *
 * @author jaycverg
 */
public class XPanel extends JPanel implements UIControl, ControlContainer {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String visibleWhen;
    
    
    public XPanel() {}
    
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
        if ( ValueUtil.isEmpty(visibleWhen) ) return;
        
        ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
        Object res = er.evaluate(binding.getBean(), visibleWhen);
        if ( "false".equals(res+"") )
            setVisible(false);
        else
            setVisible(true);
    }
    
    public void load() {}
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public boolean isHasNonDynamicContents() {
        return true;
    }
    
    public String getVisibleWhen() {
        return visibleWhen;
    }
    
    public void setVisibleWhen(String visibleWhen) {
        this.visibleWhen = visibleWhen;
    }
    
}
