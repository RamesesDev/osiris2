package com.rameses.rcp.impl;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.MethodResolver;
import com.rameses.util.PropertyResolver;
import org.apache.commons.beanutils.MethodUtils;

/**
 *
 * @author jaycverg
 */
public class MethodResolverImpl implements MethodResolver {
        
    public Object invoke(Object bean, String action, Class[] paramTypes, Object[] args) throws Exception {
        String xaction = action;
        Object xbean = bean;
        if( xaction.indexOf(".")>0) {
            xaction = action.substring(action.lastIndexOf(".")+1);
            String p = action.substring(0, action.lastIndexOf("."));
            PropertyResolver resolver = ClientContext.getCurrentContext().getPropertyResolver();
            xbean = resolver.getProperty(bean, p);
        }
        
        return MethodUtils.invokeMethod(xbean, xaction, args);
    }
    
}
