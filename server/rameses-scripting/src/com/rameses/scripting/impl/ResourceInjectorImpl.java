/*
 * ResourceInjectorImpl.java
 *
 * Created on October 15, 2010, 2:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scripting.impl;

import com.rameses.scripting.ResourceInjector;
import com.rameses.scripting.ScriptManager;
import com.rameses.util.ExprUtil;

import com.rameses.util.SysMap;
import java.lang.annotation.Annotation;

/**
 *
 * @author ms
 */
public class ResourceInjectorImpl extends ResourceInjector {
    
    /** Creates a new instance of ResourceInjectorImpl */
    public ResourceInjectorImpl() {
        super.addResourceHandler( new ResourceHandler() );
        super.addResourceHandler( new ServiceHandler() );
    }
    
    private static class ResourceHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.Resource.class;
        }
        public Object getResource(Annotation a) {
            String resname = ExprUtil.substituteValues(((com.rameses.annotations.Resource)a).value(), new SysMap());
            System.out.println("looking up " + resname );
            return resname;
            //InitialContext ctx = new InitialContext();
            //return ctx.lookup(resname);
        }
    } 

    private static class ServiceHandler implements ResourceInjector.Handler {
        public Class getAnnotationClass() {
            return com.rameses.annotations.Service.class;
        }

        public Object getResource(Annotation a) {
            //check first if the script info is already cached. if not yet then cache it
            com.rameses.annotations.Service asvc = (com.rameses.annotations.Service)a;
            SysMap sm = new SysMap();
            String scriptname =  ExprUtil.substituteValues(asvc.value(),sm);
            String host =  ExprUtil.substituteValues(asvc.host(),sm);
            
            ScriptManager scriptManager = ScriptManager.getInstance();
            /*
            if(host==null || host.trim().length()==0) {
                if(scriptname==null || scriptname.trim().length()==0) scriptname = callerService;
                return mbean.createLocalProxy(scriptname, env );
            }    
            else {
                if(scriptname==null || scriptname.trim().length()==0) 
                    throw new IllegalStateException("Please provide a remote service name value for @Service");
                return mbean.createRemoteProxy(scriptname, env, host );
            } 
             */    
            return null;
        }
        
    }
    
}
