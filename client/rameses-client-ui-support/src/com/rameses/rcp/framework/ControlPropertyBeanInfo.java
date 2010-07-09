package com.rameses.rcp.framework;

import com.rameses.beans.AbstractBeanInfo;
import java.beans.BeanDescriptor;
import test.control.Test;

public class ControlPropertyBeanInfo extends AbstractBeanInfo
{
    public Class getBeanClass() { return Test.class; }

    public void init(BeanDescriptor beanDescriptor) 
    {
        
    }
    
}
