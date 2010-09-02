package com.rameses.rcp.controlx;

import com.rameses.beans.AbstractBeanInfo;
import com.rameses.rcp.control.*;
import java.beans.BeanDescriptor;


/**
 *
 * @author jaycverg
 */
public class XButtonBeanInfo extends AbstractBeanInfo {
    
    public Class getBeanClass() {
        return XButton.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }
    
}
