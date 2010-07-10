/*
 * ServiceDelegate.java
 *
 * Created on April 8, 2010, 12:50 PM
 * @author jacverg
 */

package test;

import com.rameses.rcp.framework.ClientContext;
import com.rameses.util.MethodResolver;
import java.util.List;
import java.util.Map;

public class ServiceDelegate {
    
    public static Object service = null;
    
    
    public static final List getList(Map params) {        
        return (List) invoke("queryTestimonies", null, new Object[]{ params });
    }
    
    
    public static final Object getService() {
        if ( service == null) {
            try {
                service = InvokerProxy.getInstance().create("TestService", "default.host");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return service;
    }
    
    private static final Object invoke(String methodName, Class[] types, Object[] params) {
        try {
            MethodResolver mr = ClientContext.getCurrentContext().getMethodResolver();
            return mr.invoke(getService(), methodName, types, params);
        } catch(Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
