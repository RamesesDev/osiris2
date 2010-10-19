/*
 * InvokerMeta.java
 *
 * Created on June 26, 2009, 7:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.annotations.Param;
import com.rameses.classutils.ClassDef;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Meta Data for Invoker
 */
public class ScriptObject implements Serializable {
    
    private int interceptorModifiedVersion;
    
    private String name;
    private ClassDef classDef;
    private String proxyIntfScript;
    private Class proxyIntfClass;
    
    //store the method name and
    private Map<String, List<String>> beforeInterceptors = new HashMap();
    private Map<String, List<String>> afterInterceptors = new HashMap();
    
    //new addition: parameters will be checked for validitiy.
    private Map<String, CheckedParameter[]> checkedParameters = new HashMap();
    
    public ScriptObject(Class cp, String name, String proxyInterface, Class proxyClass  ) {
        this.name = name;
        this.classDef = new ClassDef( cp );
        this.proxyIntfScript = proxyInterface;
        this.proxyIntfClass = proxyClass;
    }
    
    public Class getTargetClass() {
        return classDef.getSource();
    }
    
    public String getName() {
        return name;
    }
    
    public ClassDef getClassDef() {
        return classDef;
    }
    
    public String getProxyIntfScript() {
        return proxyIntfScript;
    }
    
    public Class getProxyIntfClass() {
        return proxyIntfClass;
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
    
    public CheckedParameter[] getCheckedParameters(String method) {
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
    
}
