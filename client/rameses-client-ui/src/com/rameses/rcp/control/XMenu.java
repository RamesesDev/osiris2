/*
 * XMenu.java
 *
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Node;
import com.rameses.rcp.common.TreeNodeModel;
import com.rameses.rcp.control.menu.MenuProxy;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.beans.Beans;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

public class XMenu extends JPanel implements UIControl {
    
    private Binding binding;
    private int index;
    private String[] depends;
    private boolean dynamic;
    private String handler;
    
    private TreeNodeModel nodeModel;
    private Node selectedNode;
    
    private JMenuBar menuBar;
    
    
    public XMenu() {
        super.setLayout(new BorderLayout());
        setOpaque(false);
        
        if ( Beans.isDesignTime() ) {
            JMenuBar jmb = new JMenuBar();
            jmb.add(new JMenu("XMenu1"));
            add(jmb);
        }
        
    }
    
    public void setLayout(LayoutManager mgr) {;}
    
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
        if ( dynamic )
            buildPlainMenu( null );
    }
    
    public void load() {
        if( ValueUtil.isEmpty(handler) ) {
            throw new IllegalStateException( "XTree Error: A handler must be provided" );
        }
        nodeModel = (TreeNodeModel) UIControlUtil.getBeanValue(this, handler);
        
        menuBar = new JMenuBar();
        add(menuBar);
        if ( !dynamic )
            buildPlainMenu( null );
    }
    
    private void buildPlainMenu( Node parent ) {
        menuBar.removeAll();
        Node[] nodes = nodeModel.fetchNodes( nodeModel.getRootNode() );
        if ( parent == null ) {
            for(Node n : nodes) {
                menuBar.add(new MenuProxy( this, nodeModel, n ));
            }
        }
        menuBar.revalidate();
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
