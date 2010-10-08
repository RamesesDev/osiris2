package com.rameses.beaninfo.editor;

import java.beans.PropertyEditorSupport;

public abstract class AbstractEnumPropertyEditor extends PropertyEditorSupport
{
    private Class<? extends Enum> enumClass;
    
    public AbstractEnumPropertyEditor() 
    {
        this.enumClass = getEnumClass();
    }

    protected abstract Class<? extends Enum> getEnumClass() ;

    public String[] getTags() 
    {
        Object[] objs = enumClass.getEnumConstants();
        String[] arr = new String[objs.length];
        for (int i=0; i<arr.length; i++) { 
            arr[i] = objs[i].toString();
        }
        return arr;
    }
    
    public String getAsText() 
    {
        Object o = getValue();
        return (o != null) ? o.toString() : null;
    }
    
    public void setAsText(String text) throws IllegalArgumentException 
    {
        try {
            setValue(Enum.valueOf(enumClass, text));
        } catch(Exception ex) {
            setValue(null);
        }
    }

    public String getJavaInitializationString() 
    {
        try 
        {
            Object o = getValue();
            if (o == null) return null;
            
            StringBuffer sb = new StringBuffer();
            sb.append(enumClass.getName() + "." + o);
            return sb.toString();
        }
        catch(Exception ex) {
            return null;
        }
    }
    
}
