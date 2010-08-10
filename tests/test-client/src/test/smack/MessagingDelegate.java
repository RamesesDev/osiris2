/*
 * MessagingDelegate.java
 *
 * Created on July 24, 2010, 11:18 AM
 * @author jaycverg
 */

package test.smack;


import com.rameses.messaging.ConnectionManager;
import com.rameses.messaging.xmpp.SmackSystemConnection;
import java.util.Map;


public class MessagingDelegate {
    
    public static SmackSystemConnection getConnection(Map conf) {
        try {
            String host = "10.0.0.104";//(String) conf.get("host");
            String driverClass = "com.rameses.messaging.xmpp.SmackSystemConnection";//(String) conf.get("driverClass");
            
            return (SmackSystemConnection) ConnectionManager.getInstance()
            .getConnection(driverClass, host);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
        
}
