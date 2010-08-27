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
    private String exclude;
    
    public InterceptorDef(String scriptName, Method m, int idx, String pattern, String expr, String exclude) {
        this.pattern = pattern;
        this.scriptName = scriptName;
        this.index = idx;
        this.method = m.getName();
        if(exclude!=null && exclude.trim().length()>0)
            this.exclude = exclude;
        
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
        InterceptorDef idf = (InterceptorDef)o;
        if( getIndex() < idf.getIndex() ) {
            return -1;
        } else if( getIndex() > idf.getIndex() ) {
            return 1;
        }
        return 0;
    }
    
    public boolean accept(String name) {
        boolean passPattern = false;
        if( pattern == null || pattern.trim().length() == 0 ) {
            passPattern = true;
        }
        else if( name.matches(pattern)) {
            passPattern = true;
        } 
        
        if(!passPattern) return false;
        if(exclude==null) return true;
        if(name.matches(exclude)) return false;
        return true;
    }

    public String getMethod() {
        return method;
    }
    
    public boolean hasParam() {
        return hasParam;
    }
    
    public String getSignature() {
        StringBuffer sb = new StringBuffer();
        if(hasParam) sb.append("@");
        sb.append( this.scriptName + "." + method);
        if(expr!=null && expr.trim().length()>0) {
            sb.append("#"+expr);
        }
        return sb.toString();
    }
    
}
