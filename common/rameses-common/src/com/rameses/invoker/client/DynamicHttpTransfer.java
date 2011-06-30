/*
 * DynamicHttpTransfer.java
 * Created on June 30, 2011, 9:45 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.client;

import com.rameses.io.FileTransfer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 * Notes
 * status = check flag if this is the first batch.
 */
public final class DynamicHttpTransfer {
    
    public static class In implements FileTransfer.InputSource {
        private DynamicHttpInvoker.Action action;
        private String method;
        private Map parameters = new HashMap();
        private int batch = 0;
        private int bufferSize = 1024*8;
        private long start = 0;
        
        public In(String host, String appContext, String serviceName, String method) {
            this(host,appContext,serviceName, method, 1024*8);
        }    

        public In(String host, String appContext, String serviceName, String method, int bufferSize) {
             DynamicHttpInvoker invoker = new DynamicHttpInvoker(host,appContext);
             action = invoker.create(serviceName);
             this.method = method;
             this.bufferSize = bufferSize;
        }    
            
        public byte[] read() throws Exception {
            Map map = getParameters();
            map.put("_size", bufferSize);
            map.put("_start", start);
            map.put("_batch", batch);
            byte[] bytes= (byte[])action.invoke(method, new Object[]{map}) ;
            start = start + bytes.length;
            batch = batch + 1;
            return bytes;
        }

        public void close() {
            //do nothing
        }

        public long getPosition() {
            return start;
        }

        public Map getParameters() {
            return parameters;
        }

        public void setParameters(Map parameters) {
            this.parameters = parameters;
        }
    }
    
    public static class Out implements FileTransfer.OutputHandler {
        private DynamicHttpInvoker.Action action;
        private String method;
        private Map parameters = new HashMap();
        private int batch = 0;
        
        public Out(String host, String appContext, String serviceName, String method) {
             DynamicHttpInvoker invoker = new DynamicHttpInvoker(host,appContext);
             action = invoker.create(serviceName);
             this.method = method;
        }        
            
        public void write(byte[] bytes) throws Exception {
            Map map = getParameters();
            map.put("_batch", batch );
            map.put("_data", bytes);
            batch = batch+1;
            action.invoke(method, new Object[]{map});
        }
        
        public void close() {
        }

        public Map getParameters() {
            return parameters;
        }

        public void setParameters(Map parameters) {
            this.parameters = parameters;
        }
    }
    
}
