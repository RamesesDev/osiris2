/*
 * UIComposite.java
 *
 * Created on July 23, 2010, 1:27 PM
 * @author jaycverg
 */

package com.rameses.rcp.ui;

import java.util.List;


public interface UIComposite extends UIControl {
    
    List<? extends UIControl> getControls();
    
}
