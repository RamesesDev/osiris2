/*
 * PageFlowForm.java
 *
 * Created on January 27, 2010, 6:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.osiris2.client.WorkUnitUIController;
import com.rameses.osiris2.flow.SubProcessNode;
import com.rameses.osiris2.flow.Transition;
import com.rameses.rcp.annotations.Controller;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.framework.UIViewPanel;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.List;


public class PageFlowForm {
    
    @Controller
    private WorkUnitUIController controller;
    
    @com.rameses.rcp.annotations.Binding
    private Binding binding;
    
    
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
    
    /**
     * returns all the transitions List<Actions>
     */
    public List getFormActions() {
        if( controller.getWorkunit().isPageFlowCompleted() )
            return null;
        
        List trans = controller.getWorkunit().getTransitions();
        List actions = new ArrayList();
        for(Object o : trans ) {
            Transition t = (Transition)o;
            
            //non-rendered are not added to the transition list
            String rendExpr = t.getRendered();
            if ( !ValueUtil.isEmpty(rendExpr) ) {
                Object rend = evaluateExpression(this, rendExpr);
                if ( "false".equals(rend+"") ) {
                    continue;
                }
            }
            
            //hidden transitions are not displayed but are still added to the transitions list
            //this is helpful when you want to fire transition from the code bean
            String visibleExpr = t.getProperties().get("visible")+"";
            if ( !ValueUtil.isEmpty(visibleExpr) ) {
                Object visible = evaluateExpression(this, visibleExpr);
                if ( "false".equals(visible+"") ) {
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
            
            Object immediate = t.getProperties().get("immediate");
            if(immediate!=null) {
                a.setImmediate( "true".equals(immediate+"") );
            }
            
            String mnem = (String) t.getProperties().get("mnemonic");
            if( mnem != null ) {
                a.setMnemonic(mnem.trim().charAt(0));
            }
            
            //add action to action list
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
    
    public final Object signal() {
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
    
    public void fireTransition(String name) {
        WorkUnitInstance wu = controller.getWorkunit();
        wu.signal( name );
        Object outcome = null;
        if( wu.isPageFlowCompleted()) {
            outcome = "_close";
        } else if( wu.getCurrentNode() instanceof SubProcessNode ) {
            String expr = wu.getCurrentNode().getName();
            String value = (String) evaluateExpression(this, expr);
            outcome = callSubProcess( value );
        } else {
            outcome = wu.getCurrentPage().getName();
        }
        
        UIViewPanel panel = (UIViewPanel) getBinding().getProperties().get(UIViewPanel.class);
        NavigatablePanel navPanel = UIControlUtil.getParentPanel(panel, null);
        NavigationHandler nh = ClientContext.getCurrentContext().getNavigationHandler();
        if ( nh != null ) {
            nh.navigate(navPanel, null, outcome);
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
    
    public Binding getBinding() {
        return binding;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
}
