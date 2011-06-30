/*
 * InterfaceBuilder.java
 *
 * Created on June 26, 2009, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.annotations.Async;
import com.rameses.annotations.ProxyMethod;
import com.rameses.common.AsyncHandler;
import java.lang.reflect.Method;

/**
 *
 * @author elmo
 */
public final class InterfaceBuilder {
    
    public static String getProxyInterfaceScript( String pkgName, Class sc ) {
        pkgName = pkgName.replaceAll("/", ".");
        boolean proxyMethodExist = false;
        StringBuffer sbody = new StringBuffer();
        for( Method m : sc.getDeclaredMethods() ) {
            if( m.isAnnotationPresent(ProxyMethod.class)) {
                proxyMethodExist = true;
                buildMethodText(m, sbody, m.getParameterTypes() );
                
                //if there is an async marker, add an extra method for the handler
                if( m.isAnnotationPresent(Async.class)) {
                    Class[] parms = new Class[m.getParameterTypes().length+1];
                    for(int j=0; j<parms.length-1;j++) {
                        parms[j] = m.getParameterTypes()[j];
                    }
                    parms[parms.length-1] = AsyncHandler.class;
                    buildMethodText(m, sbody, parms );
                }
            }
        }
        if(!proxyMethodExist) return null;
        StringBuffer nb = new StringBuffer();
        nb.append( "package " + pkgName + "; \n");
        nb.append( "interface " + sc.getSimpleName() + "Intf { \n" );
        nb.append(sbody);
        nb.append("}\n");
        return(nb.toString());
    }
    
    public static String createMethodSignature(Method m) {
        StringBuffer sb = new StringBuffer();
        sb.append(m.getReturnType().getName()+ " ");
        sb.append( m.getName() + "(" );
        int i = 0;
        for( Class c: m.getParameterTypes() ) {
            if( i!=0) sb.append(", ");
            sb.append(c.getCanonicalName());
            i++;
        }
        sb.append(") ; \n");
        return sb.toString();
    }
    
    
    private static void buildMethodText(Method m, StringBuffer sb, Class[] paramTypes ) {
        sb.append(m.getReturnType().getName()+ " ");
        sb.append( m.getName() + "(" );
        int i = 0;
        for( Class c: paramTypes ) {
            if( i!=0) sb.append(", ");
            sb.append(c.getCanonicalName());
            sb.append(" p" + i );
            i++;
        }
        sb.append(") ; \n");
    }
    
}
