package com.rameses.templates;

import java.io.OutputStream;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.Stateless;

@Stateless
@Local(TemplateServiceLocal.class)
public class TemplateService implements TemplateServiceLocal {
    
    @Resource(mappedName="TemplateMgmt")
    private TemplateMgmtMBean templateMgmt;

    public Object transform(String fileName, Object data) throws Exception {
        Template t = templateMgmt.getTemplate(fileName);
        return t.transform(data, null);
    }

    public Object transform(String fileName, Object data, OutputStream out) throws Exception {
        Template t = templateMgmt.getTemplate(fileName);
        t.transform(data, out);
        return null;
    }
    
}
