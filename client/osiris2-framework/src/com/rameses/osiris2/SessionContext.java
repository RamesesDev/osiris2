/*
 * Application.java
 *
 * Created on February 21, 2009, 3:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * this is a package level class created only by
 * the AppContext
 */
public class SessionContext {
    
    private AppContext context;
    private Map env = new EnvMap();
    private SecurityProvider securityProvider;
    protected Map folderIndex = new Hashtable();
    
    //this holds a map of categorized invokers
    protected Map invokers = new Hashtable();
    
    protected SessionContext(AppContext ctx) {
        this.context = ctx;
        env = new EnvMap(ctx.getEnv());
    }
    
    public Map getEnv() {
        return env;
    }
    
    
    public Module getModule(String name) {
        Module c = context.getModule(name);
        if( c == null )
            throw new IllegalStateException( "Module not found : " + name);
        return c;
    }
    
    public WorkUnit getWorkUnit( String name ) {
        return context.getWorkUnit(name);
    }
    
    //the default permission is allow true. The exception is false.
    public boolean checkPermission(String workunitid, String name) {
        if( name == null ) return true;
        return securityProvider.checkPermission(workunitid + "." + name);
    }
    
    public boolean checkRoles(String name) {
        if( name  == null  ) return true;
        return securityProvider.checkRoles(name);
    }
    
    //combined permission and roles
    public boolean checkSecurity(String workunitid, String roles, String permission) {
        boolean checkRole = checkRoles(roles);
        boolean checkPermission = checkPermission(workunitid, permission);
        return checkRole && checkPermission;
    }
    
    //if there is no type specified set this as folder
    public List getInvokers() {
        return getInvokers(null);
    }
    
    public List getInvokers( String type ) {
        return getInvokers(type, true);
    }
    
    public List getInvokers( String type, boolean applySecurity ) {
        if(!invokers.containsKey(type)) {
            List list = new ArrayList();
            if(type == null) type = "folder";
            Iterator iter = context.getInvokers().iterator();
            while( iter.hasNext() ) {
                Invoker inv = (Invoker)iter.next();
                String itype = (inv.getType() == null) ? "folder" : inv.getType();
                if( itype.matches(type) ) {
                    if( applySecurity==false || checkSecurity( inv.getWorkunitid(), inv.getRoles(), inv.getPermission() ) ) {
                        list.add( inv );
                    }
                }
            }
            Collections.sort(list);
            invokers.put(type, list);
            return list;
        } else {
            return (List)invokers.get(type);
        }
    }
    
    public SecurityProvider getSecurityProvider() {
        return securityProvider;
    }
    
    public void setSecurityProvider(SecurityProvider securityProvider) {
        this.securityProvider = securityProvider;
    }
    
    //returns a list of folders including the invokers
    public List getFolders(String name) {
        if( !name.startsWith("/")) name = "/" + name;
        Folder folder = (Folder)context.getFolderManager().getFolders().get(name);
        if(folder==null)
            return null;
        else
            return getFolders(folder);
    }
    
    public List getFolders(Folder parent) {
        String fullId = parent.getFullId();
        List list = null;
        if( folderIndex.get( fullId )==null ) {
            list = new ArrayList();
            Iterator iter = parent.getFolders().iterator();
            while(iter.hasNext()) {
                list.add(iter.next());
            }
            List invokers = getInvokers("folder");
            iter = invokers.iterator();
            while(iter.hasNext()) {
                Invoker inv = (Invoker)iter.next();
                String fid = (String)inv.getProperties().get("folderid");
                if(fid!=null) {
                    if( !fid.startsWith("/")) fid = "/" + fid;
                    if( fid.equals(parent.getFullId())) {
                        if( inv.getName()==null ) {
                            inv.setName(fid);
                        }
                        String fname = inv.getName();
                        if( fname != null) {
                            Folder f = new Folder( fname, inv.getCaption(), parent, inv);
                            list.add(f);
                        }
                    }
                }
            }
            Collections.sort(list);
            folderIndex.put(fullId, list);
        } else {
            list = (List)folderIndex.get(fullId);
        }
        return list;
    }
    
    public ClassLoader getClassLoader() {
        return context.getClassLoader();
    }
    
}
