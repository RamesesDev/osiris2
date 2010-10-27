/*
 * ScriptEval.java
 *
 * Created on April 4, 2010, 8:49 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 *
 * @author elmo
 */
public final class ScriptEval {
    
    private GroovyShell shell;
    
    public ScriptEval(ActionEvent ae) {
        Binding b = new Binding();
        b.setVariable("args", ae.getArgs());
        b.setVariable("env",ae.getEnv());
        shell = new GroovyShell(b);
    }
    
    public void setResult(Object result) {
        shell.setVariable("result", result);
    }
    
    public boolean eval(String expr) {
        try {
            Boolean b = (Boolean)shell.evaluate(expr);
            return b.booleanValue();
        }
        catch(Exception ign){
            System.out.println("error evaluate " + ign.getMessage());
            return false;
        }
    }
    
    public void destroy() {
        shell.initializeBinding();
        shell = null;
    }
    
}
