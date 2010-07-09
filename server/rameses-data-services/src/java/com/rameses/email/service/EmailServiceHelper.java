/*
 * EmailTemplateHelper.java
 *
 * Created on May 2, 2009, 10:15 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.email.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author elmo
 */
public final class EmailServiceHelper {
    
    public static InternetAddress[] createAddresses(String recipients){
        if(recipients == null)
            return null;
        String[] r = recipients.split(",");
        List<InternetAddress> a = new ArrayList<InternetAddress>();
        for(String s : r){
            try{
                a.add(new InternetAddress(s.trim()));
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        return a.toArray(new InternetAddress[]{});
    }
    
    private static boolean empty( Object val ) {
        if( val == null ) return true;
        if( val instanceof String ) return (((String)val).trim().length() == 0);
        return false;
    }
    
    /**
     * Template as follows:
     * from 
     * to
     * cc
     * bcc
     * subject
     * message 
     * attachments 
     */
    public static void buildMessage(javax.mail.Message msg , Map m) throws Exception {
       String from = (String)m.get("from");
       String to = (String)m.get("to");
       String cc = (String)m.get("cc");
       String bcc = (String)m.get("bcc");
       String replyto = (String)m.get("replyto");
       String subject = (String)m.get("subject");
       String message = (String)m.get("message");
       String messagetype = (String)m.get("message_type");
       String attachments = (String)m.get("attachments");
       
       if( !empty(from) ) {
           msg.setFrom( createAddresses(  from )[0] );
       }
       
       if( !empty(to)) {
           msg.addRecipients(javax.mail.Message.RecipientType.TO, createAddresses(  to ) );
       }
       
       if( !empty(cc)) {
           msg.addRecipients(javax.mail.Message.RecipientType.CC, createAddresses(  cc ) );
       }
       
       if( !empty(bcc)) {
           msg.addRecipients(javax.mail.Message.RecipientType.BCC, createAddresses(  bcc ) );
       }
       
       if( !empty(replyto)) {
           msg.setReplyTo( createAddresses( replyto ) );
       }
       
       if( !empty(subject)) {
           msg.setSubject( subject );
       }
       
       if( empty(messagetype)) {
           messagetype = "text/html";
       }
       
       if( !empty(message)) {
           msg.setContent( message, messagetype );
       }
       
    }
            
    
            
}
