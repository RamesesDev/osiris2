/*
 * PageFlowForm.java
 *
 * Created on January 27, 2010, 6:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.common.annotations.Controller;
import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.osiris2.client.WorkUnitUIController;
import com.rameses.osiris2.flow.SubProcessNode;
import com.rameses.osiris2.flow.Transition;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.ClientContext;
import java.util.ArrayList;
import java.util.List;


public class PageFlowForm {
    
    @Controller
    private WorkUnitUIController controller;
    
    
    public PageFlowForm() {
    }
    
    private Transition transition;
    
    public String getFormTitle() {
        if( !controller.getWorkunit().isPageFlowCompleted() ) {
            String expr = controller.getWorkunit().getCurrentNode().getTitle();
            return (String) evaluateExpression(this, expr);
        } else
            return null;
    }
    
    public List getFormActions() {
        if( controller.getWorkunit().isPageFlowCompleted() )
            return null;
        
        List trans = controller.getWorkunit().getTransitions();
        List actions = new ArrayList();
        for(Object o : trans ) {
            Transition t = (Transition)o;
            String renderedExpr = t.getRendered();
            if ( renderedExpr != null && renderedExpr.trim().length() > 0 ) {
                renderedExpr = renderedExpr.trim();
                if ( renderedExpr.toLowerCase().equals("false"))
                    continue;
                
                if ( renderedExpr.matches("#\\{.+?\\}")) {
                    Boolean rendered = (Boolean) evaluateExpression(this, renderedExpr);
                    if ( !rendered )
                        continue;
                }
            }
            Action a = new Action();
            a.setName( "signal" );
            String caption = (String)t.getProperties().get("caption");
            if(caption==null) {
                caption = t.getName();
            }
            a.setCaption( caption );
            a.getParameters().put( "transition", t );
            String icon = (String)t.getProperties().get("icon");
            if(icon!=null) {
                a.setIcon(icon);
            }
            String immediate = (String)t.getProperties().get("immediate");
            if(immediate!=null) {
                try {
                    a.setImmediate( Boolean.parseBoolean(immediate) );
                } catch(Exception ign){;}
            }
            String mnem = (String) t.getProperties().get("mnemonic");
            if( mnem != null ) {
                a.setMnemonic(mnem.trim().charAt(0));
            }
            actions.add(a);
        }
        return actions;
    }
    
    public Transition getTransition() {
        return transition;
    }
    
    public void setTransition(Transition transition) {
        this.transition = transition;
    }
    
    public String start() {
        return start(null);
    }
    
    public String start(String name) {
        if( name == null )
            controller.getWorkunit().start();
        else
            controller.getWorkunit().start(name);
        return controller.getWorkunit().getCurrentPage().getName();
    }
    
    public Object signal() {
        WorkUnitInstance wu = controller.getWorkunit();
        if( transition.getTo() == null ) {
            wu.signal( null );
        } else {
            wu.signal( transition.getName() );
        }
        if( wu.isPageFlowCompleted()) {
            return "_close";
        } else if( wu.getCurrentNode() instanceof SubProcessNode ) {
            String expr = wu.getCurrentNode().getName();
            String value = (String) evaluateExpression(this, expr);
            return callSubProcess( value );
        } else {
            return wu.getCurrentPage().getName();
        }
    }
    
    //overridable
    public Opener callSubProcess( String name ) {
        return new Opener(name);
    }
    
    //helper
    private Object evaluateExpression(Object bean, String expr) {
        return ClientContext.getCurrentContext().getExpressionResolver().evaluate(bean, expr);
    }
}
