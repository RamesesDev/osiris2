package com.rameses.rcp.control;

import com.rameses.rcp.ui.Containable;
import com.rameses.rcp.ui.ControlProperty;
import com.rameses.rcp.ui.Validatable;
import com.rameses.rcp.util.ActionMessage;

/**
 *
 * @author Windhel
 */

public class LookupField extends XIconedTextField {
    
    public LookupField() {
        super();
        setIcon("/home/rameses/Desktop/images/search.png");
        setOrientation("RIGHT");
    }    
    
    public void actionPerformed(){        
        System.out.println("firing action");
    }

}
