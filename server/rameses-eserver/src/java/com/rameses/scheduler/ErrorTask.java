/*
 * TaskBeanError.java
 *
 * Created on January 4, 2009, 7:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.scheduler;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="sys_task_error")
public class ErrorTask implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="taskid")
    private TaskBean task;
    
    @Lob
    private String message;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date txndate;
    
    public ErrorTask() {
    }
    
    public ErrorTask(TaskBean t, String msg) {
        this.task = t;
        this.message = msg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TaskBean getTask() {
        return task;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    
}
