/*
 * PermissionMultiResourceHandler.java
 *
 * Created on August 4, 2010, 5:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import com.rameses.eserver.MultiResourceHandler;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class PermissionMultiResourceHandler implements MultiResourceHandler {
    
    private Map permissions;
    private PermissionParser parser;
    private List errs = new ArrayList();
    
    /** Creates a new instance of PermissionMultiResourceHandler */
    public PermissionMultiResourceHandler() {
        permissions = new Hashtable();
        parser = new PermissionParser(permissions);
        errs.clear();
    }

    public void handle(InputStream is, String source) throws Exception {
        parser.load(is, source);
        for(Object o: parser.getParseErrors() ) {
            errs.add( o );
        }
    }

    public Map getPermissionSets() {
        return permissions;
    }
    
    public List getParseErrors() {
        return errs;
    }
    
    public void printStatus() {
        if(errs.size()>0) {
            System.out.println("Permission parsing encountered errors:");
            for( Object s : errs ) {
                System.out.println( s );
            }
        }
        else {
            System.out.println("PARSE PERMISSION SUCCESSFUL");
        }
    }
    
}
