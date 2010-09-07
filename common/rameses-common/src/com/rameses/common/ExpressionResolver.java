package com.rameses.common;

import java.util.Map;

public interface ExpressionResolver 
{
    
    public Object evaluate(Object bean, String expression);
    
}
