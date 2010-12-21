package com.rameses.beaninfo.editor;

import java.beans.PropertyEditorSupport;
import javax.swing.SwingConstants;

public class SwingConstantsHAlignment extends PropertyEditorSupport {
    
    private String[] values = new String[]{ "LEFT", "CENTER", "RIGHT" };
    

    public String[] getTags() { return values; }
    
    public String getJavaInitializationString() {
        StringBuffer sb = new StringBuffer();
        sb.append(SwingConstants.class.getName() + "." + getAsText());
        return sb.toString();
    }
    
    public String getAsText() {
        if( getValue() == null );
        else if( getValue().equals(SwingConstants.CENTER) ) return "CENTER";
        else if( getValue().equals(SwingConstants.RIGHT) ) return "RIGHT";
        
        return "LEFT";
    }
    
    public void setAsText(String text) throws IllegalArgumentException {
        if ( "CENTER".equals(text) ) 
            setValue(SwingConstants.CENTER);
        else if ( "RIGHT".equals(text) ) 
            setValue(SwingConstants.RIGHT);
        else
            setValue(SwingConstants.LEFT);
        
    }
    
}
