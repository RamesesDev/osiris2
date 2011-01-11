package com.rameses.beaninfo.editor;

import com.rameses.rcp.constant.UIConstants;
import java.beans.PropertyEditorSupport;

public class VAlignmentPropertyEditor extends PropertyEditorSupport {
    private String[] values;
    
    public VAlignmentPropertyEditor() {
        values = new String[]
        {
            UIConstants.TOP, UIConstants.CENTER, UIConstants.BOTTOM
        };
    }
    
    public String[] getTags() { return values; }
    
    public String getJavaInitializationString() {
        StringBuffer sb = new StringBuffer();
        sb.append(UIConstants.class.getName() + "." + getValue());
        return sb.toString();
    }
    
}
