/*
 * MessagingDelegate.java
 *
 * Created on July 24, 2010, 11:18 AM
 * @author jaycverg
 */

package test.smack;

import com.rameses.messaging.ConnectionManager;
import com.rameses.messaging.SmackMessagingConnection;
import java.util.Map;


public class MessagingDelegate {
    
    public static SmackMessagingConnection getConnection(Map conf, String uname, String pwd) {
        try {
            String host = (String) conf.get("host");
            String driverClass = (String) conf.get("driverClass");
            
            return (SmackMessagingConnection) ConnectionManager.getInstance()
            .getConnection(driverClass, host, uname, pwd);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
}
