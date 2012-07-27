/*
 * ResourceTemplate.java
 *
 * Created on June 18, 2012, 8:30 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import groovy.text.Template;
import java.util.Map;

/**
 *
 * @author Elmo
 * this wraps the Template class from groovy.
 * This is done just in case we change our minds in the implementation
 *
 */
public class ContentTemplate {
    
    private String name;
    private Template template;
    
    /** Creates a new instance of ResourceTemplate */
    public ContentTemplate(String name, Template t) {
        this.template = t;
        this.name = name;
    }
    
    public String render(Map map) {
        try {
            return template.make(map).toString();
        } catch(RuntimeException e) {
            e.printStackTrace();
            return "<font color=red>"+e.getMessage()+" for source " + name + "</font>";
        }
    }
    
}
