/*
 * ActionInvoker.java
 *
 * Created on September 15, 2010, 12:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules.common;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;

public class RuleAction implements Serializable {
    
    public static final String GLOBAL_NAME = "action";
    
    private Map<String, RuleActionHandler> commands = new Hashtable();
    private Object context;
    private String name = "action";
    
    public void setContext(Object context) {
        this.context = context;
    }
    
    public Object getContext() {
        return context;
    }
    
    public RuleAction() {
    }
    
    public void addCommand(String name, RuleActionHandler handler ) {
        commands.put(name, handler);
    }
    
    
    public void execute( String action, Object params ) {
        RuleActionHandler handler = commands.get(action);
        if(handler!=null) {
            handler.execute( context, params );
        } 
        else {
            System.out.println("No command found for "+action +". No action executed");
        }
    }

    public void destroy() {
        commands.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
