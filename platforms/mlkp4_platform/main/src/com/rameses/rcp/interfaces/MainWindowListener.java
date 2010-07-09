package com.rameses.rcp.interfaces;

public interface MainWindowListener 
{
    
    Object onEvent(String eventName, Object evt);
    
    boolean onClose();
    
}
