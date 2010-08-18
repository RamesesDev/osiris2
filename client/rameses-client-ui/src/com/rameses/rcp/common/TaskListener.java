/*
 * TaskListener.java
 *
 * Created on August 10, 2010, 4:02 PM
 * @author jaycverg
 */

package com.rameses.rcp.common;


public interface TaskListener {

    void onStart();
    void onStop();
    void onCancel();
    
}
