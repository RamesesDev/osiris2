/*
 * EmailService.java
 *
 * Created on May 2, 2009, 9:26 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.email.service;

import com.rameses.interfaces.TemplateServiceLocal;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.Map;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

@Stateless
@Local(EmailServiceLocal.class)
public class EmailService implements EmailServiceLocal {
     
//    @Resource(mappedName="java:/JmsXA")
//    private ConnectionFactory connFactory;
//    
//    @Resource(name="queue")
//    private Queue queue;
//
//    @EJB
//    private TemplateServiceLocal templateService;
    
    public void send( String template, Map params) {
//        try {
//            String tmp = (String) templateService.getTemplate(template, params);
//            ByteArrayInputStream bis = new ByteArrayInputStream( tmp.getBytes() );
//            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
//            EmailParseHandler mth = new EmailParseHandler();
//            parser.parse( bis, mth );
//            Map result = mth.getResult();
//            broadcast(result);
//        }
//        catch(Exception ex) {
//            ex.printStackTrace();
//        }
    }
    
    private void broadcast(Map map){
//        Connection conn = null;
//        Session session = null;
//        MessageProducer sender = null;
//        try{
//            conn = connFactory.createConnection();
//            session = conn.createSession(true, 0);
//            sender = session.createProducer(queue);
//            ObjectMessage ob = session.createObjectMessage();
//            ob.setJMSType("EMAIL");
//            ob.setObject((Serializable) map);
//            sender.send(ob);
//        } 
//        catch(Exception ex){
//            ex.printStackTrace();
//        } 
//        finally {
//            try { sender.close();} catch (JMSException ex) {}
//            try { session.close(); } catch (JMSException ex) {}
//            try { conn.close(); } catch (JMSException ex) { }
//        }
    }

    

    
}
