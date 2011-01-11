package com.rameses.beaninfo.editor;

import java.beans.PropertyEditorSupport;
import javax.swing.SwingConstants;

public class SwingCaptionOrientation extends PropertyEditorSupport {
    
    private String[] values;
    
    
    public SwingCaptionOrientation() {
        values = new String[]
        {
            "LEFT", "RIGHT", "TOP", "BOTTOM"
        };
    }
    
    public String[] getTags() { return values; }
    
    public String getJavaInitializationString() {
        StringBuffer sb = new StringBuffer();
        sb.append(SwingConstants.class.getName() + "." + getAsText());
        return sb.toString();
    }
    
    public String getAsText() {
        if( getValue() == null );
        else if( getValue().equals(SwingConstants.TOP) ) return "TOP";
        else if( getValue().equals(SwingConstants.RIGHT) ) return "RIGHT";
        else if( getValue().equals(SwingConstants.BOTTOM) ) return "BOTTOM";
        
        return "LEFT";
    }
    
    public void setAsText(String text) throws IllegalArgumentException {
        if ( "TOP".equals(text) )
            setValue(SwingConstants.TOP);
        else if ( "RIGHT".equals(text) )
            setValue(SwingConstants.RIGHT);
        else if ( "BOTTOM".equals(text) )
            setValue(SwingConstants.BOTTOM);
        else
            setValue(SwingConstants.LEFT);
    }
    
}
