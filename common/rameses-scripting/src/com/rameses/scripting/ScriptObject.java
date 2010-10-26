/*
 * ScriptObjectPoolManager.java
 *
 * Created on October 22, 2010, 8:59 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.annotations.Param;
import com.rameses.classutils.ClassDef;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * this contains only one type of object
 * returns the active object.
 */
public class ScriptObject {
    
    private String name;
    private ScriptLoader scriptLoader;
    private BlockingQueue<ScriptObjectPoolItem> pool = new LinkedBlockingQueue();
    
    //store the method name and
    private int interceptorModifiedVersion;
    private Map<String, List<String>> beforeInterceptors = new HashMap();
    private Map<String, List<String>> afterInterceptors = new HashMap();
    //new addition: parameters will be checked for validitiy.
    private Map<String, CheckedParameter[]> checkedParameters = new HashMap();
    private Set<String> methodsAccessed = new HashSet();
    private Map<String,Integer> timesAccessed = new HashMap();
    
    private int minPoolSize = 5;
    
    public ScriptObject(String name, ScriptLoader loader) {
        this.name = name;
        this.scriptLoader = loader;
    }
    
    public void init() {
        for( int i = 0; i< minPoolSize; i++) {
            ScriptObjectPoolItem o = scriptLoader.findScript(name);
            o.setScriptObject(this);
            pool.add( o );
        }
    }
    
    public ScriptObjectPoolItem getPooledObject() {
        ScriptObjectPoolItem o = pool.poll();
        if(o==null) {
            o = scriptLoader.findScript( name );
            o.setScriptObject(this);
        }
        return o;
    }
    
    public void addBackToPool(ScriptObjectPoolItem p) {
        pool.add( p );
    }
    
    public int getMinPoolSize() {
        return minPoolSize;
    }
    
    public int getEstimatedActivePoolSize() {
        return pool.size();
    }
    
    public Map<String, List<String>> getBeforeInterceptors() {
        return beforeInterceptors;
    }
    
    public Map<String, List<String>> getAfterInterceptors() {
        return afterInterceptors;
    }
    
    public List<String> findBeforeInterceptors(String method) {
        if(!beforeInterceptors.containsKey(method)) return new ArrayList();
        return beforeInterceptors.get(method);
    }
    
    public List<String> findAfterInterceptors( String method ) {
        if(!afterInterceptors.containsKey(method)) return new ArrayList();
        return afterInterceptors.get(method);
    }
    
    public int getInterceptorModifiedVersion() {
        return interceptorModifiedVersion;
    }
    
    public void setInterceptorModifiedVersion( int v ) {
        interceptorModifiedVersion = v;
    }
    
    public CheckedParameter[] getCheckedParameters(String method, ClassDef classDef ) {
        if( !checkedParameters.containsKey(method) ) {
            List<CheckedParameter> params = new ArrayList();
            //must loop
            Method m = classDef.findMethodByName( method );
            int colIndex = 0;
            for(Annotation[] annots: m.getParameterAnnotations()) {
                
                //check if there are annotations
                for(Annotation a: annots) {
                    if(a.annotationType() == Param.class) {
                        Param p = (Param)a;
                        params.add( new CheckedParameter(p.schema(),colIndex, p.required(), p.types()));
                    }
                }
                colIndex++;
            }
            checkedParameters.put( method, params.toArray(new CheckedParameter[]{}) );
        }
        return checkedParameters.get(method);
    }
    
    
    public void registerMethodsAccessed(String method) {
        if( methodsAccessed.add( method ) ) {
            timesAccessed.put( method, 1 );
        }
        else {
            Integer i = timesAccessed.get(method);
            timesAccessed.put(method, i+1 );
        }
    }
    
    public String toHtml() {
        String indent = "&nbsp;&nbsp;";
        StringBuffer sb = new StringBuffer();
        sb.append( "<b>" + this.name + "</b><br>");
        sb.append( "Est. Active Pool Size : " +  getEstimatedActivePoolSize() +"<br>" );
        sb.append( "Min Pool Size : " +  getMinPoolSize() + "<br>");
        
        sb.append( "Methods Accessed: <br>");
        if(methodsAccessed.size()==0) sb.append( "No methods Accessed<br>");
        for( String m: methodsAccessed ) {
            sb.append( indent + "<font color=blue>Method Name: </font>" + m + "<br>");
            sb.append( indent + "Estimated Times Accessed: " +  timesAccessed.get(m) + "<br>");
            sb.append( indent+indent+"<u>Before Interceptors</u><br>");
            List<String> bi = beforeInterceptors.get(m);
            if(bi.size()==0) sb.append( indent+indent+indent+"<i>No Before Interceptors defined</i><br>");
            for(String b: bi ) {
                sb.append( indent+indent+indent+ b + "<br>");
            }
            sb.append( indent+indent+"<u>After Interceptors</u><br>");
            List<String> ai = afterInterceptors.get(m);
            if(ai.size()==0) sb.append( indent+indent+indent+"<i>No After Interceptors defined </i><br>");
            for(String b: ai ) {
                sb.append( indent+indent+indent+ b + "<br>");
            }
        }
        return sb.toString();
    }

    public String getName() {
        return name;
    }
}
