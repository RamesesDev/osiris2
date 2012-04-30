/*
 * TemplateServiceLocal.java
 *
 * Created on October 18, 2010, 4:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.util.TemplateSource;
import java.io.OutputStream;

/**
 *
 * @author ms
 */
public interface TemplateServiceLocal {
    Object getResult(String templateName, Object data);
    void transform(String templateName, Object data, OutputStream out);
    Object getResult(String templateName, Object data, TemplateSource tsource);
    void transform(String templateName, Object data, OutputStream out, TemplateSource ts);
    
}
