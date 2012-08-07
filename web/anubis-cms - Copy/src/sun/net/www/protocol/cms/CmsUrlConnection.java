/*
 * DBUrlConnection.java
 *
 * Created on June 13, 2012, 9:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package sun.net.www.protocol.cms;

import com.rameses.service.ScriptServiceContext;
import com.rameses.util.ExceptionManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class CmsUrlConnection extends URLConnection {
    
    private CmsURLService service;
    
    /** Creates a new instance of DBUrlConnection */
    public CmsUrlConnection(URL u) {
        super(u);
    }
    
    public void connect() throws IOException {
        if( service == null ) {
            try {
                String appHost = System.getProperty("cms.app.host");
                String appContext = System.getProperty("cms.app.context");
                if(appHost==null) {
                    throw new IOException( "Please provide a system entry in cms.app.host");
                }
                if(appContext==null) {
                    throw new IOException( "Please provide a system entry in cms.app.context");
                }
                Map map = new HashMap();
                map.put( "app.context", appContext );
                map.put( "app.host", appHost );
                ScriptServiceContext ctx = new ScriptServiceContext(map);
                service = ctx.create(  CmsURLService.class.getSimpleName(), CmsURLService.class );
            } catch(Exception e) {
                Exception origEx = ExceptionManager.getInstance().getOriginal(e);
                throw new IOException(origEx.getMessage());
            }
        }
    }
    
    @Override
    public Object getContent() throws IOException {
        connect();
        return getInputStream();
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        connect();
        String file = super.getURL().getFile();
        byte[] arr = service.getContent( file );
        if( arr == null )
            throw new IOException("File " + file + " not found");
        return new ByteArrayInputStream(arr );
    }
}
