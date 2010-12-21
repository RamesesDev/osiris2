/*
 * XTextFieldBeanInfo.java
 *
 * Created on October 8, 2010, 1:42 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.SwingConstantsHAlignment;
import com.rameses.beaninfo.editor.SwingConstantsVAlignment;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;


public class XPanelBeanInfo extends AbstractBeanInfo {
    
    public XPanelBeanInfo() {
    }

    public Class getBeanClass() {
        return XPanel.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }

    public void property(String propertyName, PropertyDescriptor desc) {
        if ( "emptyTextHAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(SwingConstantsHAlignment.class);
        else if ( "emptyTextVAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(SwingConstantsVAlignment.class);
    }
    
}
