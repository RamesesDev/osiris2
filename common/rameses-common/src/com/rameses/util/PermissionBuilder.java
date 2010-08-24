/*
 * PermissionBuilder.java
 *
 * Created on August 18, 2010, 9:48 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * adds permissions and returns the filtered permissions
 * this conforms to the permission standard as follows:
 *      modulename:workunitname.action.
 */
public class PermissionBuilder {
    
    private Map<String,List<Permission>> permissionMap = new Hashtable();
    
    public PermissionBuilder() {
    }
    
    /**
     * if there is already an existing permission set map, set it immediately
     */
    public void setPermissionMap(Map map) {
        this.permissionMap = map;
    }
    
    
    /**
     * utility for building permission objects
     * the source is a key=value pair collection which can be read by a property file.
     */
     public List<Permission> createPermissions(String moduleName, byte[] source ) {
         return createPermissions( moduleName, new ByteArrayInputStream(source) ); 
     }
    
    public List<Permission> createPermissions(String moduleName, InputStream is) {
        try {
            if(is==null)
                throw new RuntimeException( "createPermissions error. InputStream source is null");
            
            List<Permission> list = new ArrayList();
            InputStreamReader irdr = new InputStreamReader( is );
            BufferedReader br = new BufferedReader(irdr);
            String line = null;
            while( (line=br.readLine())!=null )  {
                if(line.trim().length()==0) continue;
                if( line.trim().startsWith("#")) continue;
                String arr[] = line.trim().split("=");
                String name = arr[0];
                String title = arr[1];
                
                String action = name;
                if( name.indexOf(":")<=0) action = moduleName + ":" + name;
                
                //action must match the correct prescribed pattern.
                if(!action.matches(".*:.*\\..*")) {
                    System.out.println("PermissionBuilder.error Wrong permission pattern: " + action ); 
                }
                
                Permission p = new Permission( action, title );
                list.add(p);
            }
            return list;
        } catch(Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
    public static class Permission {

        private String name;
        private String title;
        
        
        public Permission(String name,String title) {
            this.name = name;
            this.title = title;
        }
        
        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }
        
        
        public boolean equals(Object obj) {
            Permission p = (Permission)obj;
            return getName().equals( p.getName() );
        }
        
        public int hashCode() {
            return getName().hashCode();
        }
        
        public String toString() {
            return name + " [" + title + "]";
        }
    }
    
    public void loadPermissions(String permissionSet, List<Permission> permissions, List<String> excludes ) {
        if(!permissionMap.containsKey( permissionSet )) {
            permissionMap.put( permissionSet, new ArrayList() );
        }
        List list = permissionMap.get(permissionSet);
        list.addAll(permissions);
        
        List excludedList = createExcludeList( permissions, excludes );
        list.removeAll( excludedList );
    }
    
    private List createExcludeList(List<Permission> permissions, List<String> excludes) {
        List excludeList = new ArrayList();
        if(excludes == null) return excludeList;
        for(Permission p : permissions) {
            for(String e: excludes) {
                e = e.trim();
                if(p.getName().matches(e)) {
                    excludeList.add( p );
                    break;
                }
            }
        }
        return excludeList;
    }
    
    public List<Permission> getAllPermissions() {
        List<Permission> list = new ArrayList();
        for(List plist : permissionMap.values()) {
            list.addAll(plist);
        }
        return list;
    }
    
    public Map<String,List<Permission>> getPermissionSets() {
        return permissionMap;
    }
    
    public List<Permission> getFilteredPermissions( List<String> excludes ) {
        List perms = getAllPermissions();
        List excluded = createExcludeList( perms, excludes );
        perms.removeAll( excluded );
        return perms;
    }
    
    //returns a list of all allowed transactions
    public List<String> getAllowedActions( List<String> excludes ) {
        List<Permission> list = getFilteredPermissions( excludes );
        List<String> plist = new ArrayList();
        for(Permission p :list) {
            plist.add( p.getName() );
        }
        return plist;
    }
    
}
