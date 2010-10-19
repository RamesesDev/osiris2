/*
 * TaskScheduler.java
 *
 * Created on October 19, 2010, 8:06 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;


import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import com.rameses.util.DateUtil;
import com.rameses.util.MachineInfo;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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
    
    @TransactionAttribute( TransactionAttributeType.REQUIRED )
    public void fireActiveQueue(int fetchSize) {
        SqlContext ctx = null;
        try {
            ctx = SqlManager.getInstance().createContext(AppContext.getDefaultDs());
            ctx.openConnection();
            Map dt = (Map)ctx.createNamedQuery("scheduler:serverdate").getSingleResult();
            Date d = (Date)dt.get("serverdate");
            List<Map> list = ctx.createNamedQuery("scheduler:list-queue").setParameter(1,d).setMaxResults(fetchSize).getResultList();
            if(list.size()==0) {
                return;
            }
            SqlExecutor transferer = ctx.createNamedExecutor("scheduler:add-processing");
            SqlExecutor remover = ctx.createNamedExecutor("scheduler:remove-queue");
            for(Map t : list) {
                int taskid = Integer.parseInt( t.get("taskid")+"" );
                Date expirydt = (Date)t.get("expirydate");
                transferer.setParameter( 1, taskid );
                transferer.setParameter( 2, MachineInfo.getInstance().getMacAddress() );
                transferer.setParameter( 3, expirydt );
                transferer.addBatch();
                remover.setParameter( 1, taskid );
                remover.addBatch();
            }
            transferer.execute();
            remover.execute();
            for(Map map: list) {
                processAsync( map );
            }
        } 
        catch(Exception e) {
            System.out.println("ERROR SCHEDULER " );
            e.printStackTrace();
        } finally {
            ctx.closeConnection();
        }        
    }

    private void processAsync(Map map) {
        javax.jms.Connection conn = null;
        javax.jms.Session session = null;
        MessageProducer sender = null;
        try {
            conn = ((ConnectionFactory)context.lookup("java:/JmsXA")).createConnection();
            Destination dest = (Destination) context.lookup("queue/TaskSchedulerQueue");
            
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
            ctx = SqlManager.getInstance().createContext(AppContext.getDefaultDs());
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
