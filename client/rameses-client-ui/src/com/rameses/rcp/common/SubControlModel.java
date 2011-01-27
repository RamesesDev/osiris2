/*
 * SubControlModel.java
 *
 * Created on January 22, 2011, 2:00 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;

import com.rameses.rcp.control.AbstractSubControlModel;

//template class
public class SubControlModel extends AbstractSubControlModel {
    
    private static final long serialVersionUID = 1L;
    
    public String getHtmlFormat() {
        Object ctx = getContext();
        if( ctx == null )
            return "";
        
        return ctx.toString();
    }
    
    public String getPrintFormat() {
        Object ctx = getContext();
        if( ctx == null )
            return "";
        
        return ctx.toString();
    }
    
}
