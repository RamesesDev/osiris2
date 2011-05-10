package com.rameses.beaninfo.editor;

import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.math.BigInteger;

public class FieldTypePropertyEditor extends PropertyEditorSupport {
    
    private String[] values;
    
    
    public FieldTypePropertyEditor() {
        values = new String[]
        {
            "BigDecimal", "BigInteger", "Double", "Float", "Integer", "double", "int"
        };
    }
    
    public String[] getTags() { return values; }
    
    public String getJavaInitializationString() {
        String text = getAsText();
        
        if( text == null )           return null;
        if( values[0].equals(text) ) return BigDecimal.class.getName() + ".class";
        if( values[1].equals(text) ) return BigInteger.class.getName() + ".class";
        if( values[2].equals(text) ) return Double.class.getName() + ".class";
        if( values[3].equals(text) ) return Float.class.getName() + ".class";
        if( values[4].equals(text) ) return Integer.class.getName() + ".class";
        if( values[5].equals(text) ) return "double.class";
        if( values[6].equals(text) ) return "int.class";
        
        return null;
    }
    
    public String getAsText() {
        Object value = getValue();
        
        if( value == null )                   return null;
        if ( BigDecimal.class.equals(value) ) return values[0];
        if ( BigInteger.class.equals(value) ) return values[1];
        if ( Double.class.equals(value) )     return values[2];
        if ( Float.class.equals(value) )      return values[3];
        if ( Integer.class.equals(value) )    return values[4];
        if ( double.class.equals(value) )     return values[5];
        if ( int.class.equals(value) )        return values[6];
                
        return null;
    }
    
    public void setAsText(String text) throws IllegalArgumentException {
        if( values[0].equals(text) )      setValue( BigDecimal.class );
        else if( values[1].equals(text) ) setValue( BigInteger.class );
        else if( values[2].equals(text) ) setValue( Double.class );
        else if( values[3].equals(text) ) setValue( Float.class );
        else if( values[4].equals(text) ) setValue( Integer.class );
        else if( values[5].equals(text) ) setValue( double.class );
        else if( values[6].equals(text) ) setValue( int.class );
    }
    
}
