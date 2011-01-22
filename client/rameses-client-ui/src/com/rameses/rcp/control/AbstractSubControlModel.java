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


public class AbstractSubControlModel {
    
    private static final long serialVersionUID = 1L;
    
    private Binding controlBinding;
    private String beanName;
    private String fieldName;
    
    
    public AbstractSubControlModel() {
    }
    
    
    //overridable
    public void onInit() {}
    public void onRefresh() {}
    
    //package level mutators
    void setControlBinding(Binding controlBinding) {
        this.controlBinding = controlBinding;
    }
    
    void setBeanName(String beanName) {
        this.beanName = beanName;
    }
    
    void setFieldName(String fieldName) {
        this.fieldName = fieldName;
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
        PropertyResolver res = ClientContext.getCurrentContext().getPropertyResolver();
        Object bean = res.getProperty(controlBinding.getBean(), beanName);
        return res.getProperty(bean, fieldName);
    }
    
}
