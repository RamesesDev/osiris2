/*
 * FormPanelBeanInfo.java
 *
 * Created on October 7, 2010, 5:17 PM
 * @author jaycverg
 */

package com.rameses.rcp.util;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.CaptionOrientationPropertyEditor;
import com.rameses.beaninfo.editor.OrientationPropertyEditor;
import com.rameses.beaninfo.editor.HAlignmentPropertyEditor;
import com.rameses.beaninfo.editor.VAlignmentPropertyEditor;
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
            desc.setPropertyEditorClass(HAlignmentPropertyEditor.class);
        else if ( "captionVAlignment".equals(propertyName) ) 
            desc.setPropertyEditorClass(VAlignmentPropertyEditor.class);
        else if ( "captionOrientation".equals(propertyName) )
            desc.setPropertyEditorClass(CaptionOrientationPropertyEditor.class);
        else if ( "orientation".equals(propertyName) )
            desc.setPropertyEditorClass(OrientationPropertyEditor.class);
            
             
    }
    
}
