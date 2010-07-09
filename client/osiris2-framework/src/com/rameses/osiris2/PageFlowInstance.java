package com.rameses.osiris2;

import com.rameses.osiris2.flow.AbstractNode;
import com.rameses.osiris2.flow.EndNode;
import com.rameses.osiris2.flow.PageFlow;
import com.rameses.osiris2.flow.PageNode;
import com.rameses.osiris2.flow.ProcessNode;
import com.rameses.osiris2.flow.StartNode;
import com.rameses.osiris2.flow.Transition;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PageFlowInstance {
    
    private AbstractNode currentNode;
    private String name;
    private WorkUnitInstance workunit;
    
    PageFlowInstance( WorkUnitInstance wui ) {
        this.workunit = wui;
        currentNode = wui.getPageFlow().getStart();
    }
    
    public PageFlow getPageFlow() {
        return workunit.getPageFlow();
    }
    
    public void signal() {
        signal( null );
    }
    
    public void signal(String n ){
        currentNode = signalNode( currentNode, n );
    }
    
    private Map createBeanMap() {
        Map map = new HashMap();
        map.put("bean", workunit.getController());
        return map;
    }
    
    private AbstractNode signalNode(AbstractNode prevNode, String n ){
        try {
            Map map = createBeanMap();
            Transition t = findTransition(prevNode, n, map);
            fireTransitionAction(t);
            AbstractNode tempNode = findNode(t.getTo());
            fireNodeAction(tempNode);
            if( tempNode instanceof ProcessNode ) {
                return signalNode( tempNode, null );
            } else {
                return tempNode;
            }
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    
    private Transition findTransition(AbstractNode node, String name, Map params) {
        Iterator iter = node.getTransitions().iterator();
        if( name !=null && name.length() > 0 ) {
            while(iter.hasNext()) {
                Transition t = (Transition)iter.next();
                if( isRendered(t, params) && name.equals(t.getName())) {
                    return t;
                }
            }
        } else {
            while(iter.hasNext()) {
                Transition t = (Transition)iter.next();
                if( isRendered(t, params) && isMatchedCondition(t,params) ) {
                    return t;
                }
            }
        }
        return null;
    }
    
    
    private boolean isMatchedCondition(Transition t, Map map) {
        if( t.getCond() != null ) {
            try {
                Object o = evalExpr(t.getCond(), map);
                return Boolean.valueOf( o + "").booleanValue();
            }
            catch(Exception ex) {
                System.out.println("error pageflow rendered expr " + t.getName() + "->" + ex.getMessage());
                return false;
            }
        }
        return true;
    }
    
    private boolean isRendered(Transition t, Map map) {
        if( t.getRendered() != null ) {
            try {
                Object o = evalExpr(t.getRendered(), map);
                return  Boolean.valueOf( o + "").booleanValue();
            }
            catch(Exception ex) {
                System.out.println("error pageflow rendered expr " + t.getName() + "->" + ex.getMessage());
                return false;
            }
        }
        return true;
    }
    
    private Object evalExpr(String expr, Map params) {
        ExpressionProvider ep = workunit.getModule().getAppContext().getExpressionProvider();
        if( ep != null  ) {
            return ep.eval(expr, params);
        }
        throw new IllegalStateException("Please provide an ExpressionProvider");
    }
    
    private AbstractNode findNode(String n) {
        Iterator iter = getPageFlow().getNodes().iterator();
        while(iter.hasNext()) {
            AbstractNode anode = (AbstractNode)iter.next();
            if( anode.getName().equals(n)) {
                return anode;
            }
        }
        return null;
    }
    
    private void fireTransitionAction(Transition t) {
        if( t.getAction() != null ) {
            invokeMethod( workunit.getController(), t.getAction() );    
        }
    }
    
    private void fireNodeAction(AbstractNode aNode) {
        String act = null;
        if(aNode instanceof PageNode) {
            act = ((PageNode)aNode).getAction();
        } else if (aNode instanceof ProcessNode )  {
            act = ((ProcessNode)aNode).getAction();
        }
        if( act != null ) {
            invokeMethod( workunit.getController(), act );
        }
    }

    private void invokeMethod(Object o, String action ) {
        try {
            Method m = o.getClass().getMethod(action, new Class[] {});
            m.invoke( o, new Object[]{} );
        } 
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public List getTransitions() {
        //return only transitions that are rendered
        Map map = createBeanMap();
        List trans = new ArrayList();
        Iterator iter = currentNode.getTransitions().iterator();
        while(iter.hasNext()) {
            Transition t = (Transition)iter.next();
            if( isRendered( t, map ) == true ) trans.add(t);
        }
        return trans;
    }
    
    public AbstractNode getCurrentNode() {
        return currentNode;
    }
    
    public boolean isEnded() {
        return (currentNode instanceof EndNode);
    }
    
    public boolean isStarted() {
        return !(currentNode instanceof StartNode);
    }
    
}
