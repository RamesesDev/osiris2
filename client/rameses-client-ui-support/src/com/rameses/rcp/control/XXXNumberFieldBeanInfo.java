/*
 * XXXNumberFieldBeanInfo.java
 *
 * Created on May 10, 2011, 1:49 PM
 * @author jaycverg
 */

package com.rameses.rcp.control;

import com.rameses.beaninfo.editor.FieldTypePropertyEditor;
import java.beans.PropertyDescriptor;


public class XXXNumberFieldBeanInfo extends XTextFieldBeanInfo {
    
    public XXXNumberFieldBeanInfo() {
    }

    public Class getBeanClass() {
        return XNumberField.class;
    }

    public void property(String propertyName, PropertyDescriptor desc) {
        super.property(propertyName, desc);
        
//        if( "fieldType".equals(propertyName) ) {
//            desc.setPropertyEditorClass(FieldTypePropertyEditor.class);
//        }
    }
    
}
