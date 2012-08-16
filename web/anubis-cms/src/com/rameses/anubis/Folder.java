/*
 * PageFolder.java
 *
 * Created on July 1, 2012, 8:21 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class Folder {
    
    private Map meta;
    private List<NameParser> urlMappings = new ArrayList();
    private List<File> children = new ArrayList();
    
    public Folder(Map info) {
        this.meta = info;
    }
    
    public List<File> getChildren() {
        return children;
    }
    
    public Map getMeta() {
        return meta;
    }
    
    
    
}
