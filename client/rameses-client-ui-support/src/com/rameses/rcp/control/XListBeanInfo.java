/*
 * XListBeanInfo.java
 *
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.SwingConstantsHAlignment;
import com.rameses.beaninfo.editor.SwingConstantsVAlignment;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;


public class XListBeanInfo extends AbstractBeanInfo {
    
    public XListBeanInfo() {
    }

    public Class getBeanClass() {
        return XList.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }

    public void property(String propertyName, PropertyDescriptor desc) {
        if ( "cellHorizontalAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(SwingConstantsHAlignment.class);
        else if ( "cellVerticalAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(SwingConstantsVAlignment.class);
    }
    
}
