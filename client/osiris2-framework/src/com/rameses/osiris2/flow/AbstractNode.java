package com.rameses.osiris2.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class AbstractNode {
    
    private Map properties = new HashMap();
    private List transitions = new ArrayList();
    private PageFlow parent;
    private String title;
    
    public abstract String getName();
    
    public AbstractNode() {
    }

    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( ! (obj instanceof AbstractNode )) return false;
        return( this.getName().equals( ((AbstractNode)obj).getName() ) );
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("- - - - - - - - - - - - - - - - - - - - \n");
        sb.append( "Node Name:" + this.getName() + "\n" );
        if( transitions.size() > 0 ) {
            sb.append( "Transitions \n" );
            Iterator iter = transitions.iterator();
            while(iter.hasNext()) {
                sb.append( ((Transition)iter.next()).toString() + "\n" );
            }
        }    
        return sb.toString();
    }

    public List getTransitions() {
        return transitions;
    }

    public PageFlow getParent() {
        return parent;
    }

    public void setParent(PageFlow parent) {
        this.parent = parent;
    }

    public Map getProperties() {
        return properties;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
}
