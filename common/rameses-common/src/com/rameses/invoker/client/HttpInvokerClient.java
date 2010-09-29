package com.rameses.invoker.client;

import com.rameses.util.CipherUtil;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class HttpInvokerClient {
    
    private String protocol = "http";
    private String host = "localhost:8080";
    private boolean secured = false;
    
    private String[] hosts;
    private int timeout = 10000;
    
    HttpInvokerClient() {
        
    }
    
    public String buildHostPath(String host) {
        StringBuffer sb = new StringBuffer();
        sb.append( getProtocol() + "://" + host.trim() + "/invoke/");
        if( isSecured() ) sb.append( "secured/");
        return sb.toString();
    }
    
    
    private Object[] filterData( Object[] params ) throws Exception {
        if(params == null ) params = new Object[]{};
        if( secured )
            return (Object[])CipherUtil.encode( (Serializable)params);
        else
            return params;
    }
    
    
    public  Object invoke(String serviceName, Object[] params) throws Exception {
        //determine first the no. of round robins.
        int rounds = getHosts().length;
        int currentRound = 0;
        Object retval = null;
        
        //check first if we need to rearrange the host order.
        if( !host.equals( getHosts()[0]) ) {
            List plist = new ArrayList();
            List nlist = new ArrayList();
            boolean t = false;
            for( int i=0; i<getHosts().length;i++) {
                String ss = getHosts()[i];
                if( host.equals( ss ) || t == true ){
                    t = true;
                    nlist.add( ss );
                } else {
                    plist.add(ss);
                }
            }
            nlist.addAll( plist );
            hosts = (String[])nlist.toArray(new String[]{});
        }
        
        
        while( currentRound < rounds ) {
            HttpURLConnection conn = null;
            ObjectOutputStream out = null;
            ObjectInputStream in = null;
            try {
                Object[] data = filterData( params );
                
                StringBuffer urlHost = new StringBuffer(buildHostPath(host));
                if (serviceName != null && serviceName.trim().length() > 0)
                    urlHost.append(serviceName);
                
                URL url = new URL(urlHost.toString());
                
                conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(timeout);
                conn.setReadTimeout(timeout);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                
                out = new ObjectOutputStream(conn.getOutputStream());
                
                out.writeObject(data);
                in = new ObjectInputStream(conn.getInputStream());
                retval =  in.readObject();
                if( (retval instanceof String) && retval.equals("#NULL")  ) {
                    retval = null;
                }
                if( retval instanceof Exception ) 
                    throw (Exception)retval;
                
                break;
            } catch(UnknownHostException uhe) {
                currentRound++;
                if( currentRound >= rounds ) {
                    throw uhe;
                } else {
                    host = getHosts()[currentRound];
                }
            } catch(ConnectException ce) {
                currentRound++;
                if( currentRound >= rounds ) {
                    throw ce;
                } else {
                    host = getHosts()[currentRound];
                }
            } catch(SocketTimeoutException se) {
                currentRound++;
                if( currentRound >= rounds ) {
                    throw se;
                } else {
                    host = getHosts()[currentRound];
                }
            } catch (Exception ex) {
                throw ex;
            } finally {
                if (out != null) try { out.close(); } catch (Exception ign){;}
                if (in != null) try { in.close(); } catch (Exception ign){;}
                if (conn != null) try { conn.disconnect(); } catch (Exception ign){;}
            }
        }
        return retval;
    }
    
   
    
    
    // <editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public String getProtocol() {
        return protocol;
    }
    
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public boolean isSecured() {
        return secured;
    }
    
    public void setSecured(boolean secured) {
        this.secured = secured;
    }
    //</editor-fold>
    
    public String[] getHosts() {
        return hosts;
    }
    
    public void setHosts(String[] hosts) {
        this.hosts = hosts;
    }
    
    public int getTimeout() {
        return timeout;
    }
    
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
}
