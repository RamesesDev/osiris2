package com.rameses.rcp.framework;

import com.rameses.rcp.framework.UIController;
import java.util.Map;

/**
 *
 * @author jaycverg
 */
public interface ControllerProvider {
    
    UIController getController(String name);
    
}
