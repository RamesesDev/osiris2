/*
 * TaskSchedulerMgmt.java
 * Created on June 25, 2011, 2:06 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver.scheduler;

import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import javax.naming.InitialContext;


public class TaskSchedulerMgmt implements TaskSchedulerMgmtMBean, Serializable {
    
    public void start() throws Exception {
        System.out.println("      Initializing TaskManager");
        TaskManager.getInstance().start();
        InitialContext ctx = new InitialContext();
        JndiUtil.bind( ctx, AppContext.getPath()+ TaskSchedulerMgmt.class.getSimpleName(), this );
    }
    
    public void stop() throws Exception {
        System.out.println("      Stopping TaskManager");
        TaskManager.getInstance().stop();
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind( ctx, AppContext.getPath()+ TaskSchedulerMgmt.class.getSimpleName());
    }

    public void setDebug(boolean debug) {
        TaskManager.getInstance().setDebug(debug);
    }

    public boolean isDebug() {
        return TaskManager.getInstance().isDebug();
    }

    public void suspendTask(int taskid) throws Exception {
        SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        ctx.createNamedExecutor("scheduler:suspend-task").setParameter(1,taskid).execute();
    }

    public void resumeTask(int taskid) throws Exception {
        SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        ctx.createNamedExecutor("scheduler:resume-task").setParameter(1,taskid).execute();
    }

    public void resumeFromError(int taskid) throws Exception {
        SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        ctx.createNamedExecutor("scheduler:remove-error").setParameter(1,taskid).execute();
        ctx.createNamedExecutor("scheduler:add-queue").setParameter(1,taskid).execute();
    }

    public void startTask(int taskid) throws Exception {
        SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        ctx.createNamedExecutor("scheduler:reset-next-date").setParameter(1,taskid).execute();
        ctx.createNamedExecutor("scheduler:add-queue").setParameter(1,taskid).execute();
    }

    public void cancelTask(int taskid) throws Exception {
        SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        ctx.createNamedExecutor("scheduler:update-next-date").setParameter(1,null).setParameter(2,taskid).execute();
        ctx.createNamedExecutor("scheduler:remove-queue").setParameter(1,taskid).execute();
        ctx.createNamedExecutor("scheduler:resume-task").setParameter(1,taskid).execute();
    }
    
    
}
