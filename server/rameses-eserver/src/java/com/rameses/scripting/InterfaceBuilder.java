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
        StringBuffer sb = new StringBuffer();
        sb.append( "package " + pkgName + "; \n");
        sb.append( "interface " + sc.getSimpleName() + "Intf { \n" );
        for( Method m : sc.getDeclaredMethods() ) {
            if( m.isAnnotationPresent(ProxyMethod.class)) {
                sb.append(m.getReturnType().getName()+ " ");
                sb.append( m.getName() + "(" );
                int i = 0;
                for( Class c: m.getParameterTypes() ) {
                    if( i!=0) sb.append(", ");
                    sb.append(c.getCanonicalName());
                    sb.append(" p" + i );
                    i++;
                }
                sb.append(") ; \n");
            }
        }
        sb.append("}\n");
        return(sb.toString());
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
