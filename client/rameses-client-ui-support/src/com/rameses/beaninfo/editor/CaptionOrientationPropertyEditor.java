package com.rameses.beaninfo.editor;

import com.rameses.rcp.constant.UIConstants;
import java.beans.PropertyEditorSupport;

public class CaptionOrientationPropertyEditor extends PropertyEditorSupport
{
    private String[] values;
    
    public CaptionOrientationPropertyEditor()
    {
        values = new String[]
        {
            UIConstants.LEFT, UIConstants.RIGHT, UIConstants.TOP, UIConstants.BOTTOM
        };
    }
    
    public String[] getTags() { return values; }
    
}
