package com.rameses.rcp.controlx;

import com.rameses.beans.AbstractBeanInfo;
import com.rameses.rcp.control.*;
import java.beans.BeanDescriptor;


/**
 *
 * @author jaycverg
 */
public class XLabelBeanInfo extends AbstractBeanInfo {
    
    public Class getBeanClass() {
        return XLabel.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }
}
