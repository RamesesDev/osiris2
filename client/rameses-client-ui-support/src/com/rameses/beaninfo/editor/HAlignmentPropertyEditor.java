package com.rameses.beaninfo.editor;

import com.rameses.rcp.constant.UIConstants;
import java.beans.PropertyEditorSupport;

public class HAlignmentPropertyEditor extends PropertyEditorSupport {
    
    private String[] values;
    
    public HAlignmentPropertyEditor() {
        values = new String[]
        {
            UIConstants.LEFT, UIConstants.CENTER, UIConstants.RIGHT
        };
    }
    
    public String[] getTags() { return values; }

    
    public String getJavaInitializationString() {
        StringBuffer sb = new StringBuffer();
        sb.append(UIConstants.class.getName() + "." + getValue());
        return sb.toString();
    }

}
