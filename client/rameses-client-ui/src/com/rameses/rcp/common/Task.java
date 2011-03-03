package com.rameses.rcp.common;

import com.rameses.util.ValueUtil;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;

public abstract class Task {
    
    private String id = "TASK-" + new UID();
    
    private boolean cancelled;
    private boolean ended = true;
    private List<TaskListener> listeners = new ArrayList();
    
    protected boolean _started;
    
    public Task() {
    }

    public boolean equals(Object obj) {
        return obj != null && obj instanceof Task && ValueUtil.isEqual(id, ((Task)obj).id);
    }

    public int hashCode() {
        return Task.class.getName().hashCode() + id.hashCode();
    }
    
    public abstract boolean accept();
    public abstract void execute();
    
    public void start() {
        if ( !_started ) _started = true;
        
        for(TaskListener tl: listeners) {
            tl.onStart();
        }
    }
    public void end() {
        _started = false;
        
        for(TaskListener tl: listeners) {
            tl.onStop();
        }
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
        
        if ( cancelled ) {
            _started = false;
            for(TaskListener tl: listeners) {
                tl.onCancel();
            }
        }
    }
    
    public boolean isEnded() {
        return ended;
    }
    
    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
