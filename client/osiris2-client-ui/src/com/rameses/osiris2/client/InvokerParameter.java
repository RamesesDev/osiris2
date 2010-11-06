/*
 * InvokerParameter.java
 *
 * Created on September 20, 2010, 5:04 PM
 * @author jaycverg
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import java.util.Map;


public interface InvokerParameter {
    
    Map getParams(Invoker inv);
    
}
