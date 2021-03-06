/*
 * TaskSchedulerMDB.java
 *
 * Created on October 19, 2010, 8:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.scheduler;

import com.rameses.eserver.ScriptServiceDelegate;
import com.rameses.util.DateUtil;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author ms
 */
public abstract class AbstractTaskSchedulerMDB implements MessageListener {
    
    
    public void onMessage(Message message) {
        try {
            //we need to check first to ensure if we are really on the scheduled task.
            //check based on mac address and taskid.
            
            Map map = (Map)((ObjectMessage)message).getObject();
            if( map == null ) return;
            
            String script = (String)map.get("scriptname");
            String method = (String)map.get("method");
            
             //recalculate the next exirydate. based on the previous
            Date currentDate = (Date)map.get("expirydate");
            Date enddate = (Date)map.get("enddate");
            String duration = (String)map.get("duration");
            Date nextDate = null;
            if(duration!=null) nextDate = DateUtil.add(currentDate,duration);

            Map bean = new HashMap();
            bean.put("taskid", map.get("taskid") );
            bean.put("id", map.get("taskid") ); //to support old implementations
            bean.put( "startdate", map.get("startdate"));
            bean.put( "enddate", enddate);
            bean.put( "currentdate", currentDate );
            bean.put( "nextdate", nextDate );
            bean.put( "interval", duration );
            
            String props = (String)map.get("parameters");
            if(props!=null && props.trim().length()>0) {
                try {
                    Properties p = new Properties();
                    p.load( new ByteArrayInputStream(props.getBytes()) );
                    bean.put("parameters", p );
                } catch(Exception ign){;}
            }
            
            try {
                Object result = ScriptServiceDelegate.getScriptService().invoke(script,method,new Object[]{bean},bean);
                boolean incrementNextDate = true;
                try {
                    if(result!=null) incrementNextDate = Boolean.parseBoolean(result+"");
                } catch(Exception ign){;}
                TaskSchedulerDelegate.getTaskSchedulerService().updateTask(bean, incrementNextDate );
            }
            catch(Exception e) {
                System.out.println("error " + e.getMessage());
                TaskSchedulerDelegate.getTaskSchedulerService().updateTask(bean, false);
                return;
            }
        } 
        catch(Exception ex) {
            ex.printStackTrace();
        }
        
    }
    
}
