package com.rameses.scheduler;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="sys_task")
public class TaskBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    
    @Column(length=50, unique=true)
    private String name;
    
    @Lob
    private String description;
    
    @Column(length=50)        
    private String scriptname;        

    @Temporal(TemporalType.TIMESTAMP)
    private Date startdate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date enddate;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date nextdate;
    
    @Column(length=10)
    private String duration;
    
    //This is a text value to indicate feedback
    private String status;
    
    private String host;
    
    @Lob
    private String parameters;
    
    
    /** Creates a new instance of TaskBean */
    public TaskBean() {
    }
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Date getNextdate() {
        return nextdate;
    }

    public void setNextdate(Date nextdate) {
        this.nextdate = nextdate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String interval) {
        this.duration = interval;
    }

 
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getScriptname() {
        return scriptname;
    }

    public void setScriptname(String scriptname) {
        this.scriptname = scriptname;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    //</editor-fold>   

}
