/*
 * FormPanelBeanInfo.java
 *
 * Created on October 7, 2010, 5:17 PM
 * @author jaycverg
 */

package com.rameses.rcp.util;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.CaptionOrientationPropertyEditor;
import com.rameses.beaninfo.editor.ComponentsOrientationPropertyEditor;
import com.rameses.beaninfo.editor.HorizontalAlignmentPropertyEditor;
import com.rameses.beaninfo.editor.VerticalAlignmentPropertyEditor;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;


public class FormPanelBeanInfo extends AbstractBeanInfo {
    
    public FormPanelBeanInfo() {
    }

    public Class getBeanClass() {
        return FormPanel.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }

    public void property(String propertyName, PropertyDescriptor desc) {
        if ( "captionHAlignment".equals(propertyName) ) 
            desc.setPropertyEditorClass(HorizontalAlignmentPropertyEditor.class);
        else if ( "captionVAlignment".equals(propertyName) ) 
            desc.setPropertyEditorClass(VerticalAlignmentPropertyEditor.class);
        else if ( "captionOrientation".equals(propertyName) )
            desc.setPropertyEditorClass(CaptionOrientationPropertyEditor.class);
        else if ( "orientation".equals(propertyName) )
            desc.setPropertyEditorClass(ComponentsOrientationPropertyEditor.class);
            
             
    }
    
}
