/*
 * SqlResourceProvider.java
 *
 * Created on July 24, 2010, 9:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.interfaces.ResourceHandler;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

/**
 *
 * @author elmo
 */
public class ConfResourceProvider extends AbstractResourceProvider {
    
    public String getName() {
        return "conf";
    }

    public String getDescription() {
        return "Conf Resource Provider [conf://]";
    }

    public int getPriority() {
        return 0;
    }

    public boolean accept(String nameSpace) {
        return nameSpace.equals("conf");
    }

    public InputStream getResource(String name) throws Exception {
        String fileName = "META-INF/conf/" + name + ".conf";
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    public void scanResources(String name, ResourceHandler handler) throws Exception {
        if(name.equals("vars")) {
            Enumeration<URL> e = Thread.currentThread().getContextClassLoader().getResources("META-INF/global.conf");
            while(e.hasMoreElements()) {
                InputStream is = null;
                try {
                    URL u = e.nextElement();
                    is = u.openStream();
                    handler.handle(is, u.getPath());
                }
                catch(Exception ig) {
                    System.out.println(ig.getMessage());
                }
                finally {
                    try {is.close();} catch(Exception ign){;}
                }
            }
        }
    }
    
    

}
