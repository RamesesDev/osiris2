package com.rameses.email.service;

import java.util.Map;

public interface EmailServiceLocal {
    
    void send( String template, Map params );
    
    
}
