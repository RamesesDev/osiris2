/*
 * ControlEvent.java
 *
 * Created on November 5, 2010, 1:38 PM
 */

package com.rameses.rcp.framework;

import com.rameses.rcp.ui.UIControl;

/**
 *
 * @author jaycverg
 */
public class ControlEvent {
    
    public static final String RIGHT_CLICK = "RIGHT_CLICK";
    public static final String LEFT_CLICK = "LEFT_CLICK";
    
    private String source;
    private UIControl control;
    private Object context;
    private String eventName;
    private Object sourceEvent;
    
    
    public ControlEvent() {
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public UIControl getControl() {
        return control;
    }

    public void setControl(UIControl control) {
        this.control = control;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Object getSourceEvent() {
        return sourceEvent;
    }

    public void setSourceEvent(Object sourceEvent) {
        this.sourceEvent = sourceEvent;
    }
    
}
