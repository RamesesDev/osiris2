/*
 * SystemMessagingConnection.java
 *
 * Created on July 24, 2010, 9:54 AM
 * @author jaycverg
 */

package com.rameses.messaging.xmpp;

import com.rameses.util.MachineInfo;
import java.rmi.server.UID;

public class SmackSystemConnection extends SmackConnection {
    
    public String getUsername() {
        return getInfo();
    }

    public String getPassword() {
        return getInfo();
    }
    
    private String getInfo() {
        try {
            return MachineInfo.getInstance().getMacAddress().hashCode()+"";
        }
        catch(Exception e) {
            //create random
            return "RAND" + new UID();
        }
    }

}
