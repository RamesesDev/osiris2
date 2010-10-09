package com.rameses.eserver.templates;

import com.rameses.eserver.JndiUtil;
import com.rameses.util.TemplateProvider;
import java.io.OutputStream;
import java.io.Serializable;
import javax.naming.InitialContext;

public class TemplateMgmt implements TemplateMgmtMBean, Serializable {
    
    private TemplateProvider templateProvider;
    
    public void start() throws Exception {
        System.out.println("STARTING TEMPLATE MANAGEMENT");
        templateProvider = new TemplateProvider.DefaultTemplateProvider();
        InitialContext ctx = new InitialContext();
        JndiUtil.bind(ctx, TemplateMgmt.class.getSimpleName() , this);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING TEMPLATE MANAGEMENT");
        JndiUtil.unbind(new InitialContext(), TemplateMgmt.class.getSimpleName());
    }
    
    public Object getResult(String templateName, Object data) {
        return templateProvider.getResult( templateName, data );
    }
    
    public void transform(String templateName, Object data, OutputStream out) {
        templateProvider.transform(templateName, data, out);
    }
    
    public void flush(String name) {
        
    }

    public void flushAll() {
        templateProvider = new TemplateProvider.DefaultTemplateProvider();
    }
    
    
    
    
    
    
}
