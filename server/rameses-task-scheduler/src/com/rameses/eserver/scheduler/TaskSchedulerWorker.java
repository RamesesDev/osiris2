/*
 * TaskSchedulerThread.java
 *
 * Created on October 19, 2010, 8:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;


import com.rameses.server.common.AppContext;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import com.rameses.util.MachineInfo;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author ms
 */
public class TaskSchedulerWorker implements Runnable {
    
    private TaskManager taskManager;
    
    public TaskSchedulerWorker(TaskManager tm) {
        this.taskManager = tm;
    }
    
    public void run() {
        if(taskManager.isDebug())System.out.println("scheduler polled @ " + new Date() );    
        try {
            String host = AppContext.getHost();
            Map vars = new HashMap();
            vars.put("host", host );
            String machineid = MachineInfo.getInstance().getMacAddress();
            SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
            SqlQuery qry = ctx.createNamedQuery("scheduler:list-active-queue");
            List<Map> list = qry.setVars(vars).getResultList();
            Executor executor = Executors.newCachedThreadPool();
            
            //loop each task and mark in processings
            for(Map mtask : list) {
                int taskid = Integer.parseInt(mtask.get("taskid")+"");
                Date expirydate = (Date)mtask.get("nextdate");
                try {
                    //attempt to insert first to processing. if it fails, resume to next task
                    //if it succeeds, remove queue then launch the task.
                    SqlExecutor se = ctx.createNamedExecutor("scheduler:add-to-processing");
                    se.setParameter(1,taskid).setParameter(2,machineid).setParameter(3,host).execute();
                    ctx.createNamedExecutor("scheduler:remove-queue").setParameter(1,taskid).execute();
                    executor.execute(new TaskProcessor(mtask));
                } 
                catch(Exception ignore) {
                    if(taskManager.isDebug())System.out.println("ignore sked error " + ignore.getMessage());
                }
            }
        } 
        catch(Exception e) {
            System.out.println("Scheduler warning: " + e.getMessage() + " " + e.getClass());
        } 
    }
    
    
}
