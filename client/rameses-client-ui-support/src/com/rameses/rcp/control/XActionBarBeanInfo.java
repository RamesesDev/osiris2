/*
 * XActionBarBeanInfo.java
 *
 * Created on October 8, 2010, 1:37 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.ComponentsOrientationPropertyEditor;
import com.rameses.beaninfo.editor.HorizontalAlignmentPropertyEditor;
import com.rameses.beaninfo.editor.VerticalAlignmentPropertyEditor;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;


public class XActionBarBeanInfo extends AbstractBeanInfo {
    
    public XActionBarBeanInfo() {
    }

    public Class getBeanClass() {
        return XActionBar.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }

    public void property(String propertyName, PropertyDescriptor desc) {
        if ( "orientation".equals(propertyName) )
            desc.setPropertyEditorClass(ComponentsOrientationPropertyEditor.class);
        else if ( "orientationHAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(HorizontalAlignmentPropertyEditor.class);
        else if ( "orientationVAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(VerticalAlignmentPropertyEditor.class);
    }
    
}
