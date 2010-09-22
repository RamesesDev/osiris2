/*
 * InvokerAction.java
 *
 * Created on September 20, 2010, 5:01 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.rcp.common.Action;
import java.util.Map;


public class InvokerAction extends Action  {
    
    private Invoker invoker;
    private InvokerParameter invParam;
    
    public InvokerAction() {
    }
    
    public InvokerAction(Invoker inv, InvokerParameter invParam) {
        this.invoker = inv;
        this.invParam = invParam;
    }
    
    public Object execute() {
        Map p = null;
        if ( invParam != null ) {
            p = invParam.getParams();
        }
        return InvokerUtil.invokeAction(this);
    }

    public Invoker getInvoker() {
        return invoker;
    }

    public void setInvoker(Invoker invoker) {
        this.invoker = invoker;
    }

    public InvokerParameter getInvokerParam() {
        return invParam;
    }

    public void setInvokerParam(InvokerParameter invParam) {
        this.invParam = invParam;
    }
    
}
