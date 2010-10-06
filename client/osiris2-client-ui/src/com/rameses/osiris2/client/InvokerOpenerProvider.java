/*
 * InvokerOpenerProvider.java
 *
 * Created on October 6, 2010, 1:41 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.rcp.common.Opener;
import com.rameses.rcp.framework.OpenerProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class InvokerOpenerProvider implements OpenerProvider {
    
    public List<Opener> getOpeners(String type, Object context) {
        List<Invoker> invList = InvokerUtil.lookup(type, context);
        List<Opener> openers = new ArrayList();
        for(Invoker inv: invList) {
            Opener o = new Opener();
            o.setName(inv.getWorkunitid());
            o.setCaption(inv.getCaption());
            o.setAction(inv.getAction());
            
            Map props = inv.getProperties();
            if ( props.get("target") != null ) o.setTarget(props.get("target")+"");
            if ( props.get("outcome") != null ) o.setOutcome(props.get("outcome")+"");
            
            o.getProperties().putAll(props);
            
            openers.add(o);
        }
        return openers;
    }
    
}
