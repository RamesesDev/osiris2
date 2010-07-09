package com.rameses.templates;

import com.rameses.interfaces.TemplateServiceLocal;
import groovy.lang.Writable;
import groovy.text.Template;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(TemplateServiceLocal.class)
public class TemplateService implements TemplateServiceLocal {
    
    @Resource(mappedName="TemplateMgmt")
    private TemplateMgmtMBean templateMgmt;
    
    public Object getTemplate(String name, Map data) {
        try {
            System.out.println("template mgmt is " + templateMgmt);
            Template template = (Template)templateMgmt.getTemplate(name);
            if( data == null ) data = new HashMap();
            Writable w =  template.make( data );
            return w.toString();
        } catch(Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
