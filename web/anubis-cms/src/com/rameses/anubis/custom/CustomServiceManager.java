/*
 * CustomServiceManager.java
 *
 * Created on July 4, 2012, 2:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.AnubisContext;
import com.rameses.anubis.ContentUtil;
import com.rameses.anubis.Module;
import com.rameses.anubis.Project;
import com.rameses.anubis.ServiceAdapter;
import com.rameses.anubis.ServiceManager;
import com.rameses.anubis.service.HttpServiceHandler;
import com.rameses.anubis.service.ScriptServiceHandler;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class CustomServiceManager extends ServiceManager {
    
    /** Creates a new instance of CustomServiceManager */
    public CustomServiceManager(Project project) {
        super(project, null);
    }
    
    /**
     * This finds the the appropriate service adapters 
     * Check first if service is requested from a module. This is 
     * determined by the requesting page instance.
     * 
     */
    public Map getServiceAdapterInfo(String name) {
        if( AnubisContext.getCurrentContext()!=null) {
            Module mod = AnubisContext.getCurrentContext().getModule();
            if( mod != null ) {
                Map info = ContentUtil.getJsonMap( mod.getServiceAdapterSource( name ) );
                if(info!=null) return info;
            }
        }
        return ContentUtil.getJsonMap(project.getUrl(), "services", name);
    }
    
    public ServiceAdapter[] getServiceAdapters() {
        return new ServiceAdapter[] {
            new HttpServiceHandler(),
            new ScriptServiceHandler()
        };
    }
}
