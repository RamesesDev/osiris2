/*
 * PathParser.java
 *
 * Created on May 17, 2010, 6:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rameses.osiris2.web.util;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author elmo
 */
public class PathParser implements Serializable {
    
    private String module;
    private String workunit;
    private String action;
    
    /** Creates a new instance of PathParser */
    public PathParser(String path) {
        parse(path);
    }
    
    public PathParser(HttpServletRequest req) {
        String path = req.getPathInfo();
        if ( path == null ) {
            path = req.getServletPath();
        }
        parse(path);
    }
    
    private void parse(String path) {
        if( path != null ) {
            path = WebUtil.removeHash(path);
            int idx = path.lastIndexOf(".");
            if ( idx >= 0 )
                path = path.substring(0, idx);
            
            String arr[] = path.split("/");
            if(arr.length>=2) module = arr[1];
            if(arr.length>=3) workunit = arr[2];
            if(arr.length>=4) action = arr[3];
        }
    }
    
    public String getModule() {
        return module;
    }
    
    public String getWorkunit() {
        return workunit;
    }
    
    public String getAction() {
        return action;
    }
    
    public String getWorkunitId() {
        if( module==null && workunit==null ) return null;
        return module + ":" + workunit;
    }
    
    public String getPermissionKey() {
        StringBuffer sb = new StringBuffer();
        sb.append(getWorkunitId());
        if ( getAction() != null ) {
            sb.append("." + getAction());
        }
        return sb.toString();
    }
}
