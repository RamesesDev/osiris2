/*
 * TestSessionBean.java
 * Created on September 17, 2011, 4:23 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.eserver;

import com.rameses.server.common.AppContext;
import com.rameses.server.common.JndiUtil;
import java.io.Serializable;
import javax.naming.InitialContext;

/**
 *
 * @author jzamss
 */
public class TestSessionBean implements TestSessionBeanMBean,Serializable {
    
    public Object test(Object o) {
        System.out.println("receive session bean " + o);
        return "echo " + o;
    }

    public void start() throws Exception {
        InitialContext ctx = new InitialContext();
        System.out.println("binded->" + AppContext.getPath()+TestSessionBean.class.getSimpleName()+"/local");
        JndiUtil.bind(ctx, AppContext.getPath()+TestSessionBean.class.getSimpleName()+"/local", this);
    }

    public void stop() throws Exception {
        InitialContext ctx = new InitialContext();
        JndiUtil.unbind(ctx, AppContext.getPath()+TestSessionBean.class.getSimpleName()+"/local");
    }
    
}
