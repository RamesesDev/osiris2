package com.rameses.beaninfo.editor;
import com.rameses.rcp.constant.TextCase;

public class TextCasePropertyEditor extends AbstractEnumPropertyEditor
{
    protected Class getEnumClass() { return TextCase.class; }
    
}
