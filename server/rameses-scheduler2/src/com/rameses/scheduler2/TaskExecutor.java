/*
 * TaskRunnable.java
 * Created on October 27, 2011, 9:24 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import com.rameses.service.EJBServiceContext;
import com.rameses.service.ScriptServiceContext;
import com.rameses.service.ServiceProxy;
import com.rameses.util.ExceptionManager;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 *
 * @author jzamss
 */
public class TaskExecutor implements Runnable {
    
    private ScheduleManager manager;
    private TaskBean task;
    
    /** Creates a new instance of TaskRunnable */
    public TaskExecutor(ScheduleManager m, TaskBean task) {
        this.task = task;
        this.manager = m;
    }
    
    //after executing save the state in the db
    public void run() {
        try {
            String service = this.task.getService();
            String serviceType = this.task.getServicetype();
            String method = this.task.getMethod();
            ServiceProxy proxy = null;
            if(serviceType.equals(TaskBean.SERVICE_TYPE_EJB)) {
                EJBServiceContext svc = new EJBServiceContext(this.task.getConf());
                proxy = svc.create( service );
            } else {
                ScriptServiceContext svc = new ScriptServiceContext(this.task.getConf());
                proxy = svc.create(service);
            }
            Object result = proxy.invoke( method, new Object[]{this.task.toMap()} );
            
            boolean proceed = true;
            try {
                if(result!=null) {
                    proceed = Boolean.parseBoolean(result.toString());
                }
            } catch(Exception e){;}
            if(proceed) {
                this.task.calculateNextExpiry();
            }
            if( manager !=null ) {
                manager.getFinishedTasks().push( this.task );
                String status = "proceed";
                if( this.task.isEnded() ) {
                    status = "ended";
                }
                manager.notify(this.task.getId(), status,this.task.getCurrentdate());
            }
        } catch(Exception err) {
            err = ExceptionManager.getInstance().getOriginal(err);
            if( manager !=null ) {
                StringWriter sw = new StringWriter();
                err.printStackTrace(new PrintWriter(sw));
                try {
                    manager.getErrorTasks().addError(this.task,sw.toString() );
                   
                } catch(Exception ign){
                    ign.printStackTrace();
                }
            } else {
                err.printStackTrace();
            }
        }
    }
    
    
}
