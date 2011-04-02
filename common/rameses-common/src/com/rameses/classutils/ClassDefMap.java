/*
 * InterfaceBuilder.java
 *
 * Created on June 26, 2009, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.classutils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class ClassDefMap {
    
    public static Map toMap( Class clazz ) {
        Map map = new HashMap();
        map.put("name", clazz.getSimpleName() );
        map.put("package", clazz.getPackage().getName());
        map.put("fullname", clazz.getName());
        
        
        List<Map> methods = new ArrayList();
        for( Method m : clazz.getDeclaredMethods() ) {
            Map method = getMethodInfo( m );
            methods.add(method);
        }
        map.put("methods", methods);
        return map;
    }
    
    private static Map getMethodInfo(Method m) {
        Map map = new HashMap();
        map.put("returnType", m.getReturnType().getName() );
        map.put("name", m.getName() );
        List<String> params = new ArrayList();
        int i = 0;
        for( Class c: m.getParameterTypes() ) {
            params.add( c.getName() );
        }
        map.put("params", params);
        return map;
    }
    
    
}
