/*
 * ThemeManager.java
 *
 * Created on June 12, 2012, 9:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.Map;

/**
 *
 * @author Elmo
 */
public class Theme {
    
    private String name;
    private String url;
    
    public Theme(Map map) {
        this.name = (String)map.remove( "name");
        this.url = (String)map.remove( "url");
    }
    
    public Theme(String name, String url) {
        this.name = name;
        this.url = url;
    }
    
    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
    
}
