/*
 * Handler.java
 *
 * Created on June 13, 2012, 9:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sun.net.www.protocol.cms;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 *
 * @author Elmo
 */
public class Handler extends URLStreamHandler  {
    
    /** Creates a new instance of Handler */
    public Handler() {
    }
    
    protected URLConnection openConnection(URL u)  throws IOException {
        return new CmsUrlConnection(u);
    }
    
}
