/*
 * ScheduleManager.java
 * Created on October 27, 2011, 10:17 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import com.rameses.server.session.SessionServiceMBean;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

/**
 *
 * @author jzamss
 */
public final class ScheduleManager implements Serializable {
    public static String SESSION_USERNAME = "taskscheduler";
    private PendingTaskProcessor pendingTasks = new PendingTaskProcessor(this);
    private ActiveTaskProcessor activeTasks = new ActiveTaskProcessor(this);
    private FinishedTaskProcessor finishedTasks = new FinishedTaskProcessor(this);
    private ErrorTaskList errorTasks = new ErrorTaskList(this);
    private SuspendedTaskList suspendedTasks = new SuspendedTaskList(this);
    private SessionServiceMBean session;
    
    private DataSource datasource;
    private ScheduledExecutorService mainThread;
    
    /** Creates a new instance of ScheduleManager */
    public ScheduleManager(SessionServiceMBean session) {
        this.session = session;
    }
    
    public PendingTaskProcessor getPendingTasks() {
        return pendingTasks;
    }
    
    public ActiveTaskProcessor getActiveTasks() {
        return activeTasks;
    }
    
    public FinishedTaskProcessor getFinishedTasks() {
        return finishedTasks;
    }
    
    public DataSource getDataSource() {
        return datasource;
    }
    
    public void start() throws Exception {
        this.mainThread = Executors.newSingleThreadScheduledExecutor();
        this.mainThread.scheduleWithFixedDelay(pendingTasks,1000,1000,TimeUnit.MILLISECONDS);
        this.mainThread.scheduleWithFixedDelay(activeTasks,1000,1000,TimeUnit.MILLISECONDS);
        this.mainThread.scheduleWithFixedDelay(finishedTasks,1000,1000,TimeUnit.MILLISECONDS);
    }
    
    public void stop() throws Exception {
        if( this.mainThread!=null ) {
            this.mainThread.shutdownNow();
            this.mainThread = null;
        }
    }
    
    public void setDataSource(DataSource ds) {
        this.datasource = ds;
    }
    
    public ErrorTaskList getErrorTasks() {
        return errorTasks;
    }
    
    public void addTask(Map map) throws Exception {
        TaskBean t = new TaskBean(map);
        SqlContext sqlContext = SqlManager.getInstance().createContext(this.datasource);
        SqlExecutor sqle = sqlContext.createNamedExecutor("scheduler:add-task");
        sqle.setParameters( map  ).execute();
        this.pendingTasks.push(t);
    }
    
    public SuspendedTaskList getSuspendedTasks() {
        return suspendedTasks;
    }
    
    public SessionServiceMBean getSessionService() {
        return session;
    }
    
    public void notify(String id, String status)  {
        notify(id, status, null);
    }
    
    public void notify(String id, String status, Date currentdate)  {
        try {
            Map map = new HashMap();
            map.put("id", id);
            map.put("status", status);
            if(currentdate!=null){
                SimpleDateFormat dformat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                map.put("currentdate", dformat.format(currentdate));
            }
            this.getSessionService().notifyUser( SESSION_USERNAME, map );
        } catch(Exception err) {
            System.out.println("Notify error->"+err.getMessage());
        }
    }
    
}
