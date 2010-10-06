/*
 * OpenerProvider.java
 *
 * Created on October 6, 2010, 1:38 PM
 * @author jaycverg
 */

package com.rameses.rcp.framework;

import com.rameses.rcp.common.Opener;
import java.util.List;


public interface OpenerProvider {
    
    List<Opener> getOpeners(String type, Object context);
    
}
