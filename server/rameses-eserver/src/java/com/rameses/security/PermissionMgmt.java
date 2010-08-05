/*
 * PermissionMgmt.java
 *
 * Created on August 4, 2010, 2:51 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.security;

import com.rameses.eserver.CONSTANTS;
import com.rameses.eserver.JndiUtil;
import com.rameses.eserver.ResourceServiceMBean;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;

/**
 *
 * @author elmo
 */
public class PermissionMgmt implements PermissionMgmtMBean, Serializable {
    
    private String jndiName = "PermissionMgmt";
    private Map<String,PermissionSet> permissionSets;
    private ResourceServiceMBean resourceService;
    
    
    //role domains are functional areas.
    private Map<String, List> roleDomains;
    
    
    /** Creates a new instance of PermissionMgmt */
    public PermissionMgmt() {
    }
    
    public void start() throws Exception {
        System.out.println("STARTING PERMISSION MANAGEMENT ");
        InitialContext ctx = new InitialContext();
        JndiUtil.bind(ctx,jndiName,this);
        resourceService = (ResourceServiceMBean)ctx.lookup(CONSTANTS.RESOURCE_SERVICE);
        reload();
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING PERMISSION MANAGEMENT ");
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx,jndiName);
        permissionSets.clear();
        permissionSets = null;
    }
    
    public void reload() throws Exception {
        System.out.println("RELOADING PERMISSIONS");
        //reload the roledomain
        RoleDomainResourceHandler rh = new RoleDomainResourceHandler();
        resourceService.scanResources("permission://roledomains", rh);
        roleDomains = rh.getRoleDomains();
        PermissionMultiResourceHandler h = new PermissionMultiResourceHandler();
        resourceService.scanResources("permission://permissions", h);
        permissionSets = h.getPermissionSets();
        h.printStatus();
    }
    
    public void addPermissions(String permSet ) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream( permSet.getBytes() );
        PermissionParser p = new PermissionParser(permissionSets);
        p.load( bis, "addPermission method" );
        List errs = p.getParseErrors();
        if(errs.size()>0) {
            System.out.println("Permission parsing encountered errors:");
            for( Object s : errs ) {
                System.out.println( s );
            }
        }
    }
    
    
    /***
     * This should return a Map of representing permission sets.
     * each map contains a list of permission objects, also each
     * item is represented by a map.
     */
    public Map getPermissionSets(List<String> names, List<String> excludes) {
        Map map = new Hashtable();
        PermissionFinder finder = new PermissionFinder(excludes);
        for( String s: names  ) {
            PermissionSet ps = permissionSets.get(s);
            if(ps==null) {
                System.out.println("Permission Set " + s + " not found");
            } else {
                List permList = new ArrayList();
                fetchPermissions( ps, finder, permList);
                map.put( s, permList );
            }
        }
        return map;
    }
    
    public List<Map> getPermissionItems(List<String> names, List<String> excludes) {
        List<Map> list = new ArrayList();
        PermissionFinder finder = new PermissionFinder(excludes);
        for( String s: names  ) {
            PermissionSet ps = permissionSets.get(s);
            if(ps==null) {
                System.out.println("Permission Set " + s + " not found");
            } else {
                fetchPermissions( ps, finder, list );
            }
        }
        return list;
    }
    
    private void fetchPermissions(PermissionSet ps, PermissionFilter pgf, List list ) {
        for( Permission p: ps.getPermissions() ) {
            if( pgf.accept(p)) {
                Map mp = new Hashtable();
                mp.put( "action", p.getName() );
                mp.put("title", p.getTitle());
                if(p.getCategory()!=null) mp.put("category", p.getCategory());
                if(ps.getName()!=null) mp.put("permissionSet", ps.getName());
                list.add(mp);
            }
        }
    }
    
    public String getJndiName() {
        return jndiName;
    }
    
    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
    }

    public List getRoleDomainPermissionSets(String domainName) {
        List l = roleDomains.get(domainName);
        if(l==null)
            throw new IllegalStateException("Role domain " + domainName + " does not exist!");
        return l;
    }
    
    
    
    
    
    
}
