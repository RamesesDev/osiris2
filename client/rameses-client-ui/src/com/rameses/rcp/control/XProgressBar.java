/*
 * XProgressBar.java
 *
 * Created on July 21, 2010, 5:24 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.rcp.common.ProgressListener;
import com.rameses.rcp.common.ProgressModel;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.NavigatablePanel;
import com.rameses.rcp.framework.NavigationHandler;
import com.rameses.rcp.ui.UIControl;
import com.rameses.rcp.util.UIControlUtil;
import com.rameses.util.MethodResolver;
import com.rameses.util.ValueUtil;
import javax.swing.JProgressBar;


public class XProgressBar extends JProgressBar implements UIControl, ProgressListener {
    
    private Binding binding;
    private String[] depends;
    private int index;
    private String onComplete;
    
    private ProgressModel model;
    
    
    public XProgressBar() {
    }
    
    public String[] getDepends() {
        return depends;
    }
    
    public void setDepends(String[] depends) {
        this.depends = depends;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setBinding(Binding binding) {
        this.binding = binding;
    }
    
    public Binding getBinding() {
        return binding;
    }
    
    public String getOnComplete() {
        return onComplete;
    }
    
    public void setOnComplete(String onComplete) {
        this.onComplete = onComplete;
    }
    
    public void refresh() {
    }
    
    public void load() {
        Object value = UIControlUtil.getBeanValue(this);
        if ( value instanceof ProgressModel ) {
            model = (ProgressModel) value;
            model.addListener(this);
        }
    }
    
    public int compareTo(Object o) {
        return UIControlUtil.compare(this, o);
    }
    
    public void onStart(int min, int max) {
        setMinimum(min);
        setMaximum(max);
        setStringPainted(true);
        binding.notifyDepends(this);
    }
    
    public void onProgress(int totalFetched, int maxSize) {
        setMaximum(maxSize);
        setValue(totalFetched);
        binding.notifyDepends(this);
    }
    
    public void onStop() {
        binding.notifyDepends(this);
        if( model.isCompleted() ) {
            fireAction();
        }
    }
    
    public void onSuspend() {
        binding.notifyDepends(this);
    }
    
    private void fireAction() {
        if ( ValueUtil.isEmpty(onComplete) ) return;
        
        try {
            ClientContext ctx = ClientContext.getCurrentContext();
            MethodResolver mr = ctx.getMethodResolver();
            Object outcome = mr.invoke(binding.getBean(), onComplete, null, null);
            
            NavigationHandler nh = ctx.getNavigationHandler();
            NavigatablePanel panel = UIControlUtil.getParentPanel(this, null);
            
            nh.navigate(panel, this, outcome);
            
        } catch(Exception e) {
            throw new IllegalStateException("XProgressBar::fireAction", e);
        }
    }
}
