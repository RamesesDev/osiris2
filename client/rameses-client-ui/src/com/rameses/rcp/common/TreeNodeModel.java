package com.rameses.rcp.common;

public abstract class TreeNodeModel {
    
    private Node selectedNode;
    
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
}
