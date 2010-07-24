/*
 * TaskThread.java
 *
 * Created on June 13, 2009, 9:38 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scheduler;

import com.rameses.interfaces.ScriptServiceLocal;
import com.rameses.util.DateUtil;
import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;
import javax.naming.InitialContext;

public class TaskThread extends Thread {
    
    private Long taskid;
    private boolean completed;
    private TaskInfo task = new TaskInfo();
    private String errors;
    
    //this is null if there did not complete successfully
    private Date nextDate;
    private String status;
    
    public TaskThread(TaskBean taskBean) {
        taskid = taskBean.getId();
        Date nextDate = DateUtil.calculateNextDate( taskBean.getNextdate(), taskBean.getDuration() );
        task.setName( taskBean.getName() );
        task.setStartdate( taskBean.getStartdate() );
        task.setCurrentdate( taskBean.getNextdate() );
        task.setNextdate( nextDate );
        task.setEnddate( taskBean.getEnddate() );
        task.setInterval( taskBean.getDuration() );
        task.setScriptname( taskBean.getScriptname() );
        if( taskBean.getParameters() != null && taskBean.getParameters().trim().length() > 0 ) {
            Properties props = new Properties();
            try {
                props.load( new ByteArrayInputStream(taskBean.getParameters().getBytes()) );
                task.setParameters(props);
            } catch(Exception ign){;}
        }
        
    }
    
    public synchronized boolean isCompleted() {
        return completed;
    }
    
    public Long getTaskId() {
        return taskid;
    }
    
    public void setTaskId(Long id) {
        this.taskid = id;
    }
    
    public String getErrors() {
        return errors;
    }
    
    public void setErrors(String errors) {
        this.errors = errors;
    }
    
    public Date getNextDate() {
        return nextDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void run() {
        //init.
        completed = false;
        nextDate = null;
        try {
            String method = "timeout";
            String scriptName = task.getScriptname();
            if(scriptName.indexOf(".")>0) {
                String arr[] = scriptName.split("\\.");
                scriptName = arr[0];
                method = arr[1];
            }
            
            //lookup the script and inject the task
            InitialContext ctx = new InitialContext();
            ScriptServiceLocal scriptService = (ScriptServiceLocal)ctx.lookup("ScriptService/local");
            Object result = scriptService.invoke( scriptName, method, new Object[]{task}, null );
            boolean success = true;
            if(result!=null) {
                try {
                    success = Boolean.valueOf(result+"").booleanValue();
                }catch(Exception ign){;}
            }
            //if executing was successful, move to the next date.
            if(success) {
                this.nextDate = task.getNextdate();
                this.status = task.getStatus();
            }
        } catch(Exception ex) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter( sw );
                ex.printStackTrace( pw );
                String err = sw.getBuffer().toString() ;
                this.errors = err;
            } catch(Exception ignore) {
                //do nothing
            }
        } finally {
            completed = true;
        }
    }
    
    
    
}
