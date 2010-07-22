/*
 * TaskScheduler.java
 *
 * Created on December 27, 2009, 6:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.system.task;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@Local(TaskSchedulerLocal.class)
public class TaskScheduler implements TaskSchedulerLocal {
    
    @PersistenceContext(unitName="systemPU" )
    private EntityManager em;
    
    @Resource
    private SessionContext ctx;
    
    //@Resource(mappedName="TaskSchedulerMgmt")
    //private TaskSchedulerMgmtMBean taskMgmt;
    
    //to manually start the timer
    public void startTimer(long delay) {
        if(delay==0) delay = 5000;
        ctx.getTimerService().createTimer(delay,delay,"TASK_SCHEDULER");
    }
    
    public void cleanUp() {
        String ql = "DELETE FROM " + BusyTask.class.getName();
        Query q = em.createQuery( ql );
        q.executeUpdate();
    }
    
    
    @Timeout
    public void timeout(Timer t) {
        TaskSchedulerMgmtMBean taskMgmt = getTaskMgmt();
        
        //do not process if it is still processing so that there will be no overlapping tasks
        if( taskMgmt.isProcessing() ) return;
        taskMgmt.setProcessing(true);
        Map<Long, TaskThread> busy = taskMgmt.getBusyTasks();
        
        //check first if there are task threads that are completed
        List<TaskThread> forRemoval = new ArrayList<TaskThread>();
        synchronized(busy) {
            for(TaskThread tt : busy.values()) {
                if( tt.isCompleted() ) forRemoval.add(tt);
            }
        }
        //remove items in the forRemoval list
        //remove first in map, then update DB
        for(TaskThread tt: forRemoval) {
            synchronized(busy) {
                busy.remove( tt.getTaskId() );
            }
            removeBusy( tt.getTaskId() );
            if( tt.getErrors() != null ) {
                addError( tt.getTaskId(), tt.getErrors() );
            } else if( tt.getNextDate() !=null ) {
                updateTask(tt.getTaskId(), tt.getNextDate(), tt.getStatus());
            }
        }
        
        //EXECUTE TASKS HERE
        StringBuffer sb = new StringBuffer("SELECT o FROM TaskBean o WHERE o.nextdate <= ?1 ");
        sb.append( " AND (o.enddate is null OR o.nextdate <= o.enddate) " );
        sb.append( " AND NOT exists ( from BusyTask as st WHERE st.task = o ) " );
        sb.append( " AND NOT exists ( from SuspendedTask as st WHERE st.task = o ) " );
        sb.append( " AND NOT exists ( from ErrorTask as st WHERE st.task = o ) " );
        Query q = em.createQuery( sb.toString() );
        q.setParameter( 1, new Date() );
        
        List<TaskBean> tasks = q.getResultList();
        
        for( TaskBean tb : tasks ) {
            //check if task bean in busy map
            if( !busy.containsKey(tb.getId()) ) {
                String host = tb.getHost();
                if( host == null || host.trim().length() <= 0 ) {
                    host = System.getProperty("com.rameses.etl.defaulthost");
                    if( host == null ) System.out.println("ALERT: There is no default host provided");
                }
                
                if( host != null && host.trim().length() > 0 ) {
                    try {
                        String bindAddress = System.getProperty("bind.address");
                        if( bindAddress == null ) {
                            bindAddress = InetAddress.getLocalHost().getHostAddress();
                        }
                        
                        //if everything is ok start processing.
                        //create the Busy flag and add to busy map.
                        if(bindAddress.matches(tb.getHost() )) {
                            em.persist(new BusyTask(tb));
                            TaskThread tt = new TaskThread(tb);
                            synchronized(busy) {
                                busy.put( tt.getTaskId(), tt );
                            }
                            tt.start();
                        }
                    } catch(Exception ex) {
                        System.out.println("Error in launching task. " + ex.getMessage());
                    }
                }
            }
        }
        taskMgmt.setProcessing( false );
    }
    
    private void addError(Long id, String errs ) {
        //remove from busy
        TaskBean old = em.find( TaskBean.class, id );
        ErrorTask terr = new ErrorTask(old, errs );
        em.persist( terr );
    }
    
    private void removeBusy(Long id) {
        BusyTask old = em.find( BusyTask.class, id );
        em.remove(old);
    }
    
    private void updateTask(Long id, Date nextDate, String status) {
        TaskBean old = em.find( TaskBean.class, id );
        old.setNextdate(nextDate);
        old.setStatus( status );
        em.merge(old);
    }
    
    
    public TaskBean createTask(TaskBean taskbean) {
        //taskbean.setNextdate( taskbean.getStartdate() );
        em.persist(taskbean);
        return taskbean;
    }
    
    public void startTask(Long t) {
        TaskBean tb = em.find(TaskBean.class, t);
        tb.setNextdate( tb.getStartdate() );
        em.merge(tb);
    }
    
    public void suspend(Long t) {
        TaskSchedulerMgmtMBean taskMgmt = getTaskMgmt();
        TaskBean tb = em.find(TaskBean.class, t);
        em.persist(new SuspendedTask(tb));
        synchronized(taskMgmt.getBusyTasks()) {
            taskMgmt.getBusyTasks().remove( t );
        }
    }
    
    public void resume(Long t) {
        TaskSchedulerMgmtMBean taskMgmt = getTaskMgmt();
        SuspendedTask st = em.find(SuspendedTask.class, t);
        if( st != null ) em.remove(st);
        synchronized(taskMgmt.getBusyTasks()) {
            taskMgmt.getBusyTasks().remove( t );
        }
    }
    
    private TaskSchedulerMgmtMBean getTaskMgmt() {
        TaskSchedulerMgmtMBean taskMgmt = (TaskSchedulerMgmtMBean)ctx.lookup("TaskSchedulerMgmt");
        System.out.println("task management is " + taskMgmt);
        return taskMgmt;
    }
    
}
