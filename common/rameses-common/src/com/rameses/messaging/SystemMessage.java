/*
 * Message.java
 *
 * Created on July 24, 2010, 10:07 AM
 * @author jaycverg
 */

package com.rameses.messaging;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemMessage extends AbstractMessage {

    private String requestid;
    private String pushid;
    private static Pattern pattern = Pattern.compile("REQUEST-ID:(.*)\nPUSH-ID:(.*)");
    
    public String getType() {
        return "system";
    }

    
    /***
     * This constructor is used when sending messages
     */
    public SystemMessage(String requestId, String pushId) {
        this.requestid = requestId;
        this.pushid = pushId;
    }
    
    //this is used when receiving
    public SystemMessage(String msg ) {
        Matcher m = pattern.matcher( msg );
        if ( m.matches() ) {
            this.requestid = m.group(1);
            this.pushid = m.group(2) ;
        }
    }

    public String getBody() {
        return "REQUEST-ID:" + requestid + "\nPUSH-ID:"+pushid;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getPushid() {
        return pushid;
    }

    public void setPushid(String pushid) {
        this.pushid = pushid;
    }

    
}
