/*
 * InterfaceBuilder.java
 *
 * Created on June 26, 2009, 2:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import com.rameses.annotations.ProxyMethod;
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
                sbody.append(m.getReturnType().getName()+ " ");
                sbody.append( m.getName() + "(" );
                int i = 0;
                for( Class c: m.getParameterTypes() ) {
                    if( i!=0) sbody.append(", ");
                    sbody.append(c.getCanonicalName());
                    sbody.append(" p" + i );
                    i++;
                }
                sbody.append(") ; \n");
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
    
    
}
