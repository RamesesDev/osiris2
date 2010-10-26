/*
 * InterceptorLoader.java
 *
 * Created on October 15, 2010, 11:09 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting;

import java.util.List;

/**
 *
 * @author ms
 */
public interface InterceptorLoader {
    
    void load();
    List<Interceptor> getBeforeInterceptors();
    List<Interceptor> getAfterInterceptors();
    
}
