/*
 * MachineInfo.java
 */

package com.rameses.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        int c;
        while ((c = stdoutStream.read()) != -1) {
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
                throw new RuntimeException("Machine with " + osName + " not supported");
            instance.os = osName;    
        }
        return instance;
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="IMPLEMENTATIONS">
    public static class WindowsMachineInfo extends MachineInfo {
        private String macAddress;
        
        public String getMacAddress() throws Exception {
            if(macAddress != null) return macAddress;
            // modified : jzamora
            // date     : 2011-12-02
            // prioritize IPConfig result
            try {
                macAddress = getMacFromIPConfig();
                if( macAddress != null ) return macAddress;
            }
            catch( Exception e ) {
                // ignore, try getting macaddress using getmac
            }
                    
            try {
                Process p = Runtime.getRuntime().exec( "getmac /fo table /nh" );
                BufferedReader br = new BufferedReader((new InputStreamReader(p.getInputStream())));
                String line;
                
                //it returns the first macaddress found
                while((line=br.readLine())!=null) {
                    if(line.trim().length()>0) {
                        macAddress = line.split("\\s")[0].trim();
                        return macAddress;
                    }
                }
            }
            //if getmac is not available use the ipconfig /all command on windows
            catch(Exception ioe) {
                //
            }
            
            throw new ParseException("cannot read MAC address ", 0);
        }
        
        private String getMacFromIPConfig() {
            try {
                String response = getProcessResponse("ipconfig /all");
                StringTokenizer tokenizer = new StringTokenizer(response, "\n");

                int counter = 1;
                boolean skipMac = false;
                Pattern macPattern = Pattern.compile("(?:\\w{2}-){5}\\w{2}");

                //returns the first non-disconnected macaddress
                while(tokenizer.hasMoreTokens()) {
                    String line = tokenizer.nextToken().trim();

                    if ( line.toLowerCase().contains("disconnected") ) skipMac = true;

                    Matcher m = macPattern.matcher(line);
                    if( m.find() ) {
                        if( !skipMac ) {
                            macAddress = m.group();
                            return macAddress;
                        }
                        skipMac = false;
                    }
                }
            }
            catch( Exception e ) {
                //
            }
            return null;
        }
    }
    
    
    public static class LinuxMachineInfo extends MachineInfo {
        private String macAddress;
        public String getMacAddress() throws Exception {
            //better suggested information.
            //ifconfig -a | grep HWAddr | awk '{print $1 "\t"$5}'
            //ifconfig -a eth0 | grep HWAddr | sed '/^.*HWAddr */!d; s///;q'
            
            
            if(macAddress!=null) return macAddress;
            String localHost = getLocalHost();
            String ipConfigResponse = null;
            
            try {
                ipConfigResponse = getProcessResponse("ifconfig");
            }
            //in Red Hat versions, sometimes it is /sbin/ifconfig
            catch(IOException ioe) {
                ipConfigResponse = getProcessResponse("/sbin/ifconfig");
            }
            
            StringTokenizer tokenizer = new StringTokenizer(ipConfigResponse, "\n");
            String lastMacAddress = null;
            
            while( tokenizer.hasMoreTokens() ) {
                String line = tokenizer.nextToken().trim();
                
                // see if line contains MAC address
                int macAddressPosition = line.indexOf("HWaddr");
                if(macAddressPosition <= 0) continue;
                
                String macAddressCandidate = line.substring(macAddressPosition + 6).trim();
                // TODO: use a smart regular expression
                if ( macAddressCandidate.length() == 17 ) {
                    macAddress = macAddressCandidate;
                    return macAddress;
                }
            }
            
            throw new ParseException("cannot read MAC address for " + localHost + " from [" + ipConfigResponse + "]", 0);
        }
    }
    
    public static class MacMachineInfo extends MachineInfo {
        private String macAddress;
        
        public String getMacAddress() throws Exception {
            //use this much better
            //ifconfig -a | grep ether | awk '(print $2}'
            
            if(macAddress!=null) return macAddress;
            
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
                if ( macAddressCandidate.length() == 17 ) {
                    macAddress = macAddressCandidate;
                    return macAddress;
                }
            }
            throw new ParseException("cannot read MAC address for " + localHost + " from [" + ipConfigResponse + "]", 0);
        }
        
    }
//</editor-fold>
    
    
    
}
