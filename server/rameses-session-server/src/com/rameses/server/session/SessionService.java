/*
 * NotificationServer.java
 * Created on August 3, 2011, 7:52 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.server.session;

import com.rameses.client.session.SessionConstant;
import com.rameses.server.cluster.ClusterServiceMBean;
import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.service.EJBServiceContext;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author jzamss
 */
public class SessionService implements SessionServiceMBean,Serializable,Runnable {
    
    private ClusterServiceMBean clusterService;
    
    private ConcurrentHashMap<String, Session> sessions;
    
    private String hostName;
    private ScheduledExecutorService cleaner;
    private int hostNameLength = 6;
    private DataSource dataSource;
    
    
    public void start() throws Exception {
        hostName = clusterService.getCurrentHostName();
        System.out.println("STARTING SESSION SERVER @" + hostName);
        sessions = new ConcurrentHashMap();
        dataSource = AppContext.getSystemDs();
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath()+ SessionService.class.getSimpleName(), this );
        cleaner = Executors.newScheduledThreadPool(1);
        cleaner.scheduleWithFixedDelay(this,30,60,TimeUnit.SECONDS);
    }
    
    public void stop() throws Exception {
        System.out.println("STOPPING SESSION SERVER");
        
        //remove all sessions associated with this host. make this a priority
        SqlContext ctx = SqlManager.getInstance().createContext(dataSource);
        ctx.createNamedExecutor("session:remove-host-sessions").setParameter(1, hostName).execute();
        
        //clear out all sessiosns connecting by sending messages to each
        Iterator iter = sessions.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            Session s = sessions.remove(key);
            s.push(SessionConstant.SESSION_ABORTED);
        }
        
        
        
        cleaner.shutdown();
        
        sessions.clear();
        sessions = null;
        
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath()+ SessionService.class.getSimpleName() );
        dataSource = null;
    }
    
    private Map findRemoteConf( String sessionid ) {
        if(sessionid==null)
            throw new RuntimeException("SessionServer error. Session id must not be null.");
        String _host = sessionid.substring(0, sessionid.indexOf(":"));
        //if not current host
        if(!_host.equals(this.hostName)) {
            try {
                Map redirectInfo = clusterService.findHost(_host);
                redirectInfo.put(SessionConstant.SESSION_REDIRECT,true);
                return redirectInfo;
            } catch(Exception e){;}
        }
        return null;
    }
    
    /***
     * poll. check first if the session can be found within this host.
     * if not, return a redirect message with info of where to redirect the session.
     * Redirect info is a map with a redirect message.
     */
    public Object poll(String sessionid, String tokenid) {
        Map conf = findRemoteConf(sessionid);
        if(conf!=null) return conf;
        Session session = sessions.get(sessionid);
        if(session==null) {
            return SessionConstant.SESSION_DESTROYED;
        }
        return session.poll(tokenid);
    }
    
    public boolean push(final String sessionid, final String tokenid, final Object msg) {
        Map conf = findRemoteConf(sessionid);
        if( conf !=null && conf.containsKey(SessionConstant.SESSION_REDIRECT)) {
            final Map xconf = conf;
            ExecutorService svc = Executors.newCachedThreadPool();
            svc.submit( new Runnable(){
                public void run() {
                    EJBServiceContext ctx = new EJBServiceContext(xconf);
                    SessionServiceMBean mb = ctx.create("SessionService", SessionServiceMBean.class);
                    mb.push(sessionid, tokenid,msg);
                }
            });
            return true;
        } else {
            Session session = sessions.get(sessionid);
            if(session==null) {
                return false;
            }
            session.push( msg );
            return true;
        }
    }
    
    public void run() {
        //check also if host is still alive.
        Iterator<String> iter = sessions.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            Session s = sessions.get(key);
            if(s.canRemove()) {
                System.out.println("session expired " + key);
                this.removeSession( key, SessionConstant.SESSION_EXPIRED );
            } else {
                s.cleanUnusedSessions();
            }
        }
    }
    
    
    //returns a token id if successful
    public String register(String username, Object info) {
        try {
            String sessionid = hostName + ":" + new UID();
            Map map = new HashMap();
            map.put("sessionid", sessionid );
            map.put("username", username);
            map.put("info", info);
            map.put("dtaccessed", new java.util.Date());
            map.put("host", hostName );
            map.put("dtexpiry", null);
            SqlContext ctx = SqlManager.getInstance().createContext(dataSource);
            ctx.createNamedExecutor("session:add-session").setParameters(map).execute();
            Session session = new Session(sessionid, username, info);
            sessions.put( sessionid, session );
            return sessionid;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public Object destroy(String sessionId) {
        final Map conf = findRemoteConf(sessionId);
        if( conf != null ) {
            //this means session is not located in this server.
            EJBServiceContext ctx = new EJBServiceContext(conf);
            SessionServiceMBean mb = ctx.create("SessionService", SessionServiceMBean.class);
            return mb.destroy(sessionId);
        } else {
            return removeSession(sessionId, SessionConstant.SESSION_ENDED);
        }
    }
    
    private Object removeSession(String sessionId, String msg) {
        if(sessions.containsKey(sessionId)) {
            Session s = sessions.remove(sessionId);
            s.push(msg);
            SqlContext ctx = SqlManager.getInstance().createContext(dataSource);
            Map map = new HashMap();
            map.put("sessionid", sessionId);
            map.put("username",s.getUsername());
            map.put("info",s.getInfo());
            try {
                ctx.createNamedExecutor("session:remove-session").setParameters(map).execute();
                return map;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    
    public Object getInfo(String sessionid) {
        final Map conf = findRemoteConf(sessionid);
        if( conf != null ) {
            //this means session is not located in this server.
            EJBServiceContext ctx = new EJBServiceContext(conf);
            SessionServiceMBean mb = ctx.create("SessionService", SessionServiceMBean.class);
            return mb.getInfo(sessionid);
        } else {
            Session s = sessions.get(sessionid);
            if(s==null) return null;
            return s.getInfo();
        }
    }
    
    public void setCluster(ClusterServiceMBean cluster) {
        this.clusterService = cluster;
    }
    
    
    
}
