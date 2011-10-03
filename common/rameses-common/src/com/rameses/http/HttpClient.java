/*
 * HttpClient.java
 * Created on September 19, 2011, 8:52 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */
package com.rameses.http;

import com.rameses.util.SealedMessage;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * This is a generic utility for accessing servlets in a service style manner
 * To use pass the hosts (including ports) list in the constructor. Hosts can be separated
 * by a semicolon so that it would act as a failover. This is not to be used as HA
 * Example usage:
 *
 * HttpClient c = new HttpClient("localhost:8080;10.0.0.151:8080;10.0.0.152:8080", true);
 * Map map = new HashMap();
 * map.put("id", "12345" );
 * c.get( "osiris2/get" );
 * c.post( "send", map );                                                 //simple post to any website
 * c.post( "ejb-secured/appcontext/SessionService/local.fire", map )   //access the service invoker securely
 * c.post( "ejb/appcontext/SessionService/local.fire", map );
 * map contains env and args
 */
public class HttpClient implements Serializable {
    
   
    
    private int connectionTimeout = 5000;
    private int readTimeout =  5000;    //default read timeout at 5 seconds
    private HttpClientOutputHandler outputHandler;
    private String protocol = "http";
    private String appContext;
    
    //if true, parameters will be written in the outputstream as an object.
    private boolean postAsObject = false;
    
    private String[] hosts;
    
    //encrypted is applicable only if postAsObject is true.
    //by default the transfer type is encrypted
    private boolean encrypted = true;
    
    public HttpClient(String host) {
        this( host, false );
    }
    
    public HttpClient(String host, boolean postAsObject) {
        this.hosts = host.split(";");
        this.postAsObject = postAsObject;
    }
    
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }
    
    public int getReadTimeout() {
        return readTimeout;
    }
    
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }
    
    public void setOutputHandler(HttpClientOutputHandler h) {
        this.outputHandler = h;
    }
    public String getAppContext() {
        return appContext;
    }
    
    public void setAppContext(String appContext) {
        this.appContext = appContext;
    }
    
    /**********************************************
     * //GET METHODS
     *********************************************/
    public Object get() throws Exception {
        return get(null, null);
    }
    
    public Object get(String path) throws Exception {
        return get(path, null);
    }
    
    public Object get(String path, Map params) throws Exception {
        String parms = "";
        if( params !=null) parms = "?"+HttpClientUtils.stringifyParameters(params);
        LinkedList list = new LinkedList();
        for(String s: hosts) {
            String ctx = (appContext!=null&&appContext.trim().length()>0) ? "/"+appContext : "";
            String p = (path!=null && path.trim().length()>0) ? "/" + path : "";
            list.add( protocol + "://" + s + ctx + p + parms );
        }
        return invoke(list, null, "GET");
    }
    
    /**********************************************
     * //POST METHODS
     *********************************************/
    public Object post(Map params) throws Exception {
        return post(null, params);
    }
    
    public Object post(String path) throws Exception {
        return post(path, null);
    }
    
    public Object post(String path, Object args) throws Exception {
        LinkedList list = new LinkedList();
        for(String s: hosts) {
            String ctx = (appContext!=null&&appContext.trim().length()>0) ? "/"+appContext : "";
            String p = (path!=null && path.trim().length()>0) ? "/" + path : "";
            list.add( protocol + "://" +  s + ctx + p );
        }
        if( !postAsObject && (args instanceof Map) ) {
            args = HttpClientUtils.stringifyParameters( (Map)args);
        }
        return invoke(list, args, "POST");
    }
    
    private Object invoke(Queue<String> queue, Object parms, String methodType) throws Exception {
        HttpURLConnection conn = null;
        InputStream is = null;
        ObjectInputStream in = null;
        ObjectOutputStream out = null;
        String uhost = null;
        try {
            uhost = queue.poll();
            if( uhost == null )
                throw new AllConnectionFailed();
            
            URL url = new URL(uhost);
            conn = (HttpURLConnection) url.openConnection();
            if( connectionTimeout > 0 ) conn.setConnectTimeout(connectionTimeout);
            if( readTimeout > 0 ) conn.setReadTimeout(readTimeout);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(methodType);
            
            boolean _asObject = postAsObject;
            if(!_asObject && parms==null) _asObject = true;
            if(!_asObject && !(parms instanceof String)) _asObject = true;            
            
            if(_asObject) {
                conn.setRequestProperty( "CONTENT-TYPE",HttpConstants.APP_CONTENT_TYPE);
                out = new ObjectOutputStream(conn.getOutputStream());
                if(encrypted) {
                    parms = new SealedMessage(parms);
                }
                if(parms!=null) out.writeObject( parms );
                out.flush();
            } else if(methodType.equalsIgnoreCase("POST")) {
                conn.setRequestProperty( "CONTENT-TYPE", "application/x-www-form-urlencoded");
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write( (String)parms );
                writer.flush();
            }
            
            //read the input stream. we cannot use this
            try {
                is = conn.getInputStream();
            } catch(Exception e) {
                InputStream es = conn.getErrorStream();
                if( es != null ) {
                    throw new ResponseError(conn.getResponseCode(), conn.getResponseMessage());
                }    
                else
                    throw e;
            }
            
            if( outputHandler ==null ) {
                Object retval = null;
                if(is!=null) {
                    String t = conn.getContentType();
                    if(t!=null && t.startsWith("text")) {
                        StringBuilder b = new StringBuilder();
                        int i = 0;
                        while((i=is.read())!=-1) {
                            b.append((char)i);
                        }
                        retval = b.toString();
                    } else {
                        try {
                            in = new ObjectInputStream(is);
                            retval =  in.readObject();
                        } catch(Exception ign){;}
                    }
                }
                
               //check first if the result is sealed. If true, unseal it  
                if(retval!=null && (retval instanceof SealedMessage)) {
                    SealedMessage sm = (SealedMessage)retval;
                    retval = sm.getMessage();
                }
                
                if(retval==null) {
                    //do nothing
                } else if( (retval instanceof String) && retval.equals("#NULL")  ) {
                    retval = null;
                } else if( retval instanceof Exception )
                    throw (Exception)retval;
                
                return retval;
            } else {
                return outputHandler.getResult(is);
            }
            
        } catch (Exception ex) {
            if( (ex instanceof UnknownHostException)
            || (ex instanceof SocketException)
            || (ex instanceof ConnectException)
            || (ex instanceof SocketTimeoutException)){
            //|| (ex instanceof IOException )) {
                //find the next host
                try {
                    return invoke(queue, parms, methodType );
                } catch(AllConnectionFailed ae) {
                    throw ex;
                } catch(Exception se) {
                    throw se;
                }
            } else {
                throw ex;
            }
        } finally {
            if (in != null) try { in.close(); } catch(Exception ign){;}
            if (is != null) try { is.close(); } catch(Exception ign){;}
            if (out != null) try { out.close(); } catch(Exception ign){;}
            if (conn != null) try { conn.disconnect(); } catch (Exception ign){;}
        }
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }
    
    
    
    
    
    
}
