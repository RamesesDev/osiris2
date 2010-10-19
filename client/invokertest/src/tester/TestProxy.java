/*
 * InvokerProxy.java
 *
 * Created on June 28, 2009, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tester;

import com.rameses.invoker.client.HttpInvokerClient;
import com.rameses.invoker.client.HttpClientManager;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TestProxy {
    
    
    private final static String INVOKER = "ScriptService";
    private Map<String, Class> map = new Hashtable<String, Class>();
    private Map env = new HashMap();
    private GroovyClassLoader loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
    private HttpClientManager manager = new HttpClientManager();
    
    public TestProxy() {;}
    
    public TestProxy(Map env) {
        this.env = env;
    }

    public Object create( String name ) throws Exception {
        return create(name, null);
    }
    
    public Object create( String name, String hostKey) throws Exception {
        HttpInvokerClient client = manager.getService(hostKey, env);
        Class clazz = null;
        if(! map.containsKey(name)) {
            byte[] b = (byte[])client.invoke(INVOKER+".getScriptInfo", new Object[]{name});
            InputStream is = new ByteArrayInputStream(b);
            clazz = loader.parseClass(is);
            map.put(name, clazz);
        }
        clazz = map.get(name);
        return Proxy.newProxyInstance(loader, new Class[]{clazz}, new MyHandler(name, client));
    }
    
    public class MyHandler implements InvocationHandler {
        
        private HttpInvokerClient client;
        private String serviceName;
        private String machineKey;
        
        public MyHandler(String serviceName, HttpInvokerClient client) {
            this.serviceName = serviceName;
            this.client = client;
        }
        
        public Object invoke(Object object, Method method, Object[] args) throws Throwable {
            if( method.getName().equals("toString")) return serviceName;
            try {
                return client.invoke( INVOKER+".invoke", new Object[]{ serviceName, method.getName(), args, env } );
            }
            catch(Exception e) {
                System.out.println("ERROR CLASS " + e.getClass().getName());
                e.printStackTrace();
                throw e;
            }
        }
    }
    
    public void reset() {
        map.clear();
        manager = new HttpClientManager();
    }
    
}
