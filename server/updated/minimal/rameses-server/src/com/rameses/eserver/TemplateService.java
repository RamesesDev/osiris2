/*
 * TemplateService.java
 *
 * Created on October 18, 2010, 4:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.util.TemplateProvider;
import java.io.OutputStream;
import javax.ejb.Local;
import javax.ejb.Stateless;

/**
 *
 * @author ms
 */
@Stateless
@Local(TemplateServiceLocal.class)
public class TemplateService implements TemplateServiceLocal {
    
    public Object getResult(String templateName, Object data) {
        return TemplateProvider.getInstance().getResult( templateName, data ); 
    }

    public void transform(String templateName, Object data, OutputStream out) {
        TemplateProvider.getInstance().transform( templateName, data, out );
    }
    
}
