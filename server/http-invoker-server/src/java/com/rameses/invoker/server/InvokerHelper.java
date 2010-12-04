/*
 * InvokerHelper.java
 *
 * Created on December 3, 2010, 8:44 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.invoker.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ms
 */
public final class InvokerHelper {
    
    /***
     * this will locate all methods that matches the name, with the same arg count.
     */
    public static Method[] getMethodByName(Object bean, String name, int argCount) throws Exception {
        List list = new ArrayList();
        Method[] methods = bean.getClass().getMethods();
        for (int i=0; i<methods.length; i++) {
            if (!methods[i].getName().equals(name)) continue;
            if (methods[i].getParameterTypes().length != argCount) continue;
            list.add(methods[i]);
        }
        return (Method[]) list.toArray(new Method[]{});
    }
}
