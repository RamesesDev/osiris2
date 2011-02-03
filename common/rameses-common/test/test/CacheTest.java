/*
 * IOTest.java
 * JUnit based test
 *
 * Created on July 20, 2010, 7:28 AM
 */

package test;

import bak.ManagedCache;
import javax.swing.JOptionPane;
import junit.framework.*;

/**
 *
 * @author ms
 */
public class CacheTest extends TestCase {
    
    public void testMap() throws Exception {
        MyHandler handler = new MyHandler();
        Cache.get
        
        System.out.println( Cache.getCache("mykey1", handler));
        System.out.println( Cache.getCache("mykey2", handler ));
        System.out.println( Cache.getCache("mykey3", handler ));
        System.out.println( Cache.getCache("mykey4", handler ));
        System.out.println( Cache.getCache("mykey1", handler ));
        System.out.println( Cache.getCache("mykey1", handler));
        System.out.println( Cache.getCache("mykey1", handler ));
        System.out.println( Cache.getCache("mykey1", handler ));
        System.out.println( Cache.getCache("mykey1", handler ));
        System.out.println("waiting 3 seconds");
        Thread.sleep(4);
        JOptionPane.showMessageDialog(null, "PAUSE");
        System.out.println("continue...");
        System.out.println( Cache.getCache("mykey1", handler ));
        System.out.println( Cache.getCache("mykey1", handler ));
        System.out.println( Cache.getCache("mykey1", handler ));
        
    }
    
    public static class MyHandler extends ManagedCache.DefaultHandler {
        public Object get(String name) {
            return "hello:"+name;
        }
        public String getNamespace() {
            return "default";
        }
        public int getExpiry() {
            return 3;
        }
        public int getMaxSize() {
            return 1;
        }
    }
    
}

