/*
 * ActionEvent.java
 *
 * Created on June 28, 2009, 5:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.lang.reflect.Method;

/**
 * info regarding the target method and method
 */
public class ActionEvent {
    
    private String sourceName;
    private Object source;
    private Method method;
    private Object[] args;
    private Object result;

    public ActionEvent(String name, Object source, Method m, Object[] args) {
        this.sourceName = name;
        this.source = source;
        this.method = m;
        this.args = args;
    }
    
    public String getMethodName() {
        return method.getName();
    }

    public String getSourceName() {
        return sourceName;
    }

    public Object getSource() {
        return source;
    }

    public Method getMethod() {
        return method;
    }

    public Object[] getArgs() {
        return args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    
    public void destroy() {
        args = null;
        sourceName = null;
        source = null;
        method = null;
        result = null;
    }
    
}
