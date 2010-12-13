/*
 * HttpResourceServlet.java
 *
 * Created on December 6, 2010, 4:26 PM
 */

package com.rameses.image.server;

import com.rameses.io.FileTransferInfo;
import com.rameses.util.ExprUtil;
import com.rameses.util.SysMap;
import com.rameses.util.ValueUtil;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.naming.InitialContext;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.fileupload.FileItem;

/**
 *
 * @author jaycverg
 */
public class HttpResourceServlet extends HttpServlet {
    
    private static final String RES_DIR = "META-INF/resources/";
    private static final String RES_EXT = ".conf";
    
    private Logger logger;
    
    
    public void init() throws ServletException {
        logger = Logger.getLogger(HttpResourceServlet.class.getName());
    }
    
    //<editor-fold defaultstate="collapsed" desc="  get request support  ">
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        Properties resConf = getConf( path );
        
        if ( resConf == null || resConf.get("type") == null || !resConf.get("type").equals("get") ) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, path);
            return;
        }
        
        try {
            Map params = new HashMap();
            params.put("resourceId", resConf.getProperty("resourceId"));
            
            Object svcResult = invoke(resConf, params);
            OutputStream output = null;
            try {
                output = response.getOutputStream();
                if ( svcResult instanceof Map ) {
                    Map result = (Map) svcResult;
                    
                    //add header info if fileInfo is on the svcResult
                    if ( result.get("fileInfo") != null ) {
                        FileTransferInfo info = (FileTransferInfo) result.get("fileInfo");
                        response.setContentLength( (int) info.getSize() );
                        String mimeType = request.getSession().getServletContext().getMimeType( info.getName() );
                        response.setContentType( mimeType );
                        response.addHeader("Content-Disposition", "inline; filename=" + info.getName());
                    }
                    
                    //apply headers if specified on the svcResult
                    if ( result.get("headers") != null ) {
                        Map headers = (Map) result.remove("headers");
                        applyHeaders(response, headers);
                    }
                    
                    byte[] data = (byte[]) result.remove("data");
                    output.write( data );
                    
                    String status = (String) result.remove("status");
                    while( status != null && status.equals("partial") ) {
                        //reset params
                        params.clear();
                        params.put("resourceId", resConf.getProperty("resourceId"));
                        if ( !result.isEmpty() ) {
                            params.putAll( result );
                        }
                        
                        result = (Map) invoke(resConf, params);
                        if ( result == null ) break;
                        
                        data = (byte[]) result.remove("data");
                        if ( data != null ) output.write( data );
                        
                        status = (String) result.remove("status");
                    }
                    
                } else if ( svcResult instanceof byte[] ) {
                    byte[] data = (byte[]) svcResult;
                    response.setContentLength( data.length );
                    output.write( data );
                }
                output.flush();
                
            } catch(Exception e) {
                response.sendError(HttpServletResponse.SC_FOUND, path);
            } finally {
                try { output.close(); }catch(Exception ign){;}
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }
    
    private void applyHeaders(HttpServletResponse httpResp, Map headers) {
        for(Map.Entry me : (Set<Map.Entry>)headers.entrySet()) {
            if ( me.getKey() != null && me.getValue() != null ) {
                httpResp.addHeader(me.getKey().toString(), me.getValue().toString());
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  post request support  ">
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        Properties resConf = getConf( path );
        
        if ( resConf == null || resConf.get("type") == null || !resConf.get("type").equals("post") ) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, path);
            return;
        }
        
        if ( !(request instanceof HttpMultipartRequestWrapper) ) {
            response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE, "Multipart content-type is expected.");
            return;
        }
        
        HttpMultipartRequestWrapper multiReq = (HttpMultipartRequestWrapper) request;
        
        OutputStream output = null;
        try {
            int counter = 0;
            String action = resConf.getProperty("action");
            String location = resConf.getProperty("location");
            if ( location != null ) {
                location = ExprUtil.substituteValues(location, new SysMap());
            }
            for(FileItem item : multiReq.getFileParameters()) {
                if ( action == null || action.equals("send") ) {
                    sendItem(item, resConf);
                } else if ( action.equals("store") && location != null ) {
                    item.write( new File(location, item.getName()) );
                }
            }
            
            response.setStatus(200);
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } finally {
            try { output.close(); }catch(Exception e){}
        }
    }
    
    private void sendItem(FileItem item, Properties resConf) throws Exception {
        Map params = new HashMap();
        params.put("filename", item.getName());
        params.put("size", item.getSize());
        params.put("contentType", item.getContentType());
        
        //small files are just stored in memory
        if ( item.isInMemory() ) {
            params.put("data", item.get());
            invoke(resConf, params);
        } 
        //large files are stored on disk
        else {
            BufferedInputStream bis = null;
            try {
                int buffSize = 0;
                if ( !ValueUtil.isEmpty(resConf.get("bufferSize")) ) {
                    buffSize = Integer.parseInt( resConf.get("bufferSize").toString() );
                } else {
                    buffSize = 1024*8;
                }
                
                bis = new BufferedInputStream(item.getInputStream(), buffSize);
                byte []buff = new byte[buffSize];
                int bytesRead = -1;
                while( (bytesRead = bis.read(buff)) != -1 ) {
                    params.put("data", buff);
                    invoke(resConf, params);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                try { bis.close(); }catch(Exception e){}
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    /**
     * expected result value could be a Map or a byte[]
     * if the result is a Map:
     *  expected fields:
     *    data:     byte[]
     *    status:   completed, partial
     *    fileInfo: (optional) this field is sent back
     *              to the service incase of partial response data
     */
    private Object invoke(Properties resConf, Map svcParam) throws Exception {
        Object scriptSvc = resConf.get("__scriptService");
        if ( scriptSvc == null ) {
            String appContext = resConf.getProperty("app.context") + "/";
            InitialContext ctx = new InitialContext();
            scriptSvc = ctx.lookup(appContext + "ScriptService/local");
            resConf.put("__scriptService", scriptSvc);
        }
        
        Object []params = new Object[]{
            resConf.getProperty("service"), //service name
            resConf.getProperty("method"),  //service method
            new Object[]{ svcParam },
            new HashMap()
        };
        
        return MethodUtils.invokeMethod(scriptSvc, "invoke", params);
    }
    
    private Properties getConf(String path) throws IOException {
        if ( path == null ) return null;
        
        String[] pathArr = path.toString().split("/");
        if ( pathArr.length < 2 ) return null;
        
        String confId = pathArr[1];
        
        confId = RES_DIR + confId + RES_EXT;
        InputStream res = Thread.currentThread().getContextClassLoader().getResourceAsStream( confId );
        if ( res == null ) return null;
        
        Properties resConf = new Properties();
        resConf.load( res );
        
        if ( pathArr.length >= 3 )
            resConf.put("resourceId", pathArr[2]);
        
        return resConf;
    }
    //</editor-fold>
    
}
