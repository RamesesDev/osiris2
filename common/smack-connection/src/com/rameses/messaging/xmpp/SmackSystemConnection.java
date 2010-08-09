/*
 * SmackSystemConnection.java
 *
 * Created on August 9, 2010, 2:34 PM
 * @author jaycverg
 */

package com.rameses.messaging.xmpp;

import com.rameses.util.MachineInfo;


public class SmackSystemConnection extends SmackConnection {
    
    private String machineAccount;
    
    public SmackSystemConnection() {
        try {
            machineAccount = MachineInfo.getInstance().getMacAddress().hashCode()+"";
        }
        catch(Exception e) {
            throw new IllegalStateException("Cannot read mac address", e);
        }
    }
    
    public String getUsername() {
        return machineAccount;
    }

    public String getPassword() {
        return machineAccount;
    }
        
}
