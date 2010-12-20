/*
 * TreeNodeModelListener.java
 *
 * Created on December 20, 2010, 3:40 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import java.util.List;


public interface TreeNodeModelListener {
    
    Node findNode(NodeFilter filter);
    List<Node> findNodes(NodeFilter filter);
    
}
