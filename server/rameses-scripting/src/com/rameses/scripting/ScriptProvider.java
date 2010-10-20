package com.rameses.scripting;

import java.io.InputStream;

public interface ScriptProvider {
    
    Class parseClass(String code);
    Class parseClass(InputStream resource);
    ClassLoader getClassLoader();
    
}