/*
 * RoleDomainParser.java
 *
 * Created on August 5, 2010, 11:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import com.rameses.eserver.MultiResourceHandler;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class RoleDomainResourceHandler implements MultiResourceHandler {
    
    private Map<String,List> domains = new Hashtable();
    
    public RoleDomainResourceHandler() {
    }

    public void handle(InputStream is, String resName) throws Exception {
        InputStreamReader ir = new InputStreamReader(is);
        BufferedReader rdr = new BufferedReader(ir);
        String line = null;
        while( (line=rdr.readLine())!=null ) {
            if(line.trim().length()==0) continue;
            if(line.trim().startsWith("#")) continue;
            if(!line.contains("=")) continue;
            String[] arr = line.split("=");
            String name = arr[0];
            String permSets = arr[1];
            List list = domains.get(name);
            if( list == null ) {
                list = new ArrayList();
                domains.put( name, list );
            }
            for(String s: permSets.split(",")) {
                s = s.trim();
                if(!list.contains(s)) list.add(s);
            }
        }
        
    }
    
    public Map<String,List> getRoleDomains() {
        return domains;
    }
}
