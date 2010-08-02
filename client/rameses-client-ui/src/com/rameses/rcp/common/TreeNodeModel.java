package com.rameses.rcp.common;

public abstract class TreeNodeModel {
    
   
    
    public TreeNodeModel() {
    }

    public abstract Node[] fetchNodes( Node node );
    
    public Node getRootNode() {
        return new Node("root", "Root");
    }
    
    public Object openLeaf(Node node){
        //do nothing
        return null;
    } 
    
    public Object openFolder(Node node) {
        //do nothing
        return null;
    }

   
    
    
}
