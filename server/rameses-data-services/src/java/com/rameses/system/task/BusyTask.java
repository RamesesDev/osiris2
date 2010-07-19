/*
 * SuspendedTask.java
 *
 * Created on January 7, 2009, 8:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.system.task;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="sys_task_busy")
public class BusyTask implements Serializable {
    
    @Id
    @Column(name="id")
    private Long id;
    
    @OneToOne
    @JoinColumn(name="taskid")
    private TaskBean task;
    
    public BusyTask() {
    }
    
    public BusyTask(TaskBean task) {
        this.task = task;
        this.id = task.getId();
    }

    public TaskBean getTask() {
        return task;
    }

    public Long getId() {
        return id;
    }
    
}
