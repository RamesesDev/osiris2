package com.rameses.beaninfo.editor;

import java.beans.PropertyEditorSupport;
import javax.swing.SwingConstants;

public class SwingConstantsVAlignment extends PropertyEditorSupport {
    
    private String[] values = new String[]{ "TOP", "CENTER", "BOTTOM" };
    
    
    public String[] getTags() { return values; }
    
    public String getJavaInitializationString() {
        StringBuffer sb = new StringBuffer();
        sb.append(SwingConstants.class.getName() + "." + getAsText());
        return sb.toString();
    }
    
    public String getAsText() {
        if( getValue() == null );
        else if( getValue().equals(SwingConstants.CENTER) ) return "CENTER";
        else if( getValue().equals(SwingConstants.RIGHT) ) return "BOTTOM";
        
        return "TOP";
    }
    
    public void setAsText(String text) throws IllegalArgumentException {
        if ( "CENTER".equals(text) )
            setValue(SwingConstants.CENTER);
        else if ( "BOTTOM".equals(text) )
            setValue(SwingConstants.BOTTOM);
        else
            setValue(SwingConstants.TOP);
        
    }
    
}
