/*
 * TemplateServiceMBean.java
 *
 * Created on August 8, 2011, 9:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import java.io.OutputStream;

/**
 *
 * @author jzamss
 */
public interface TemplateServiceMBean {
    void start() throws Exception;
    void end() throws Exception;
    void reload(String name) throws Exception;
    Object getResult(String templateName, Object data);
    void transform(String templateName, Object data, OutputStream out);
}
