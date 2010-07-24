/*
 * SmackDelegate.java
 *
 * Created on July 24, 2010, 11:18 AM
 * @author jaycverg
 */

package test.smack;

import com.rameses.messaging.ConnectionManager;
import com.rameses.messaging.SmackMessagingConnection;


public class SmackDelegate {
    
    public static SmackMessagingConnection getConnection(String uname, String pwd) {
        try {
            return (SmackMessagingConnection) ConnectionManager.getInstance()
            .getConnection(SmackMessagingConnection.class.getName(), "etracs.org", uname, pwd);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
