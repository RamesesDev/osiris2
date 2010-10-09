package com.rameses.beaninfo.editor;

import com.rameses.rcp.constant.UIConstants;
import java.beans.PropertyEditorSupport;
import javax.swing.SwingConstants;

public class HorizontalAlignmentPropertyEditor extends PropertyEditorSupport
{
    private String[] values;
    
    public HorizontalAlignmentPropertyEditor()
    {
        values = new String[]
        {
            UIConstants.LEFT, UIConstants.CENTER, UIConstants.RIGHT
        };
    }
    
    public String[] getTags() { return values; }
}
