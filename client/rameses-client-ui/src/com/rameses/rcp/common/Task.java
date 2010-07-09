package com.rameses.rcp.common;

public abstract class Task {
    
    private boolean cancelled;
    private boolean ended;
    
    public Task() {
    }

    public abstract boolean accept();
    public abstract void execute();

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }
}
