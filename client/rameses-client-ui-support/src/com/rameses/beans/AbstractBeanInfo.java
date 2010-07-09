package com.rameses.beans;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanInfo extends SimpleBeanInfo 
{
    private Class beanClass;
    private BeanDescriptor beanDescriptor;
    private PropertyDescriptor[] propertyDescriptors; 
    
    public AbstractBeanInfo() 
    {
        beanClass = getBeanClass(); 
        beanDescriptor = new BeanDescriptor(beanClass);
        propertyDescriptors = new PropertyDescriptor[]{}; 
        
        reflectClassProperties();         
        init(beanDescriptor); 
    }
    
    public abstract Class getBeanClass(); 
    
    public abstract void init(BeanDescriptor beanDescriptor); 
        
    public BeanDescriptor getBeanDescriptor() { 
        return beanDescriptor;  
    } 

    
    public PropertyDescriptor[] getPropertyDescriptors() {
        return propertyDescriptors; 
    }
    
    private void reflectClassProperties() 
    {
        List getters = new ArrayList();
        
        Method[] methods = beanClass.getDeclaredMethods();
        for (int i=0; i<methods.length; i++)
        {
            Method m = methods[i];
            if (m.getParameterTypes() == null || m.getParameterTypes().length != 0) continue;
            
            String name = m.getName(); 
            if (name.startsWith("is") && name.length() > 2) 
            { 
                findSetterMethod(methods, "set"+name.substring(2), boolean.class);
            }
            else if (name.startsWith("get") && name.length() > 3) 
            {
               System.out.println(m.getName() + ", " + m.getReturnType() + ", " + m.getReturnType().getName());
            }
        }
        
        List list = new ArrayList(); 
    }
    
    private Method findSetterMethod(Method[] methods, String name, Class paramType)
    {
        for (int i=0; i<methods.length; i++)
        {
            Method m = methods[i]; 
            String methodName = m.getName(); 
            if (!methodName.equals(name)) continue;
            if (m.getParameterTypes() == null || m.getParameterTypes().length != 1) continue; 
            if (m.getParameterTypes()[0] != paramType) continue; 
            
            return m; 
        }
        return null; 
    }
    
}
