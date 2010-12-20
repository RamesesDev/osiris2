package com.rameses.rcp.common;

import java.util.List;

public abstract class TreeNodeModel {
    
    private Node selectedNode;
    private TreeNodeModelListener listener;
    
    public TreeNodeModel() {
    }
    
    public abstract Node[] fetchNodes( Node node );
    
    public Node getRootNode() {
        return new Node("root", "All");
    }
    
    public Object openLeaf(Node node){
        //do nothing
        return null;
    }
    
    public Object openFolder(Node node) {
        //do nothing
        return null;
    }
    
    public Node getSelectedNode() {
        return selectedNode;
    }
    
    public void setSelectedNode(Node selectedNode) {
        this.selectedNode = selectedNode;
    }
    
    public Object openSelected() {
        if ( selectedNode == null ) return null;
        
        if ( selectedNode.isLeaf() )
            return openLeaf( selectedNode );
        
        return openFolder( selectedNode );
    }
    
    public TreeNodeModelListener getListener() {
        return listener;
    }
    
    public void setListener(TreeNodeModelListener listener) {
        this.listener = listener;
    }
    
    public final Node findNode(NodeFilter filter) {
        if ( listener == null )
            throw new RuntimeException("No TreeNodeModelListener found.");
        
        return listener.findNode(filter);
    }
    
    public final List<Node> findNodes(NodeFilter filter) {
        if ( listener == null )
            throw new RuntimeException("No TreeNodeModelListener found.");
        
        return listener.findNodes(filter);
    }
}
