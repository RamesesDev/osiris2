/*
 * XPageManager.java
 *
 * Created on June 30, 2012, 12:42 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.added;

import com.rameses.cms.AbstractFile;
import com.rameses.cms.Page;
import com.rameses.cms.Project;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class XPageManager extends XFileManager {
    
    public XPageManager(Project project) {
        super(project);
    }

    protected AbstractFile loadFile(String name) {
        Map map = new HashMap();
        map.put("title", name);
        map.put("id", name);
        return new Page(map);
    }
    
    public Page getPage(String name) {
        Page p = super.getFile( name );
        return p;
    }
    
    public static void main(String[] args) {
        XPageManager pm = new XPageManager(null);
        Page p1 = pm.getPage( "/about" );
        System.out.println("page is " + p1.getTitle());
        Page p2 = pm.getPage( "/about" );
        System.out.println("page is " + p2.getTitle());
        
    }
  
}
