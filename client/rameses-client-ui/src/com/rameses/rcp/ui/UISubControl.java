/*
 * UISubControl.java
 *
 * Created on July 28, 2010, 10:46 AM
 * @author jaycverg
 */

package com.rameses.rcp.ui;

import com.rameses.rcp.framework.Binding;
import java.util.List;


public interface UISubControl extends UIControl {
 
    List<Binding> getSubBindings();
    
}
