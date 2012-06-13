/*
 * ScriptMessageListener.java
 *
 * Created on June 11, 2012, 1:28 PM
 * @author jaycverg
 */

package com.rameses.xmpp.service;

import com.rameses.server.common.JsonUtil;
import com.rameses.service.ScriptServiceContext;
import java.util.Map;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;


public class ScriptMessageListener implements PacketListener, PacketFilter {
    
    private Map conf;
    private String serviceName = "MessageServiceHandler";
    private String originName;
    
    public ScriptMessageListener( Map conf, String svc, String originName ) {
        this.conf = conf;
        if(svc!=null) this.serviceName = svc;
        this.originName = originName;
    }
    
    public void processPacket(Packet packet) {
        try {
            if( packet instanceof Message ) {
                Message m = (Message) packet;
                String strmsg = m.getBody();
                Object objmsg = JsonUtil.toObject(strmsg);
                
                if( objmsg instanceof Map ) 
                {
                    Map msg = (Map) objmsg;
                    String msgOrigin = (String) msg.get("originName");
                    if( originName != null && !originName.equals(msgOrigin) ) return;
                    
                    getScriptHandler().onMessage(objmsg);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean accept(Packet packet) {
        return (packet instanceof Message);
    }
    
    //helper interface
    interface ScriptHandler {
        void onMessage(Object o);
    }
    
    private ScriptHandler handler;
    
    private ScriptHandler getScriptHandler() {
        if( handler != null ) return handler;
        
        ScriptServiceContext ctx = new ScriptServiceContext(conf);
        return (handler = (ScriptHandler) ctx.create( serviceName, ScriptHandler.class ));
    }
}
