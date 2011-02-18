/*
 * Osiris2VariableResolver.java
 *
 * Created on May 21, 2010, 9:42 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.WorkUnitInstance;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.VariableResolver;

public class Osiris2VariableResolver extends VariableResolver {
    
    private VariableResolver baseResolver;
    
    public Osiris2VariableResolver(VariableResolver base) {
        this.baseResolver = base;
    }
    
    public Object resolveVariable(FacesContext context, String name) throws EvaluationException {
        if ("Controller".equals(name)) {
            WorkUnitInstance wi = WebContext.getInstance().getCurrentWorkUnitInstance();
            if ( wi != null ) {
                return wi.getController();
            }
        }
        
        return baseResolver.resolveVariable(context, name);
    }
    
}
