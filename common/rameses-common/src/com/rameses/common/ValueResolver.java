package com.rameses.common;


public interface ValueResolver {

    Class getType(Object bean, String name);
    Object getValue(Object bean, String name);
    void setValue(Object bean, String name, Object o);
    
}
