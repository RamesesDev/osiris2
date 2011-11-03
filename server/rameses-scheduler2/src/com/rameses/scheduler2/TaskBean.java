/*
 * Task.java
 * Created on October 27, 2011, 8:55 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import com.rameses.util.DateUtil;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author jzamss
 */
public class TaskBean implements Serializable {
    public static String SERVICE_TYPE_EJB = "ejb";
    public static String SERVICE_TYPE_SCRIPT = "script";
    
    private String appcontext;
    private String method;
    private String service;
    private String servicetype = SERVICE_TYPE_SCRIPT;
    private Map parameters;
    private String apphost = "localhost:8080";
    
    private String id;
    private String interval;
    private Date currentdate;
    private Date startdate;
    private Date enddate;
    
    private Date nextdate;
    
    /** Creates a new instance of Task */
    public TaskBean(Map map) {
        appcontext=(String)map.get("appcontext");
        service =(String)map.get("service");
        method=(String)map.get("method");
        if(map.get("servicetype")!=null) {
            servicetype = (String)map.get("servicetype");
        }
        //private Object[] params=(String)map.get("params");
        if( map.get("apphost")!=null) {
            apphost =(String)map.get("apphost");
        }
        
        id=(String)map.get("id");
        interval=(String)map.get("interval");
        startdate = (Date)map.get("startdate");
        enddate=(Date)map.get("enddate");
        currentdate=(Date)map.get("currentdate");
        if(currentdate==null) currentdate = startdate;
        this.nextdate = DateUtil.add(this.currentdate, this.interval);
        
        if(map.get("parameters")!=null) {
            String props = (String)map.get("parameters");
            if(props!=null && props.trim().length()>0) {
                try {
                    Properties p = new Properties();
                    p.load( new ByteArrayInputStream(props.getBytes()) );
                    this.parameters = p;
                } catch(Exception ign){;}
            }
        }
        
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
    
    
    public Map getParameters() {
        return parameters;
    }
    
    public void setParameters(Map params) {
        this.parameters = params;
    }
    
    public String getApphost() {
        return apphost;
    }
    
    public void setApphost(String host) {
        this.apphost = host;
    }
    
    public String getService() {
        return service;
    }
    
    public String getServicetype() {
        return servicetype;
    }
    
    public void setService(String service) {
        this.service = service;
    }
    
    public void setServiceType(String serviceType) {
        this.servicetype = serviceType;
    }
    
    public String getAppcontext() {
        return appcontext;
    }
    
    public Map getConf() {
        Map map = new HashMap();
        map.put("app.context", this.appcontext);
        map.put("app.host", this.apphost);
        return map;
    }
    
    public void calculateNextExpiry() {
        this.currentdate = this.nextdate;
        this.nextdate = DateUtil.calculateNextDate(this.nextdate, this.interval);
    }
    
    public boolean isEnded() {
        if(this.enddate==null) return false;
        return (this.enddate.before(this.currentdate));
    }
    
    public boolean isExpired() {
        Date today = new Date();
        return  this.currentdate.before(today);
    }
    
    public String getInterval() {
        return interval;
    }
    
    public Date getCurrentdate() {
        return currentdate;
    }
    
    public Date getStartdate() {
        return startdate;
    }
    
    public Date getEnddate() {
        return enddate;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Map toMap() {
        Map m = new HashMap();
        m.put("taskid", this.id );
        m.put("id", this.id);
        m.put("service", this.service);
        m.put("servicetype", this.servicetype);
        m.put("method", this.method);
        m.put("startdate", this.startdate);
        m.put("enddate", this.enddate);
        m.put("currentdate", this.currentdate);
        m.put("nextdate", this.nextdate);
        m.put("interval", this.interval);
        m.put("parameters", this.parameters);
        m.put("apphost", this.apphost);
        m.put("appcontext", this.appcontext);
        return m;
    }
    
}

