package com.rameses.scripting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class InterceptorManager {
    
    private int modifiedVersion;
    private List<Interceptor> beforeInterceptors;
    private List<Interceptor> afterInterceptors;
    private InterceptorLoader loader;
    
    public final void load() {
        loader.load();
        modifiedVersion++;
        beforeInterceptors = loader.getBeforeInterceptors();
        afterInterceptors = loader.getAfterInterceptors();
        Collections.sort(beforeInterceptors);
        Collections.sort(afterInterceptors);
    }
    
    public void destroy() {
        if( beforeInterceptors!=null ) beforeInterceptors.clear();
        if( afterInterceptors!=null ) afterInterceptors.clear();
    }
    
    //injects interceptors to the script object
    public void injectInterceptors( ScriptObject so, String method ) {
        //load before and after to a method
        //check first the interceptor modified version and compare if we need to reflush.
        boolean needsReload = (so.getInterceptorModifiedVersion() != modifiedVersion);
        so.setInterceptorModifiedVersion( modifiedVersion );
        
        Map _beforeInterceptors = so.getBeforeInterceptors();
        Map _afterInterceptors = so.getAfterInterceptors();
        String name =  so.getName() +"." +method;
        
        if(!_beforeInterceptors.containsKey(method) || needsReload ) {
            List<String> list = new ArrayList<String>();
            for(Interceptor idf: beforeInterceptors) {
                if(idf.accept(name)) {
                    list.add(idf.getSignature());
                }
            }
            _beforeInterceptors.put(method,list);
        }
        
        if(!_afterInterceptors.containsKey(method) || needsReload ) {
            List<String> list = new ArrayList<String>();
            for(Interceptor idf: afterInterceptors) {
                if(idf.accept(name)) {
                    list.add(idf.getSignature());
                }
            }
            _afterInterceptors.put(method,list);
        }
    }

    public InterceptorLoader getLoader() {
        return loader;
    }

    public void setLoader(InterceptorLoader loader) {
        this.loader = loader;
    }
    
}
