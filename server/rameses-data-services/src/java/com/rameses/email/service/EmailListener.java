package com.rameses.email.service;

import java.util.Date;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.SessionContext;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

//@MessageDriven(
//    activationConfig= {
//        @ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
//        @ActivationConfigProperty(propertyName="destination", propertyValue="queue/testQueue"),
//        @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge"),
//        @ActivationConfigProperty(propertyName="providerAdapterJNDI", propertyValue="java:/DefaultJMSProvider"),
//        @ActivationConfigProperty(propertyName="subscriptionDurability", propertyValue="NonDurable")
//    }
//)
public class EmailListener implements javax.jms.MessageListener {
    
//    @Resource
//    private SessionContext ctx;
    
    public void onMessage(Message message) {
//        try {
//            System.out.println("Message JSM Type " + message.getJMSType());
//            if(message.getJMSType().equals("EMAIL")){
//                ObjectMessage ob = (ObjectMessage) message;
//                Map map = (Map) ob.getObject();
//                javax.mail.Session ses = (javax.mail.Session) ctx.lookup((String) map.get("service"));
//                javax.mail.Message msg = new MimeMessage(ses);
//                EmailServiceHelper.buildMessage( msg, map );
//                msg.setSentDate(new Date());
//                Transport.send(msg);
//            }
//        } 
//        catch(Exception ex) {
//           ex.printStackTrace();
//           throw new IllegalStateException(ex.getMessage());
//        }
//            
    }
    
    
}
