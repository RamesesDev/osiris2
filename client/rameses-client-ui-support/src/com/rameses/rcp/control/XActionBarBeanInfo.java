/*
 * XActionBarBeanInfo.java
 *
 * Created on October 8, 2010, 1:37 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.ButtonTemplatePropertyEditor;
import com.rameses.beaninfo.editor.OrientationPropertyEditor;
import com.rameses.beaninfo.editor.HAlignmentPropertyEditor;
import com.rameses.beaninfo.editor.SwingCaptionOrientation;
import com.rameses.beaninfo.editor.VAlignmentPropertyEditor;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import javax.swing.ImageIcon;


public class XActionBarBeanInfo extends AbstractBeanInfo {
    
    private ImageIcon icon;
    
    public XActionBarBeanInfo() {

    }

    public Class getBeanClass() {
        return XActionBar.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }

    public void property(String propertyName, PropertyDescriptor desc) {
        if ( "orientation".equals(propertyName) )
            desc.setPropertyEditorClass(OrientationPropertyEditor.class);
        else if ( "orientationHAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(HAlignmentPropertyEditor.class);
        else if ( "orientationVAlignment".equals(propertyName) )
            desc.setPropertyEditorClass(VAlignmentPropertyEditor.class);
        else if ( "buttonTemplate".equals(propertyName) )
            desc.setPropertyEditorClass(ButtonTemplatePropertyEditor.class);
        else if ( "buttonCaptionOrientation".equals(propertyName) )
            desc.setPropertyEditorClass(SwingCaptionOrientation.class);

    }
    
}
