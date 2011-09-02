/*
 * TaskScheduler.java
 *
 * Created on October 19, 2010, 8:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;


import com.rameses.eserver.AppContext;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import com.rameses.util.MachineInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;

@Stateless
@Local(TaskSchedulerServiceLocal.class)
public class TaskSchedulerService implements TaskSchedulerServiceLocal{
    
    @Resource
    private SessionContext context;
    
    //transfer data from queue to interim that is expired. add machine id so we can remember it when firing
    public void scheduleTasks() {
        try {
            String host = AppContext.getHost();
            Map vars = new HashMap();
            vars.put("host", host );
            String machineid = MachineInfo.getInstance().getMacAddress();
            SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
            
            List<Map> entries = ctx.createNamedQuery("scheduler:list-queue").setVars(vars).getResultList();
            for(Map m: entries ) {
                int taskid = Integer.parseInt(m.get("taskid")+"");
                Date expirydate = (Date)m.get("expirydate");
                try {
                    ctx.openConnection();
                    SqlExecutor se = ctx.createNamedExecutor("scheduler:transfer-queue-to-interim");    
                    se.setParameter(1,taskid).setParameter(2,machineid).setParameter(3,expirydate).setParameter(4,host).execute();
                    ctx.createNamedExecutor("scheduler:remove-queue").setParameter(1,taskid).execute();
                }
                catch(Exception ex) {
                    System.out.println("Scheduler Queue Error. " + taskid + ":" + ex.getMessage() );
                }
                finally {
                    ctx.closeConnection();
                }
            }
        } 
        catch(Exception e) {
            System.out.println("Scheduler. Schedule Tasks error " + e.getMessage() );
        }        
    }

    public void processTasks() {
        SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
        List<Map> results = new ArrayList();
        try {
            ctx.openConnection();
            String machineid = MachineInfo.getInstance().getMacAddress();
            SqlQuery sq = ctx.createNamedQuery("scheduler:list-interim");
            List<Map> list = sq.setParameter(1,machineid).getResultList();
            if(list.size()==0) return;
            
            for(Map t : list) {
                int taskid = Integer.parseInt(t.get("taskid")+"");
                ctx.createNamedExecutor("scheduler:transfer-interim-to-processing").setParameter(1,taskid).execute();
                ctx.createNamedExecutor("scheduler:remove-interim").setParameter(1,taskid).execute();
                results.add(t);
            }
        } 
        catch(Exception e) {
            System.out.println("Scheduler.Process Task error " + e.getMessage() );
        }     
        finally {
            ctx.closeConnection();
        }
        
        //process async
        for(Map m : results) {
            processAsync(m);
        }
    }

    private void processAsync(Map map) {
        javax.jms.Connection conn = null;
        javax.jms.Session session = null;
        MessageProducer sender = null;
        try {
            conn = ((ConnectionFactory)context.lookup("java:/JmsXA")).createConnection();
            Destination dest = (Destination) context.lookup("queue/" + AppContext.getPath() + "TaskSchedulerQueue");
            
            session = conn.createSession(true, 0);
            sender = session.createProducer(dest);
            ObjectMessage ob = session.createObjectMessage();
            ob.setObject((Serializable) map);
            sender.send(ob);
        } 
        catch(Exception e1) {
            throw new IllegalStateException(e1);
        } 
        finally {
            try { sender.close(); } catch (JMSException ex) {}
            try { session.close(); } catch (JMSException ex) {}
            try { conn.close(); } catch (JMSException ex) { }
        }
        
    }

    public void updateTask(Map map, boolean increment) {
        //recalculate the next exirydate. based on the previous
        Date currentDate = (Date)map.get("currentdate");
        Date enddate = (Date)map.get("enddate");
        Date nextdate = (Date)map.get("nextdate");
        
        boolean reload = false;
        if(nextdate ==null ) {
            //do nothing. if duration is null this should fire only once 
        }
        else if(increment==false) {
            reload = true;
            nextdate = currentDate;
        }
        else if( enddate==null || enddate.after(nextdate) ) {
            reload = true;
        }
        SqlContext ctx = null;
        try {
            ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
            ctx.openConnection();
            int taskId = Integer.parseInt( map.get("taskid")+"");
            ctx.createNamedExecutor("scheduler:remove-processing").setParameter(1,taskId).execute();
            if(reload) {
                ctx.createNamedExecutor("scheduler:add-queue").setParameter(1,taskId).setParameter(2,nextdate).execute();
            }
        } 
        catch(Exception e) {
            e.printStackTrace();
        } finally {
            ctx.closeConnection();
        }     
    }

    
    
}
