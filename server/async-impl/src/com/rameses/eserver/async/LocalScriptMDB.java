/*
 * ScriptMDB.java
 *
 * Created on October 20, 2010, 8:00 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver.async;

import com.rameses.eserver.AbstractScriptMDB;
import javax.jms.MessageListener;


public class LocalScriptMDB extends AbstractScriptMDB implements MessageListener {
    
    public boolean isRemote() {
        return false;
    }
    
}
