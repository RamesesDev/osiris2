package com.rameses.beaninfo.editor;

import com.rameses.rcp.constant.UIConstants;
import java.beans.PropertyEditorSupport;

public class ComponentsOrientationPropertyEditor extends PropertyEditorSupport
{
    private String[] values;
    
    public ComponentsOrientationPropertyEditor()
    {
        values = new String[]
        {
            UIConstants.HORIZONTAL, UIConstants.VERTICAL
        };
    }
    
    public String[] getTags() { return values; }
    
}
