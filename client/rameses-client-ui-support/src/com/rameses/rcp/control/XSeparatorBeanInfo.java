/*
 * XSeparatorBeanInfo.java
 *
 * Created on October 12, 2010, 6:14 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.OrientationPropertyEditor;
import com.rameses.beaninfo.editor.HAlignmentPropertyEditor;
import com.rameses.beaninfo.editor.VAlignmentPropertyEditor;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;


public class XSeparatorBeanInfo extends AbstractBeanInfo {
    
    public XSeparatorBeanInfo() {
    }
    
    public Class getBeanClass() {
        return XSeparator.class;
    }
    
    public void init(BeanDescriptor beanDescriptor) {
    }
    
    public void property(String propertyName, PropertyDescriptor desc) {
        if ( "orientation".equals(propertyName) )
            desc.setPropertyEditorClass(OrientationPropertyEditor.class);
        else if ( "orientationHPosition".equals(propertyName) )
            desc.setPropertyEditorClass(VAlignmentPropertyEditor.class);
        else if ( "orientationVPosition".equals(propertyName) )
            desc.setPropertyEditorClass(HAlignmentPropertyEditor.class);
    }
    
}
