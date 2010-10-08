package com.rameses.beaninfo;

import java.beans.BeanDescriptor;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanInfo extends SimpleBeanInfo {
    
    private Class beanClass;
    private BeanDescriptor beanDescriptor;
    private PropertyDescriptor[] propertyDescriptors;
    
    
    public AbstractBeanInfo() {
        beanClass = getBeanClass();
        beanDescriptor = new BeanDescriptor(beanClass);
        propertyDescriptors = new PropertyDescriptor[]{};
        
        reflectClassProperties();
        init(beanDescriptor);
    }
    
    public abstract Class getBeanClass();
    public abstract void init(BeanDescriptor beanDescriptor);
    public abstract void property(String propertyName, PropertyDescriptor descriptor);
    
    public BeanDescriptor getBeanDescriptor() {
        return beanDescriptor;
    }
    
    
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors;
    }
    
    private void reflectClassProperties() {
        List list = new ArrayList();
        
        Method[] methods = beanClass.getMethods();
        for (int i=0; i<methods.length; i++) {
            Method m = methods[i];
            if (m.getParameterTypes() == null || m.getParameterTypes().length != 0) continue;
            
            String name = m.getName();
            if (name.startsWith("is") && name.length() > 2) {
                Method mm = findSetterMethod("set"+name.substring(2), boolean.class);
                if ( mm != null ) {
                    try {
                        String propertyName = formatPropertyName(name.substring(2));
                        PropertyDescriptor desc = new PropertyDescriptor(propertyName, m, mm);
                        property(propertyName, desc);
                        list.add(desc);
                        
                    } catch (IntrospectionException ex) {;}
                }
                
            } else if (name.startsWith("get") && name.length() > 3) {
                Method mm = findSetterMethod("set"+name.substring(3), m.getReturnType());
                if ( mm != null ) {
                    try {
                        String propertyName = formatPropertyName(name.substring(3));
                        PropertyDescriptor desc = new PropertyDescriptor(propertyName, m, mm);
                        property(propertyName, desc);
                        list.add(desc);
                        
                    } catch (IntrospectionException ex) {;}
                }
            }
        }
        
        propertyDescriptors = (PropertyDescriptor[]) list.toArray(new PropertyDescriptor[]{});
        
    }
    
    private Method findSetterMethod(String name, Class paramType) {
        try {
            return beanClass.getMethod(name, paramType);
        } catch (Exception ex) {;} 
        
        return null;
    }
    
    private String formatPropertyName(String propertyName) {
        if ( propertyName.length() == 1)
            return propertyName.toLowerCase();
        else
            return propertyName.substring(0,1).toLowerCase() + propertyName.substring(1);
    }
    
}
