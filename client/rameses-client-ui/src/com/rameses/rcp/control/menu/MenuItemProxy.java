/*
 * MenuProxy.java
 *
 * Created on November 5, 2010, 1:30 PM
 */

package com.rameses.rcp.control.menu;

import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.Node;
import com.rameses.rcp.common.TreeNodeModel;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.util.ValueUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;

/**
 *
 * @author jaycverg
 */
public class MenuItemProxy extends JMenuItem implements ActionListener {
    
    private UIControl control;
    private TreeNodeModel nodeModel;
    private Node node;
    
    public MenuItemProxy(UIControl control, TreeNodeModel nodeModel, Node node) {
        this.control = control;
        this.nodeModel = nodeModel;
        this.node = node;
        super.setText( node.getCaption() );
        if ( !ValueUtil.isEmpty(node.getIcon()) )
            super.setIcon( ControlSupport.getImageIcon(node.getIcon()) );
        
        if ( !ValueUtil.isEmpty(node.getMnemonic()) )
            super.setMnemonic( node.getMnemonic().trim().charAt(0) );
        
        addActionListener(this);
    }
    
    public void actionPerformed(ActionEvent e) {
        try {
            Object outcome = nodeModel.openLeaf( node );
            ControlSupport.fireNavigation(control, outcome);
            
        } catch(Exception ex){
            MsgBox.err(new IllegalStateException(ex));
        }
    }
}