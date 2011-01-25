/*
 * FormPanelModel.java
 *
 * Created on January 25, 2011, 11:31 AM
 */

package com.rameses.rcp.common;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author jaycverg
 */
public class FormPanelModel {
    
    private Listener listener;
    
    public FormPanelModel() {
    }
    
    public Object getFormControls() { return null; }
    
    public void setProperty(String name, Object value) {
        if( listener != null ) {
            listener.onPropertyUpdated(name, value);
        }
    }
    
    public void setProperties(Map props) {
        if( listener != null ) {
            for(Map.Entry<String, Object> me : (Set<Map.Entry>) props.entrySet()) {
                listener.onPropertyUpdated(me.getKey()+"", me.getValue());
            }
        }
    }
    
    public String getHtmlFormat() {
        if( listener != null ) {
            return listener.getHtmlFormat();
        }
        
        return "";
    }
    
    public void reload() {
        if( listener != null ) {
            listener.onReload();
        }
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    public static interface Listener {
        
        void onPropertyUpdated(String name, Object value);
        String getHtmlFormat();
        void onReload();
        
    }
    
}
