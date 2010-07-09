package com.rameses.osiris2.flow;

public class ProcessNode extends AbstractNode{
    
    private String name;
    private String action;
    
    public ProcessNode() {
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

}
