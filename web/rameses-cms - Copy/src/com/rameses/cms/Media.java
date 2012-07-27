/*
 * Media.java
 *
 * Created on June 19, 2012, 11:40 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class Media extends AbstractFile {
    
    private Map info = new HashMap();
    
    /** Creates a new instance of Media */
    public Media(Map map) {
        info.putAll( map );
        this.setTitle((String)info.remove("title"));
        String sorder =(String) info.remove("sortorder");
        if(sorder!=null) {
            this.setSortorder( Integer.parseInt(sorder) );
        }
    }
    
    public Map getInfo() {
        return info;
    }
    
}
