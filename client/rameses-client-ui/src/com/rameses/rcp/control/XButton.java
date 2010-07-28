package com.rameses.rcp.control;

import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.util.UICommandUtil;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.ui.UICommand;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ExpressionResolver;
import com.rameses.util.ValueUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;

/**
 *
 * @author jaycverg
 */
public class XButton extends JButton implements UICommand, ActionListener, Containable {
    
    private int index;
    private String[] depends;
    private Binding binding;
    private boolean immediate;
    private boolean update;
    private ControlProperty property = new ControlProperty();
    private String target;
    private boolean defaultCommand;
    private String expression;
    private Map params = new HashMap();
    private String permission;
    
    
    public XButton() {
        addActionListener(this);
    }
    
    
    public void refresh() {
        if ( !ValueUtil.isEmpty(expression) ) {
            ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
            Object value = er.evaluate(binding.getBean(), expression);
            setText( value+"" );
        }
    }
    
    public void load() {}
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void actionPerformed(ActionEvent e) {
        UICommandUtil.processAction(this);
    }
    
    //<editor-fold defaultstate="collapsed" desc="  Getters/Setters  ">
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
    
    public String getActionName() {
        return getName();
    }
    
    public boolean isImmediate() {
        return immediate;
    }
    
    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }
    
    public ControlProperty getControlProperty() {
        return property;
    }
    
    public String getTarget() {
        return target;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public boolean isUpdate() {
        return update;
    }
    
    public void setUpdate(boolean update) {
        this.update = update;
    }
    
    public boolean isDefaultCommand() {
        return defaultCommand;
    }
    
    public void setDefaultCommand(boolean defaultCommand) {
        this.defaultCommand = defaultCommand;
    }
    
    public String getExpression() {
        return expression;
    }
    
    public void setExpression(String expression) {
        this.expression = expression;
    }
    
    public Map getParams() {
        return params;
    }
    
    public void setParams(Map params) {
        this.params = params;
    }
    //</editor-fold>

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
    
}
