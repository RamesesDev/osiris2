/*
 * TemplateService.java
 *
 * Created on October 18, 2010, 4:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.util.TemplateProvider;
import com.rameses.util.TemplateProvider.DefaultTemplateProvider;
import com.rameses.util.TemplateSource;
import java.io.OutputStream;
import java.io.Serializable;
import javax.naming.InitialContext;

/**
 *
 * @author ms
 */
public class TemplateService implements TemplateServiceMBean,Serializable {
    
    public void start() throws Exception {
        System.out.println("      Initializing Template Manager");
        TemplateProvider.setInstance( new DefaultTemplateProvider() );
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, AppContext.getPath()+ TemplateService.class.getSimpleName(), this );
    }
    
    public void end() throws Exception {
        System.out.println("      Unloading Template Provider");
        TemplateProvider.setInstance( null );
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, AppContext.getPath()+ TemplateService.class.getSimpleName() );
    }
    
    public Object getResult(String templateName, Object data) {
        return TemplateProvider.getInstance().getResult( templateName, data );
    }
    
    public void transform(String templateName, Object data, OutputStream out) {
        TemplateProvider.getInstance().transform( templateName, data, out );
    }
    
    public void reload(String name) throws Exception {
        TemplateProvider.getInstance().clear(name);
    }

    public Object getResult(String templateName, Object data, TemplateSource tsource) {
        return TemplateProvider.getInstance().getResult( templateName, data, tsource );
    }

    public void transform(String templateName, Object data, OutputStream out, TemplateSource ts) {
         TemplateProvider.getInstance().transform( templateName, data, out, ts );
    }
    
    
    
}
