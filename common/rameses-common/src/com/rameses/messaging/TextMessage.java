/*
 * SimpleMessage.java
 *
 * Created on August 10, 2010, 8:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.messaging;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public class TextMessage extends AbstractMessage {
    
    private String body;
    
    /** Creates a new instance of SimpleMessage */
    public TextMessage() {
    }

    public String getType() {
        return "text";
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    
}
