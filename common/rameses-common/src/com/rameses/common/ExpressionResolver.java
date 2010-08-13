package com.rameses.common;

public interface ExpressionResolver 
{
    
    Object evaluate(Object bean, String expression);
    
}
