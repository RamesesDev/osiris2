/*
 * XTree.java
 *
 * Created on August 2, 2010, 10:27 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Node;
import com.rameses.rcp.common.TreeNodeModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import javax.swing.JPanel;

public class XMenu extends JPanel implements UIControl {
    
    private Binding binding;
    private int index;
    private String[] depends;
    private boolean dynamic;
    private String handler;
    
    private TreeNodeModel nodeModel;
    private Node selectedNode;
    
    public XMenu() {
        
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
    
    public void refresh() {}
    
    public void load() {
        if( ValueUtil.isEmpty(handler) ) {
            throw new IllegalStateException( "XTree Error: A handler must be provided" );
        }
        nodeModel = (TreeNodeModel) UIControlUtil.getBeanValue(this, handler);
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public boolean isDynamic() {
        return dynamic;
    }
    
    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    
}
