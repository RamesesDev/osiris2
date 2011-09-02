/*
 * NotificationServer.java
 * Created on August 3, 2011, 7:52 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver;

import com.rameses.invoker.client.SimpleHttpClient;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author jzamss
 */
public class NotificationServer implements NotificationServerMBean,Serializable,Runnable {
    
    private Set<String> hosts = new HashSet();
    private HashMap<String,Session> sessions;
    
    private ScheduledExecutorService cleaner;
    private String port = "8080";
    private String protocol = "http";
    
    private SqlContext getSqlContext() {
        DataSource ds = AppContext.getSystemDs();
        return SqlManager.getInstance().createContext(ds);
    }
    
    public void start() throws Exception {
        System.out.println("STARTING NOTIFIER SERVER");
        sessions = new HashMap();
        String host = AppContext.getHost();
        SqlContext ctx = getSqlContext();
        ctx.createNamedExecutor("rms:add-cluster").setParameter(1, host).execute();
        //load all cluster hosts if any.
        List results = ctx.createNamedQuery("rms:list-clusters").setParameter(1, host).getResultList();
        for(Object o: results) {
            String rhost = (String)((Map)o).get("host");
            addHost(rhost);
        }
        InitialContext ictx = new InitialContext();
        JndiUtil.bind( ictx, AppContext.getPath()+ NotificationServer.class.getSimpleName(), this );
        Map map = new HashMap();
        map.put("host", host);
        broadcast("join", map);
        
        cleaner = Executors.newScheduledThreadPool(1);
        cleaner.scheduleWithFixedDelay(this,30,60,TimeUnit.SECONDS);
    }
    
    public void stop() throws Exception {
        System.out.println("SHUTTING DOWN NOTIFIER SERVER");
        cleaner.shutdown();
        sessions.clear();
        sessions = null;
        String host = AppContext.getHost();
        SqlContext ctx = getSqlContext();
        ctx.createNamedExecutor("rms:remove-cluster").setParameter(1, host).execute();
        Map map = new HashMap();
        map.put("host", host);
        broadcast("disjoin", map);
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath()+ NotificationServer.class.getSimpleName() );
        
    }
    
    public void addHost(String host) {
        System.out.println("adding host " +host);
        hosts.add(host);
    }
    
    public void removeHost(String host) {
        System.out.println("removing host " + host);
        hosts.remove(host);
    }
    
    //invokes action on other servers
    private void broadcast(String action, Map params) {
        String context =  AppContext.getName() ;
        params.put("action", action);
        List<Future> futures = new ArrayList();
        for(String h: hosts) {
            ExecutorService e = Executors.newCachedThreadPool();
            futures.add( e.submit( new Broadcaster(context,h,params)));
        }
        for(Future f :futures ) {
            try {
                f.get(2,TimeUnit.SECONDS);
            }
            catch(Exception e){
                System.out.println("broadcast error ->"+e.getMessage());
            }
        }
    }
    
    private class Broadcaster implements Runnable {
        private String host;
        private Map params; 
        private String context;
        public Broadcaster(String c,String h,Map p) {
            this.host = h;
            this.params = p;
            this.context = c;
        }
        public void run() {
            try {
                String h = protocol +"://"+this.host+":"+ port+"/"+context;
                SimpleHttpClient client = new SimpleHttpClient(h);
                client.post("notifier", params );
            } catch(Exception e) {
                System.out.println("error starting->"+e.getMessage());
            }
        }
    }

    public void signal(String sessionid, Object message) {
        signal(sessionid, message, true);
    }
    //called by implementors
    public void signal(String sessionid, Object message, boolean broadcast) {
        //System.out.println("signalling for " + sessionid );
        Session session = sessions.get(sessionid);
        
        //if session is not found in this server, broadcast to the other servers
        if(session==null) {
            System.out.println("no session found for " + sessionid);    
            if(broadcast) {
                Map map = new HashMap();
                map.put( "sessionid", sessionid );
                map.put( "message", message );
                broadcast( "send", map );
            }
        }
        else {
            //System.out.println("sending data " + message );
            session.push(message);
        }
    }
    
    //called by the front servlet
    public Object poll(String sessionid, String tokenid) {
        Session session = sessions.get(sessionid);
        if(session==null) {
            //throw new RuntimeException("Session "  + sessionid +  " not found on this server");
            return "-1";
        }
        return session.poll(tokenid);
    }
    
    

    public void run() {
        //check also if host is still alive.
        Iterator<String> iter = sessions.keySet().iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            Session s = sessions.get(key);
            if(s.canRemove()) sessions.remove(key);
        }
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    //returns a token id if successful
    public String register(String sessionId) {
        Session session = sessions.get(sessionId);
        if( session == null ) {
            session = new Session();
            sessions.put( sessionId, session );
        } 
        return session.createConnection();
    }

    public void unregister(String sessionId) {
        if(sessions.containsKey(sessionId)) {
            Session s = sessions.remove(sessionId);
            s.push("");
        }    
    }

    public String getHost() {
        return AppContext.getHost();
    }
    
    private static class Session implements Serializable {
        private Map<String,ArrayBlockingQueue> tokens = new HashMap();
        private Map<String,Date> expiryDates = new HashMap();
        private long timeout = 60000;

        public String createConnection() {
            String tokenid = ("TOKEN-" + (new UID()).hashCode() ); 
            tokens.put( tokenid, new ArrayBlockingQueue(50) );
            return tokenid;
        }
        
        public void unregister( String tokenid ) {
            tokens.remove(tokenid);
        }
        
        //when polling provide a token id
        public Object poll(String tokenid) {
            try {
                if(!tokens.containsKey(tokenid)) {
                    return "-1";
                }
                ArrayBlockingQueue q = tokens.get(tokenid);

                //set the next expiry date to check that if there is no response within this time, 
                //the token queue should be removed.
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.MINUTE, 2);
                expiryDates.put( tokenid, cal.getTime() );
                return q.poll( timeout, TimeUnit.MILLISECONDS );
            } 
            catch(Exception ex) {
                return null;
            }
        }

        public void push(Object message) {
            Iterator<String> iter = tokens.keySet().iterator();
            List<String> forRemoval = new ArrayList();
            while(iter.hasNext()){
                String k = iter.next();
                Date d = expiryDates.get(k);
                //for messages above 50, remove it.
                try {
                    tokens.get(k).add( message );
                }
                catch(Exception e) {
                    System.out.println("removing token " + k);
                    forRemoval.add(k);
                }
            }
            //remove items
            for(String s: forRemoval) {
                tokens.remove(s);
            }
        }

        public void destroy() {
            push("");
            tokens.clear();
            tokens = null;
        }

        public boolean canRemove() {
            return false;
        }
    
    }

    
}
