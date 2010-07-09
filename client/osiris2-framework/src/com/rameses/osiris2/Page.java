/*
 * Page.java
 *
 * Created on February 17, 2009, 8:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import java.util.HashMap;
import java.util.Map;

public class Page  {
    
    private String name;
    private String id;
    private String caption;
    private String template;
    private Map properties = new HashMap();
    
    public Page() {
        
    }

    public Page(String name) {
        this.setName(name);
    }

    public String getTemplate() {
        if(template == null )
            return name;
        else
            return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Map getProperties() {
        return properties;
    }

    public void setProperties(Map properties) {
        this.properties = properties;
    }
    
    public String toXml() {
        return "<page name=\"" + name + "\" template=\"" + template + "\">";
    }
    
}
