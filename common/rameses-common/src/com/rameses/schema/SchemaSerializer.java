package com.rameses.schema;


public interface SchemaSerializer {

    Object read(String s);
    String write(Object s);
    
}
