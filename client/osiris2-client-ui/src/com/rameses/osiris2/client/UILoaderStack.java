/*
 * UILoaderStack.java
 *
 * Created on May 27, 2010, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author elmo
 */
public class UILoaderStack<E> extends Stack<E> {
    
    private List loaders;
    
    public void setLoaders( List loaders ) {
        this.loaders = loaders;
    }
    
    public boolean empty() {
        if(loaders!=null && loaders.size()>0)
            return false;
        else
            return super.empty();
    }
    
    public E pop() {
        if( super.empty() ) {
            while(loaders.size()>0) {
                Invoker i = (Invoker)loaders.remove(0);
                String action = i.getAction();
                String target = (String)i.getProperties().get("target");
                
                ControllerProvider cp = ClientContext.getCurrentContext().getControllerProvider();
                UIController c = cp.getController( i.getWorkunitid() );
                if( target!=null && target.matches(".*process")) {
                    try {
                        c.init( null, action );
                        continue;
                    }
                    catch(Exception e) {
                        System.out.println("ERROR IN LOADER " + i.getWorkunitid() + ": " + e.getMessage());
                    }
                } 
                else {
//                    UIControllerContext ctx = new UIControllerContext();
//                    ctx.setAction( action);
//                    ctx.build(c, null);
//                    return (E) ctx;
                    c.init( null, action );
                    return (E) c;
                }
            }
            return null;
        } else {
            return (E)super.pop();
        }
    }
    
    
    
}
