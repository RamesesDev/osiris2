/*
 * FormulaEditorModel.java
 *
 * Created on June 28, 2011, 7:27 PM
 */

package com.rameses.rcp.common;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jaycverg
 */
public class FormulaEditorModel {
    
    private Listener listener;
    private List<String> keywords;
    
    public FormulaEditorModel() {
    }
    
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    
    public void insert(String str) {
        if( listener != null ) listener.insert(str);
    }
    
    public void load() {
        if( listener != null ) listener.load();
    }
    
    public void refresh() {
        if( listener != null ) listener.refresh();
    }
    
    public static interface Listener {
        
        void insert(String str);
        void load();
        void refresh();
        
    }

    public List<String> getKeywords() {
        if( keywords != null ) return keywords;
        
        return (keywords = new ArrayList());
    }
    
}
