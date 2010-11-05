/*
 * MenuProxy.java
 *
 * Created on November 5, 2010, 1:30 PM
 */

package com.rameses.rcp.control.menu;

import com.rameses.rcp.common.Node;
import com.rameses.rcp.common.TreeNodeModel;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.util.ValueUtil;
import javax.swing.JMenu;

/**
 *
 * @author jaycverg
 */
public class MenuProxy extends JMenu {
    
    private boolean init;
    private UIControl control;
    private TreeNodeModel nodeModel;
    private Node node;
    
    public MenuProxy(UIControl control, TreeNodeModel nodeModel, Node node) {
        this.control = control;
        this.node = node;
        this.nodeModel = nodeModel;
        super.setText( node.getCaption() );
        if ( !ValueUtil.isEmpty(node.getIcon()) )
            super.setIcon( ControlSupport.getImageIcon(node.getIcon()) );
        
        if ( !ValueUtil.isEmpty(node.getMnemonic()) )
            super.setMnemonic( node.getMnemonic().trim().charAt(0) );
    }
    
    public int getComponentCount() {
        if(!init) {
            for(Node n : nodeModel.fetchNodes(node) ) {
                if ( n.isLeaf() ) {
                    add( new MenuItemProxy( control, nodeModel, n ) );
                } else {
                    add( new MenuProxy( control, nodeModel, n ) );
                }
            }
            init = true;
        }
        return super.getComponentCount();
    }
    
}
