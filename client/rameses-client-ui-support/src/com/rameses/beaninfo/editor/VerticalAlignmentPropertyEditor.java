package com.rameses.beaninfo.editor;

import com.rameses.rcp.constant.UIConstants;
import java.beans.PropertyEditorSupport;

public class VerticalAlignmentPropertyEditor extends PropertyEditorSupport
{
    private String[] values;
    
    public VerticalAlignmentPropertyEditor()
    {
        values = new String[]
        {
            UIConstants.TOP, UIConstants.CENTER, UIConstants.BOTTOM
        };
    }
    
    public String[] getTags() { return values; }
    
}
