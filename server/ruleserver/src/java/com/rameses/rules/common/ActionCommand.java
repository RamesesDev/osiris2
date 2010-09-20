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

public class ActionCommand implements Serializable {
    
    public final static String GLOBAL_NAME = "command";
    
    private Map<String,CommandHandler> commands = new Hashtable();
    private Object context;
    
    public void setContext(Object context) {
        this.context = context;
    }
    
    public Object getContext() {
        return context;
    }
    
    public ActionCommand() {
    }
    
    public void addCommand(String name, CommandHandler handler ) {
        commands.put(name, handler);
    }
    
    
    public void execute( String action, Object params ) {
        CommandHandler handler = commands.get(action);
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

    
}
