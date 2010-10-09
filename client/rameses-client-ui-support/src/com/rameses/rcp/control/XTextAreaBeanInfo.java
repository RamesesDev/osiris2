/*
 * XTextFieldBeanInfo.java
 *
 * Created on October 8, 2010, 1:42 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.beaninfo.AbstractBeanInfo;
import com.rameses.beaninfo.editor.TextCasePropertyEditor;
import com.rameses.beaninfo.editor.TrimSpaceOptionPropertyEditor;
import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;


public class XTextAreaBeanInfo extends AbstractBeanInfo {
    
    public XTextAreaBeanInfo() {
    }

    public Class getBeanClass() {
        return XTextArea.class;
    }

    public void init(BeanDescriptor beanDescriptor) {
    }

    public void property(String propertyName, PropertyDescriptor desc) {
        if ( "textCase".equals(propertyName) )
            desc.setPropertyEditorClass(TextCasePropertyEditor.class);
        else if ( "trimSpaceOption".equals(propertyName) )
            desc.setPropertyEditorClass(TrimSpaceOptionPropertyEditor.class);
    }
    
}
