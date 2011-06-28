/*
 * TaskProcessor.java
 * Created on June 25, 2011, 2:13 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver.scheduler;

import com.rameses.eserver.AppContext;
import com.rameses.eserver.ScriptServiceDelegate;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.util.DateUtil;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author jzamss
 */
public class TaskProcessor implements Runnable {
    
    private Map map;
    
    public TaskProcessor(Map map) {
        this.map = map;
    }
    
    public void run() {
        try {
            //we need to check first to ensure if we are really on the scheduled task.
            //check based on mac address and taskid.
            if( map == null ) return;
            int taskId = Integer.parseInt( map.get("taskid")+"");
            String script = (String)map.get("scriptname");
            String method = (String)map.get("method");
            
            //recalculate the next exirydate. based on the previous
            Date currentDate = (Date)map.get("nextdate");
            Date enddate = (Date)map.get("enddate");
            String duration = (String)map.get("duration");
            Date nextDate = null;
            if(duration!=null) nextDate = DateUtil.add(currentDate,duration);
            
            Map bean = new HashMap();
            bean.put("taskid", taskId );
            bean.put("id", taskId ); //to support old implementations
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
                
                boolean repeat = false;
                if(nextDate ==null ) {
                    //do nothing. if duration is null this should fire only once 
                }
                else if(incrementNextDate==false) {
                    repeat = true;
                    nextDate = currentDate;
                }
                else if( enddate==null || enddate.after(nextDate) ) {
                    repeat = true;
                }

                //update the task
                try {
                    SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
                    ctx.createNamedExecutor("scheduler:remove-processing").setParameter(1,taskId).execute();
                    if(repeat) {
                        ctx.createNamedExecutor("scheduler:update-next-date").setParameter(1,nextDate).setParameter(2,taskId).execute();
                        ctx.createNamedExecutor("scheduler:add-queue").setParameter(1,taskId).execute();
                    }
                    else {
                        ctx.createNamedExecutor("scheduler:update-next-date").setParameter(1,null).setParameter(2,taskId).execute();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } 
            catch(Exception e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                SqlContext ctx = SqlManager.getInstance().createContext(AppContext.getSystemDs());
                ctx.createNamedExecutor("scheduler:remove-processing").setParameter(1,taskId).execute();
                ctx.createNamedExecutor("scheduler:add-error").setParameter(1,taskId).setParameter(2,sw.toString()).execute();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
