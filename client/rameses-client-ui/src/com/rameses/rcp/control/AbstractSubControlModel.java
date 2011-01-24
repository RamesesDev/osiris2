/*
 * AbstractSubControlModel.java
 *
 * Created on January 22, 2011, 2:10 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.common.PropertyResolver;
import com.rameses.rcp.framework.Binding;
import com.rameses.rcp.framework.ClientContext;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractSubControlModel {
    
    private static final long serialVersionUID = 1L;
    
    private Binding controlBinding;
    private String beanName;
    private String fieldName;
    
    private Map properties = new HashMap();
    
    
    public AbstractSubControlModel() {
    }
    
    
    //overridable methods
    public void onInit() {}
    public void onRefresh() {}
    
    /**
     * this is called to initialize/create the context object
     * when it is null especially if the context is a complex object
     */
    public Object createContext() { return null; }
    
    //package level
    final void init(Binding controlBinding, String name) {
        this.controlBinding = controlBinding;
        
        if( name == null) return;
        if( name.contains(".") ) {
            beanName = name.substring(0, name.lastIndexOf("."));
            fieldName = name.substring(name.lastIndexOf(".")+1);
        } else {
            beanName = name;
        }
    }
    
    
    //final accessors
    public final Binding getControlBinding() {
        return controlBinding;
    }
    
    public final String getBeanName() {
        return beanName;
    }
    
    public final String getFieldName() {
        return fieldName;
    }
    
    public final Object getContext() {
        if( beanName == null ) return null;
        
        PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
        Object context = res.getProperty(controlBinding.getBean(), beanName);
        if( fieldName != null )
            context = res.getProperty(context, fieldName);
        
        if( context == null ) {
            context = createContext();
            if( context != null ) setContext(context);
        }
        
        return context;
    }
    
    public final void setContext(Object value) {
        if( beanName == null ) return;
        
        PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
        if( fieldName != null ) {
            Object bean = res.getProperty(controlBinding.getBean(), beanName);
            res.setProperty(bean, fieldName, value);
        } else {
            res.setProperty(controlBinding.getBean(), beanName, value);
        }
        
    }
    
    public final Map getProperties() {
        return properties;
    }
    
}
