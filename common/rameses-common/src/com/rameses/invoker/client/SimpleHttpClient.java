/*
 * HttpSimpleClient.java
 * Created on July 27, 2011, 9:55 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.client;

import com.rameses.io.StreamUtil;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * A very simple wrapper for URL operations
 * requirements:
 *    host = example: http://10.0.0.101:8080/context
 *    action = any method after the host and context.
 *    parameters = Map key-value pair converted to string.
 *    Object result = must be filtered.
 */
public class SimpleHttpClient {
    
    private String host;
    private int timeout = 10000;
    private int readTimeout = -1;
    
    
    public SimpleHttpClient(String host) {
        this.host = host;
    }
    
    public String post(String path, Map<String,String> params) throws Exception {
        return invoke(path, params, "POST");
    }
    
    public String get(String path, Map<String,String> params) throws Exception {
        return invoke(path, params, "GET");
    }
    
    public String post(String path) throws Exception {
        return invoke(path, null, "POST");
    }
    
    public String get(String path) throws Exception {
        return invoke(path, null, "GET");
    }

    private String invoke(String path, Map<String,String> params, String methodType) throws Exception{
        HttpURLConnection conn = null;
        InputStream is = null;
        try {
            URL url = new URL(host+"/"+path);
            conn = (HttpURLConnection) url.openConnection();
            if( timeout > 0 ) {
                conn.setConnectTimeout(timeout);
            }
            if( readTimeout > 0 ) {
                conn.setReadTimeout(readTimeout);
            }
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(methodType);
            if(params!=null) {
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                Iterator<String> iter = params.keySet().iterator();
                boolean b = false;
                while(iter.hasNext()) {
                    String key = iter.next();
                    if(!b) {
                        b = true;
                    }
                    else {
                        writer.write("&");
                    }
                    writer.write( key + "=" + URLEncoder.encode( params.get(key) ));
                    //conn.setRequestProperty(key, params.get(key));
                }
                writer.flush();
            }
            try {
                is = conn.getInputStream();
                StringBuilder builder = new StringBuilder();
                int i = 0;
                while( (i=is.read()) !=-1 ) {
                    builder.append((char)i);
                }
                return builder.toString();
             } catch(Exception e) {
                throw new Exception(StreamUtil.toString( conn.getErrorStream() ));
            }
        } catch (Exception ex) {
                throw ex;
        } finally {
            if (conn != null) try { conn.disconnect(); } catch (Exception ign){;}
            if (is != null) try { is.close(); } catch(Exception ign){;}
        }
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
    
}
