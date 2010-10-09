/*
 * TemplateMgmtMBean.java
 *
 * Created on October 9, 2010, 7:47 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.templates;

import java.io.OutputStream;

/**
 *
 * @author ms
 */
public interface TemplateMgmtMBean {
    void start() throws Exception;
    void stop() throws Exception;
    Object getResult(String templateName, Object data);
    void transform(String template, Object data, OutputStream out);
    void flush(String name);
    void flushAll();
}
