/*
 * ClusterService.java
 * Created on September 18, 2011, 8:54 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.server.cluster;

import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.service.DefaultEJBServiceProxy;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.util.KeyGen;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author jzamss
 */
public class ClusterService implements ClusterServiceMBean{
    
    private String hostname;
    private String host;
    private String context;
    
    private String port = "8080";
    private String protocol = "http";
    private int hostNameLength = 6;
    private DataSource dataSource;
    
    private Map<String,RemoteHost> remoteHosts = new Hashtable(); 
    
    public void start() throws Exception {
        System.out.println("STARTING CLUSTER SERVICE");
        AppContext.load();
        remoteHosts.clear();
        InitialContext ictx = new InitialContext();
        JndiUtil.bind(ictx, AppContext.getPath()+ClusterService.class.getSimpleName(), this);
        
        host = AppContext.getHost();
        context = AppContext.getName();
        
        if(this.hostname==null) {
            hostname = KeyGen.generateAlphanumKey(null,hostNameLength);
        }
        
        if( port!=null && port.trim().length()>0) host = host + ":" + port;
        
        dataSource = AppContext.getSystemDs();
        
        SqlContext ctx = SqlManager.getInstance().createContext(dataSource);
        
        ctx.createNamedExecutor("cluster:add-host").setParameter(1, hostname).setParameter(2,context).setParameter(3,host).execute();
        //load all cluster hosts if any.
        List<Map> results = ctx.createNamedQuery("cluster:list-hosts").setParameter(1, hostname).getResultList();
        
        for(Map o: results) {
            RemoteHost r = new RemoteHost(o);
            remoteHosts.put(r.getName(),r);
        }
        
        Object[] args = new Object[]{this.hostname, this.context, this.host};
        Iterator<RemoteHost> iter = remoteHosts.values().iterator();
        ExecutorService svc = Executors.newCachedThreadPool();
        while(iter.hasNext()) {
            svc.submit( new RemoteHostNotifier( iter.next(), "addRemoteHost", args ));
        }
    }
    
    public void stop() throws Exception {
        Object[] args = new Object[]{this.hostname};
        ExecutorService svc = Executors.newCachedThreadPool();
        Iterator<RemoteHost> iter = remoteHosts.values().iterator();
        while(iter.hasNext()) {
            svc.submit( new RemoteHostNotifier( iter.next(), "removeRemoteHost",args ));
        }

        //remove all sessions associated with this host
        SqlContext ctx = SqlManager.getInstance().createContext(dataSource);
        ctx.createNamedExecutor("cluster:remove-host").setParameter(1, hostname).execute();
        remoteHosts.clear();
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind(ictx, AppContext.getPath()+ClusterService.class.getSimpleName());
        dataSource = null;
    }

    /***
     * This class notifies other servers of the presence of this server
     */
    private class RemoteHostNotifier implements Runnable {
        private RemoteHost rhost;
        private String action;
        private Object[] args;

        public RemoteHostNotifier(RemoteHost rh, String action, Object[] args) {
            this.rhost = rh;
            this.action = action;
            this.args = args;
        }
        public void run() {
            try {
                Map conf = new HashMap();
                conf.put("app.host", rhost.getHost());
                conf.put("app.context",rhost.getContext());
                DefaultEJBServiceProxy remote = new DefaultEJBServiceProxy("ClusterService",conf);
                remote.invoke(action, args);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    private class RemoteHost implements Serializable {
        private String name;
        private String context;
        private String host;
        
        public RemoteHost(Map map) {
            name = (String)map.get("name");
            context = (String)map.get("context");
            host = (String)map.get("host");
        }
        
        public RemoteHost(String name, String context, String host) {
            this.name = name;
            this.context = context;
            this.host = host;
        }
         
        public String getPath() {
            return protocol + "://" + host + "/" + context + "/";
        }

        public String getName() {
            return name;
        }

        public String getContext() {
            return context;
        }

        public String getHost() {
            return host;
        }
        
        public Map getMap() {
            Map map = new HashMap();
            map.put("name", name);
            map.put("app.context",context);
            map.put("app.host", host);
            map.put("path", getPath() );
            return map;
        }
    }
    
    
    public void addRemoteHost(String name, String context, String host) {
        remoteHosts.put(name,new RemoteHost(name, context,host));
    }

    public void removeRemoteHost(String name) {
        remoteHosts.remove(name);
    }

    public String getCurrentHostName() {
        return this.hostname;
    }

    public String getCurrentHost() {
        return host;
    }

    public String getCurrentContext() {
        return context;
    }

    public Map findHost(String name) {
        if(this.hostname.equals(name)) {
            Map map = new HashMap();
            map.put("name",this.hostname);
            map.put("app.context",this.context);
            String lhost = "localhost";
            if( port!=null && port.trim().length()>0) lhost = lhost + ":" + port;
            map.put("app.host",lhost);
            return map;
        }
        RemoteHost rh = remoteHosts.get(name);
        if(rh==null)
            throw new RuntimeException("remote host " + name + " not found");
        return rh.getMap();
    }

    public Map<String, Map> getRemoteHosts() {
        Map<String,Map> map = new HashMap();
        for(RemoteHost rh : remoteHosts.values()) {
            map.put(rh.getName(), rh.getMap());
        }
        return map;
    }

    
    public void setHostName(String h) {
        this.hostname = h;
    }
    
}
