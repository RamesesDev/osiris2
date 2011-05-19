/*
 * XTree.java
 *
 * Created on August 2, 2010, 10:27 AM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.Node;
import com.rameses.rcp.common.NodeFilter;
import com.rameses.rcp.common.NodeListener;
import com.rameses.rcp.common.TreeNodeModel;
import com.rameses.rcp.common.TreeNodeModelListener;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.common.PropertyResolver;
import com.rameses.util.ValueUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.tree.TreeSelectionModel;


public class XTree extends JTree implements UIControl, TreeSelectionListener, TreeNodeModelListener {
    
    private Binding binding;
    private int index;
    private String[] depends;
    private boolean dynamic;
    private String handler;
    
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private TreeNodeModel nodeModel;
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
        nodeModel.setListener(this);
        
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
                    openNode(nodeModel.getSelectedNode());
                }
            }
        });
        
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0), "openNode");
        getActionMap().put("openNode", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                openNode(nodeModel.getSelectedNode());
            }
        });
    }
    
    private void openNode( Node node) {
        Object retVal = null;
        if( node == null ); //do nothing
        else if( node.isLeaf() ) {
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
        nodeModel.setSelectedNode( defaultNode.getNode() );
        if(getName()!=null ) {
            PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
            res.setProperty(binding.getBean(), getName(), nodeModel.getSelectedNode());
            binding.notifyDepends(this);
        }
    }
    
    public String getHandler() {
        return handler;
    }
    
    public void setHandler(String handler) {
        this.handler = handler;
    }
    
    public Node findNode(NodeFilter filter) {
        DefaultNode parent = (DefaultNode) model.getRoot();
        Node n = parent.getNode();
        if ( filter.accept(n) ) return n;
        
        return doFindNode(parent, filter);
    }
    
    private Node doFindNode(DefaultNode parent, NodeFilter filter) {
        for(int i = 0; i < parent.getChildCount(); ++i) {
            DefaultNode child = (DefaultNode) parent.getChildAt(i);
            Node n = child.getNode();
            if ( filter.accept(n) ) return n;
            
            if ( n.isLoaded() && child.getChildCount() > 0 ) {
                Node nn = doFindNode(child, filter);
                if ( nn != null ) return nn;
            }
        }
        return null;
    }
    
    public List<Node> findNodes(NodeFilter filter) {
        List<Node> nodes = new ArrayList();
        DefaultNode parent = (DefaultNode) model.getRoot();
        Node n = parent.getNode();
        if ( filter.accept(n) ) nodes.add(n);
        
        doCollectNodeList(parent, filter, nodes);
        
        return nodes;
    }
    
    private void doCollectNodeList(DefaultNode parent, NodeFilter filter, List nodes) {
        for(int i = 0; i < parent.getChildCount(); ++i) {
            DefaultNode child = (DefaultNode) parent.getChildAt(i);
            Node n = child.getNode();
            if ( filter.accept(n) ) nodes.add(n);
            
            if ( n.isLoaded() && child.getChildCount() > 0 ) {
                doCollectNodeList(child, filter, nodes);
            }
        }
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
