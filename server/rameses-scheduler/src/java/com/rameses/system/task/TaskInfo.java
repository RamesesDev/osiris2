/*
 * AbstractTaskHandler.java
 *
 * Created on May 13, 2009, 10:49 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.system.task;

import java.util.Date;
import java.util.Map;

/**
 *
 * @author elmo
 */
public class TaskInfo {
    
    private String name;
    private Date startdate;
    private Date currentdate;
    private Date nextdate;
    private Date enddate;
    private String interval;
    private String status;
    private Map parameters;
    private String scriptname;
    
    public TaskInfo() {
    }
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Date getStartdate() {
        return startdate;
    }
    
    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }
    
    public Date getCurrentdate() {
        return currentdate;
    }
    
    public void setCurrentdate(Date currentdate) {
        this.currentdate = currentdate;
    }
    
    public Date getNextdate() {
        return nextdate;
    }
    
    public void setNextdate(Date nextdate) {
        this.nextdate = nextdate;
    }
    
    public Date getEnddate() {
        return enddate;
    }
    
    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
    
    public String getInterval() {
        return interval;
    }
    
    public void setInterval(String interval) {
        this.interval = interval;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname;
    }
    //</editor-fold>
    
}
