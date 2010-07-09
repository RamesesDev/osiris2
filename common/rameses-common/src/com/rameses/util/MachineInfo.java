/*
 * MachineInfo.java
 *
 * Created on June 2, 2010, 2:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * to get system profile in mac = /usr/sbin/system_profiler
 * in linux = lshw
 */

public abstract class MachineInfo {
    
    private static MachineInfo instance;
    
    private Map info = new HashMap();
    private String os;
    
    public MachineInfo() {
    }
    
    public Map getInfo() {
        return info;
    }
    
    public String getOs() {
        return os;
    }
    
    public abstract String getMacAddress() throws Exception;
    
    private static String getProcessResponse(String command) throws Exception
    {
        Process p = Runtime.getRuntime().exec(command);
        InputStream stdoutStream = new BufferedInputStream(p.getInputStream());
        StringBuffer buffer= new StringBuffer();
        for (;;) {
            int c = stdoutStream.read();
            if (c == -1) break;
            buffer.append((char)c);
        }
        
        String outputText = buffer.toString();
        stdoutStream.close();
        return outputText;
    }
    
    // <editor-fold defaultstate="collapsed" desc="HELPER METHODS">
    private static String getLocalHost() throws ParseException{
        String localHost = null;
        try {
            localHost = InetAddress.getLocalHost().getHostAddress();
        } catch(java.net.UnknownHostException ex) {
            ex.printStackTrace();
            throw new ParseException(ex.getMessage(), 0);
        }
        return localHost;
    }
    
    public static MachineInfo getInstance() {
        if(instance==null) {
            String osName = System.getProperty("os.name").toLowerCase();
            if(osName.indexOf("windows") >= 0) {
                instance = new WindowsMachineInfo();
            } else if(osName.indexOf("linux") >= 0) {
                instance = new LinuxMachineInfo();
                
            } else if(osName.indexOf("mac") >= 0) {
                instance = new MacMachineInfo();
            }
            
            if(instance==null)
                throw new IllegalStateException("Machine with " + osName + " not supported");
            instance.os = osName;    
        }
        return instance;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="IMPLEMENTATIONS">
    public static class WindowsMachineInfo extends MachineInfo 
    {
        public String getMacAddress() throws Exception 
        {
            String response = getProcessResponse("getmac");
            StringTokenizer tokenizer = new StringTokenizer(response, "\n");
  
            int counter = 1;
            while(tokenizer.hasMoreTokens()) 
            {
                String line = tokenizer.nextToken().trim();
                if (counter > 3)
                {
                   
                    if (line.toLowerCase().indexOf("disconnected") < 0)
                    {
                        int idx = line.indexOf(' ');
                        String macAddressCandidate = line.substring(0, idx).trim();
                        
                        // TODO: use a smart regular expression
                        if ( macAddressCandidate.length() == 17 ) 
                            return macAddressCandidate;
                    }
                }
                counter++;
            }
            
            throw new ParseException("cannot read MAC address from [" + response + "]", 0);
        }        
        
        /*
        public String getMacAddress() throws Exception 
        {
            String localHost = getLocalHost();
            String ipConfigResponse = getProcessResponse("ipconfig");
            StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
            String lastMacAddress = null;
            
            while(tokenizer.hasMoreTokens()) {
                String line = tokenizer.nextToken().trim();
                
                // see if line contains MAC address
                int macAddressPosition = line.indexOf(":");
                if(macAddressPosition <= 0) continue;
                
                String macAddressCandidate = line.substring(macAddressPosition + 1).trim();
                // TODO: use a smart regular expression
                if ( macAddressCandidate.length() == 17 ) return macAddressCandidate;
            }
            
            throw new ParseException("cannot read MAC address from [" + ipConfigResponse + "]", 0);
        }
         */
    }
    
    
    public static class LinuxMachineInfo extends MachineInfo {
        public String getMacAddress() throws Exception {
            String localHost = getLocalHost();
            String ipConfigResponse = getProcessResponse("ifconfig");
            StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
            String lastMacAddress = null;
            
            while( tokenizer.hasMoreTokens() ) {
                String line = tokenizer.nextToken().trim();
                
                // see if line contains MAC address
                int macAddressPosition = line.indexOf("HWaddr");
                if(macAddressPosition <= 0) continue;
                
                String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
                // TODO: use a smart regular expression
                if ( macAddressCandidate.length() == 17 ) return macAddressCandidate;
            }
            
            throw new ParseException("cannot read MAC address for " + localHost + " from [" + ipConfigResponse + "]", 0);
        }
    }
    
    public static class MacMachineInfo extends MachineInfo {
        public String getMacAddress() throws Exception {
            String localHost = getLocalHost();
            String ipConfigResponse = getProcessResponse("ifconfig");
            StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
            String lastMacAddress = null;
            while(tokenizer.hasMoreTokens()) {
                String line = tokenizer.nextToken().trim();
                
                // see if line contains MAC address
                int macAddressPosition = line.indexOf("ether ");
                if(macAddressPosition < 0) continue;
                
                String macAddressCandidate = line.substring(macAddressPosition + 5).trim();
                // TODO: use a smart regular expression
                if ( macAddressCandidate.length() == 17 ) return macAddressCandidate;
            }
            throw new ParseException("cannot read MAC address for " + localHost + " from [" + ipConfigResponse + "]", 0);
        }
        
    }
//</editor-fold>
    
    
    
}
