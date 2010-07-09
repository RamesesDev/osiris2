/*
 * InvokerDef.java
 *
 * Created on December 14, 2009, 4:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.lang.reflect.Method;


public class InterceptorDef implements Comparable {
    
    private String scriptName;
    private String method;
    private int index;
    private String pattern;
    private boolean hasParam;
    private String expr;
    
    public InterceptorDef() {
    }
    
    public InterceptorDef(String scriptName, Method m, int idx, String pattern, String expr) {
        this.pattern = pattern;
        this.scriptName = scriptName;
        this.index = index;
        this.method = m.getName();
        if( m.getParameterTypes()!=null && m.getParameterTypes().length>0) {
            this.hasParam = true;
        }
        if(expr!=null) {
            //this is just for brevity's sake. remove #{} characters.
            this.expr = expr.replaceAll("#\\{|\\}", "");
        }
    }
    
    public String getScriptName() {
        return scriptName;
    }
    
    public int getIndex() {
        return index;
    }
    
    public String getPattern() {
        return pattern;
    }
    
    public int compareTo(Object o) {
        if( o instanceof InterceptorDef ) return 0;
        InterceptorDef idf = (InterceptorDef)o;
        if( getIndex() < idf.getIndex() ) {
            return -1;
        } else if( getIndex() > idf.getIndex() ) {
            return 1;
        }
        return 0;
    }
    
    public boolean accept(String name) {
        if( pattern == null || pattern.trim().length() == 0 )
            return true;
        if( name.matches(pattern)) {
            return true;
        } else {
            return false;
        }
    }

    public String getMethod() {
        return method;
    }
    
    public boolean hasParam() {
        return hasParam;
    }
    
    public String getSignature() {
        StringBuffer sb = new StringBuffer();
        if(hasParam) sb.append("~");
        sb.append( this.scriptName + "." + method);
        if(expr!=null && expr.trim().length()>0) {
            sb.append("#"+expr);
        }
        return sb.toString();
    }
    
}
