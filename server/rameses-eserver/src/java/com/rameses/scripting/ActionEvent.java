/*
 * ActionEvent.java
 *
 * Created on June 28, 2009, 5:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * info regarding the target method and method
 */
public class ActionEvent implements Serializable {
    
    private String sourceName;
    private String methodName;
    private Object[] args;
    private Object result;
    private Map env;

    public ActionEvent(String name, String m, Object[] args, Map env) {
        this.sourceName = name;
        this.methodName = m;
        this.args = args;
        this.env = env;
        if(env==null) env = new HashMap();
    }
    
    public String getMethodName() {
        return methodName;
    }

    public String getSourceName() {
        return sourceName;
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
        methodName = null;
        result = null;
    }

    public Map getEnv() {
        return env;
    }
    
}
