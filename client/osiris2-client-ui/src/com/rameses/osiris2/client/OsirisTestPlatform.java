/*
 * OsirisTestPlatform.java
 *
 * Created on October 27, 2009, 5:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.rcp.impl.MainDialog;
import com.rameses.rcp.support.ResURLStreamHandlerFactory;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class OsirisTestPlatform {
    
    public static void runTest(Map map) throws Exception {
        URL.setURLStreamHandlerFactory(new ResURLStreamHandlerFactory(Thread.currentThread().getContextClassLoader()));
        MainDialog d = new MainDialog();
        OsirisAppLoader loader = new OsirisAppLoader();
        loader.load(Thread.currentThread().getContextClassLoader(), map, d);
        d.show();
    }
    
    
}
