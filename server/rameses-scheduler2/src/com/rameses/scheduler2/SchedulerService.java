/*
 * SchedulerService.java
 * Created on October 27, 2011, 8:55 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import com.rameses.server.cluster.ClusterServiceMBean;
import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.server.session.SessionServiceMBean;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import com.rameses.util.ExceptionManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 *
 * @author emn
 *
 * Pending - tasks that are not yet ready for execution. Once ready, meaning
 *          the expired time has elapsed, the thread is transferred to active
 * Active - active threads execute imeediately
 */
public class SchedulerService implements Serializable, SchedulerServiceMBean {
    
    private ScheduleManager scheduleManager;
    private DataSource datasource;
    private ClusterServiceMBean clusterService;
    private SessionServiceMBean sessionService;
    
    
    
    private String sessionId;
    
    public void start() throws Exception {
        
        String hostName = clusterService.getCurrentHostName();
        System.out.println("STARTING SCHEDULER 2 @" + hostName);
        datasource = AppContext.getSystemDs();
        SqlContext sqlc = SqlManager.getInstance().createContext(datasource);
        SqlExecutor sqe = sqlc.createNamedExecutor("scheduler:load-tasks");
        sqe.setParameter("host", hostName);
        sqe.execute();
        
        this.scheduleManager = new ScheduleManager(sessionService);
        this.scheduleManager.setDataSource( this.datasource );
        
        //load the initial tasks
        SqlQuery sqry = sqlc.createNamedQuery("scheduler:load-pending-tasks");
        sqry.setParameter("host", hostName);
        List<Map> list = sqry.getResultList();
        for(Map m : list ) {
            TaskBean tb = new TaskBean(m);
            if( m.get("suspended")!=null && (m.get("suspended")+"").equals("1") ) {
                this.scheduleManager.getSuspendedTasks().getForSuspension().add(tb.getId());
            }
            if(m.get("error")!=null && (m.get("error")+"").equals("1")) {
                this.scheduleManager.getErrorTasks().getTasks().put(tb.getId(), tb);
            }
            this.scheduleManager.getPendingTasks().push(tb);
        }
        
        //create a session with zero timeout
        this.sessionId = sessionService.register(ScheduleManager.SESSION_USERNAME,"task-scheduler",-1);
        
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, AppContext.getPath()+ SchedulerService.class.getSimpleName(), this );
        this.scheduleManager.start();
    }
    
    public void stop() throws Exception {
        SqlContext sqlc = SqlManager.getInstance().createContext(datasource);
        SqlExecutor sqe = sqlc.createNamedExecutor("scheduler:unload-tasks");
        sqe.setParameter("host", clusterService.getCurrentHostName());
        sqe.execute();
        this.scheduleManager.stop();
        this.sessionService.destroy(this.sessionId);
        this.clusterService = null;
        this.sessionService = null;
        this.sessionId = null;
        System.out.println("STOPPING SCHEDULER 2");
        InitialContext ictx = new InitialContext();
        JndiUtil.unbind( ictx, AppContext.getPath()+ SchedulerService.class.getSimpleName() );
    }
    
    public void setCluster(ClusterServiceMBean cluster) {
        this.clusterService = cluster;
    }
    
    public void setSession(SessionServiceMBean session) {
        this.sessionService = session;
    }
    
    public void suspend(String id) throws Exception {
        this.scheduleManager.getSuspendedTasks().addForSuspension(id);
    }
    
    public void resume(String id) throws Exception {
        this.scheduleManager.getSuspendedTasks().resume(id);
    }
    
    public void resumeError(String id) throws Exception {
        this.scheduleManager.getErrorTasks().resume(id);
    }
    
    public void restart(String id) throws Exception {
        try {
            SqlContext sqlc = SqlManager.getInstance().createContext(datasource);
            Map map = new HashMap();
            map.put("host", this.clusterService.getCurrentHostName());
            map.put("id", id);
            sqlc.createNamedExecutor("scheduler:load-single-task").setParameters(map).execute();
            Map m = (Map)sqlc.createNamedQuery("scheduler:get-single-task").setParameters(map).getSingleResult();
            if(m.size()>0) {
                TaskBean tb = new TaskBean(m);
                if( m.get("suspended")!=null && (m.get("suspended")+"").equals("1") ) {
                    this.scheduleManager.getSuspendedTasks().getSuspended().put(tb.getId(),tb);
                } else if(m.get("error")!=null && (m.get("error")+"").equals("1")) {
                    this.scheduleManager.getErrorTasks().getTasks().put(tb.getId(), tb);
                } else {
                    this.scheduleManager.getPendingTasks().push(tb);
                }
            } else {
                throw new Exception("No tasks found. Please check current date");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public String getSessionId() {
        return this.sessionId;
    }
    
    public void addTask(Map task) throws Exception {
        try {
            SqlContext sqlc = SqlManager.getInstance().createContext(datasource);
            SqlExecutor sqe = sqlc.createNamedExecutor("scheduler:add-task");
            sqe.setParameters(task).execute();

            String id = (String)task.get("id");
            String suspended = (String)task.get("suspended");
            if("true".equals(suspended)) {
                suspend(id);
            }
            restart(id);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            throw new Exception(ExceptionManager.getOriginal(ex));
        }
    }
    
    //before updating, make sure that task is suspended first
    public void updateTask(Map task)  throws Exception {
        SqlContext sqlc = SqlManager.getInstance().createContext(datasource);
        sqlc.createNamedExecutor("scheduler:update-task").setParameters(task).execute();

        try {
            Map map = new HashMap();
            map.put("host", this.clusterService.getCurrentHostName());
            map.put("id", task.get("id"));
            Map m = (Map)sqlc.createNamedQuery("scheduler:get-single-task").setParameters(map).getSingleResult();
            if(m.size()>0) {
                TaskBean tb = new TaskBean(m);
                if( m.get("suspended")!=null && (m.get("suspended")+"").equals("1") ) {
                    this.scheduleManager.getSuspendedTasks().getSuspended().put(tb.getId(),tb);
                } else if(m.get("error")!=null && (m.get("error")+"").equals("1")) {
                    this.scheduleManager.getErrorTasks().getTasks().put(tb.getId(), tb);
                } else {
                    this.scheduleManager.getPendingTasks().push(tb);
                }
            } else {
                throw new Exception("No tasks found. Please check current date");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    
    public void remove(String id) throws Exception {
        try {
            SqlContext sqlc = SqlManager.getInstance().createContext(datasource);
            sqlc.createNamedExecutor("scheduler:remove-active").setParameter(1, id).execute(); //removes active record
            sqlc.createNamedExecutor("scheduler:resume").setParameter(1, id).execute();        //removes suspended record
            sqlc.createNamedExecutor("scheduler:remove-task").setParameter(1, id).execute();   //removes task record

            this.scheduleManager.getSuspendedTasks().getSuspended().remove(id);
            this.scheduleManager.getErrorTasks().getTasks().remove(id);
        }
        catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
