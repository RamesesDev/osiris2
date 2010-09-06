package com.rameses.common;

import java.util.Map;

public interface ExpressionResolver 
{
    
    Object evaluate(String expression,Map resources);
    
}
