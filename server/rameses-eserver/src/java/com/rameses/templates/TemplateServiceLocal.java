package com.rameses.templates;

import java.io.OutputStream;

public interface TemplateServiceLocal {
    
    Object transform(String fileName, Object data) throws Exception;
    Object transform(String fileName, Object data, OutputStream out ) throws Exception;
    
}
