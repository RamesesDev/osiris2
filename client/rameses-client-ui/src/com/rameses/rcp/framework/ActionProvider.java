package com.rameses.rcp.framework;

import com.rameses.rcp.common.Action;
import java.util.List;

/**
 * This interface will be implemented to provide actions
 *  category = filters actions only with specified category
 *  context = optional. If provided, it is used to filter the action. 
 */
public interface ActionProvider {
    
    boolean hasItems( String category, Object context );
    List<Action> getActions(String category, Object context);
    
}
