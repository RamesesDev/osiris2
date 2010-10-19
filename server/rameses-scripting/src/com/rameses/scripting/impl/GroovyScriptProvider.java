/*
 * GroovyScriptProvider.java
 *
 * Created on October 16, 2010, 9:37 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting.impl;

import com.rameses.scripting.ScriptProvider;
import groovy.lang.GroovyClassLoader;
import java.io.InputStream;

/**
 *
 * @author ms
 */
public class GroovyScriptProvider implements ScriptProvider {

    private GroovyClassLoader classLoader;
    
    public GroovyScriptProvider() {
        classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
    }

    public Class parseClass(String code) {
        return classLoader.parseClass(code);
    }

    public Class parseClass(InputStream resource) {
        return classLoader.parseClass(resource);
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
    
    
}
