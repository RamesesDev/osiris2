/*
 * TaskSchedulerThread.java
 *
 * Created on October 19, 2010, 8:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;

import com.rameses.eserver.AppContext;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.util.MachineInfo;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ms
 */
public class TaskSchedulerThread extends Thread {
    
    private boolean cancelled;
    private int delay = 2000;
    private int fetchSize = 3;
    private TaskManager taskManager;
    
    public TaskSchedulerThread(TaskManager tm) {
        this.taskManager = tm;
    }
    
    public int getDelay() {
        return delay;
    }
    
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public void cancel() {
        cancelled = true;
    }
    
    public void run() {
        while(!cancelled) {
            if(taskManager.isDebug())System.out.println("run scheduler " + new Date() );    
            try {
                String host = AppContext.getHost();
                Map vars = new HashMap();
                vars.put("host", host );
                String machineid = MachineInfo.getInstance().getMacAddress();
                SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
                List<Map> list = (ctx.createNamedQuery("scheduler:list-active-queue")).setVars(vars).getResultList();
                
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
                        Thread t = new Thread(new TaskProcessor(mtask));
                        t.start();
                    } catch(Exception ignore) {
                        if(taskManager.isDebug())System.out.println("ignore sked error " + ignore.getMessage());
                    }
                }
            } catch(Exception e) {
                System.out.println("Scheduler warning: " + e.getMessage() + " " + e.getClass());
            } finally {
                try {
                    sleep(delay);
                } catch (InterruptedException ign) {
                    break;
                }
            }
        }
    }
    
    
    
    
}
