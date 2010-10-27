/*
 * TestProxy.java
 *
 * Created on October 24, 2010, 2:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tester;

import com.rameses.invoker.client.AbstractScriptServiceProxy;
import com.rameses.invoker.client.ResponseHandler;
import com.rameses.invoker.client.ScriptInterfaceProvider;
import groovy.lang.GroovyClassLoader;
import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 *
 * @author ms
 */
public class TestProxy extends AbstractScriptServiceProxy {

    private long timeout = 10000;
    private long delay = 2000;
    
    private GroovyClassLoader classLoader;
    private GroovyScriptInterfaceProvider interfaceProvider;
    
    public TestProxy(Map env) {
        super(env);
        classLoader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
        interfaceProvider = new GroovyScriptInterfaceProvider();
    }

    protected ScriptInterfaceProvider getScriptInterfaceProvider() {
        return interfaceProvider;
    }

    private class GroovyScriptInterfaceProvider extends ScriptInterfaceProvider {
        
        protected Class parseClass(byte[] bytes) {
            return classLoader.parseClass( new ByteArrayInputStream(bytes));
        }

        public ClassLoader getProxyClassLoader() {
            return classLoader;
        }
    }
    
    public void invokeLater(ResponseHandler handler) {
        Thread t = new Thread(new AsyncRunnable(handler));
        t.start();
    }

    private class AsyncRunnable implements Runnable {
        
        private long timeleft;
        private ResponseHandler handler;
        
        public AsyncRunnable(ResponseHandler h) {
            timeleft = timeout;
            handler = h;
        }
        
        public void run() {
            while(timeleft>0) {
                try {
                    //if there are results, reset the timer.
                    if( handler.execute() ) timeleft = timeout; 
                    Thread.sleep(delay );
                }
                catch(Exception e) {
                    //do nothing, let it die gracefully
                }
                timeleft = timeleft - delay;
            }
            System.out.println("finished");
        }
        
    }
    
    
}
