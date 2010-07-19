/*
 * ActionEvent.java
 *
 * Created on July 10, 2010, 2:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.common;

import java.io.Serializable;

/**
 *
 * @author ms
 */
public class AsyncEvent implements Serializable {
    
    private int loop = 0;
    
    //status = 0 more;
    //status = 1 last loop
    private int status;
    
    /** Creates a new instance of ActionEvent */
    public AsyncEvent() {
    }

    public int getLoop() {
        return loop;
    }

    public void setLoop(int loop) {
        this.loop = loop;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    public void nextLoop() {
        loop++;
    }
    
    public void flagLast() {
        status = 1;
    }
    
    public boolean isLastLoop() {
        return status == 1;
    }
    
}
