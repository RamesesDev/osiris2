/*
 * AnubisContext.java
 *
 * Created on July 15, 2012, 10:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

/**
 *
 * @author Elmo
 */
public abstract class AnubisContext {
    
   
    protected static final ThreadLocal<AnubisContext> threadLocal = new ThreadLocal();
    
    public static void setContext(AnubisContext ctx) {
        threadLocal.set(ctx);
    }
    
    public static void removeContext() {
        threadLocal.remove();
    }
    
    public static AnubisContext getCurrentContext() {
        return threadLocal.get();
    }
    
    public abstract Project getProject();
    public abstract Module getModule();
    public abstract SessionContext getSession();
    public abstract String getLang();
    
    public LocaleSupport getCurrentLocale() {
        if( getLang()==null) return null;
        return getProject().getLocaleSupport( getLang() );
    }
    
    
}
