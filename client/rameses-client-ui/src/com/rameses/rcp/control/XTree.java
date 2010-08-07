/*
 * XTree.java
 *
 * Created on August 2, 2010, 10:27 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Node;
import com.rameses.rcp.common.NodeListener;
import com.rameses.rcp.common.TreeNodeModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControlSupport;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


public class XTree extends JTree implements UIControl, TreeSelectionListener {
    
    private Binding binding;
    private int index;
    private String[] depends;
    private boolean dynamic;
    private String handler;
    
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private TreeNodeModel nodeModel;
    private Node selectedNode;
    private DefaultNode defaultNode;
    
    
    public XTree() {
        setCellRenderer( new NodeTreeRenderer() );
        
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
        root = new DefaultNode(nodeModel.getRootNode());
        
        model = new DefaultTreeModel(root, true);
        setModel(model);
        
        addTreeSelectionListener(this);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        //treat items w/ no children as folders unless explicitly defined as leaf
        model.setAsksAllowsChildren(true);
        addMouseListener(  new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if(me.getClickCount()==2) {
                    openNode(selectedNode);
                }
            }
        });
        
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,InputEvent.CTRL_MASK), "openNode");
        getActionMap().put("openNode", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                openNode(selectedNode);
            }
        });
    }
    
    private void openNode( Node node) {
        Object retVal = null;
        if( node.isLeaf() ) {
            retVal = nodeModel.openLeaf(node);
        } else {
            retVal = nodeModel.openFolder(node);
        }
        
        NavigationHandler handler = ClientContext.getCurrentContext().getNavigationHandler();
        NavigatablePanel panel = UIControlUtil.getParentPanel(this, null);
        handler.navigate(panel, this, retVal);
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
    
    public void valueChanged(TreeSelectionEvent e) {
        defaultNode = ((DefaultNode)e.getPath().getLastPathComponent());
        selectedNode = defaultNode.getNode();
        if(getName()!=null ) {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            res.setProperty(binding.getBean(), getName(), selectedNode);
            binding.notifyDepends(this);
        }
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    
    //<editor-fold defaultstate="collapsed" desc="  DefaultNode (class)  ">
    public class DefaultNode extends DefaultMutableTreeNode implements NodeListener {
        
        private Node node;
        
        public DefaultNode(String n) {
            super(n);
        }
        
        public DefaultNode(Node node) {
            super(node.getCaption(), !node.isLeaf());
            this.node = node;
            this.node.addListener(this);
        }
        
        public int getChildCount() {
            if( !node.isLoaded() ) {
                synchronized(this) {
                    node.setLoaded( true );
                    loadChildren();
                }
            }
            return super.getChildCount();
        }
        
        public void loadChildren() {
            Node[] nodes = nodeModel.fetchNodes(node);
            if( nodes !=null) {
                super.removeAllChildren();
                for(Node n: nodes ) {
                    this.add(new DefaultNode(n));
                }
            }
        }
        
        public Node getNode() {
            return node;
        }
        
        public void reload() {
            if ( !node.isLoaded() ) return;
            
            synchronized(this) {
                loadChildren();
                XTree.this.model.reload(this);
            }
        }
        
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  NodeTreeRenderer (class)  ">
    class NodeTreeRenderer extends DefaultTreeCellRenderer {
        
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree,value,selected,expanded,leaf,row,hasFocus);
            super.setText(value+"");
            super.setToolTipText(value+"");
            super.setBorder( BorderFactory.createEmptyBorder(1,1,1,1) );
            
            if( value !=null && (value instanceof DefaultNode)) {
                Node n = ((DefaultNode)value).getNode();
                if( n!=null ) {
                    if( n.getIcon()!=null ) {
                        String icon = n.getIcon();
                        ImageIcon ic = ControlSupport.getImageIcon(icon);
                        super.setIcon(ic);
                    }
                    if( n.getTooltip() !=null) {
                        super.setToolTipText(n.getTooltip());
                    }
                }
            }
            return this;
        }
        
    }
    //</editor-fold>
    
}
