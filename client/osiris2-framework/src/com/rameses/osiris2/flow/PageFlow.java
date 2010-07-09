/*
 * FlowDef.java
 *
 * Created on February 20, 2009, 8:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.flow;

import com.rameses.osiris2.PageFlowInstance;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author elmo
 */
public class PageFlow {
    
    private List nodes = new ArrayList();
    private String name;
    
    private StartNode start;
    private EndNode end;
    private String title;
    
    public PageFlow() {
    }

    public void addNode( AbstractNode n ) {
        this.getNodes().add( n );
        if( n instanceof StartNode ) start = (StartNode)n;
        if( n instanceof EndNode ) end = (EndNode)n;
        n.setParent(this);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n");
        sb.append( "Page flow: " + this.getName() + "\n" );
        Iterator iter = nodes.iterator();
        while(iter.hasNext()) {
            sb.append( ((AbstractNode)iter.next()).toString() );
        }
        return sb.toString();
    }

    public StartNode getStart() {
        return start;
    }

    public EndNode getEnd() {
        return end;
    }

    public List getNodes() {
        return nodes;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    
}
