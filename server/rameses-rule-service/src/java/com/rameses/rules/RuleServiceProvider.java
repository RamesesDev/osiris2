package com.rameses.rules;

import java.util.Enumeration;
import javax.naming.Context;

public interface RuleServiceProvider {

    void setContext( Context ctx);
    Enumeration<RuleSource> getRules(String filterName);
    
}
