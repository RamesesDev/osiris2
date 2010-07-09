package com.rameses.system;

import com.rameses.interfaces.CacheProvider;
import com.rameses.interfaces.CacheServiceLocal;
import com.rameses.util.StreamUtil;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Local;
import javax.ejb.Stateless;
import org.jboss.annotation.ejb.Management;

//@Service(objectName="rameses:service=CacheService")
@Stateless
@Local(CacheServiceLocal.class)
@Management(CacheServiceMgmt.class)
public class CacheService implements CacheServiceMgmt, CacheServiceLocal {
    
    private Map<String, CacheMap> map = new HashMap<String, CacheMap>(); 
    private Map<String, CacheProvider> providers = new HashMap<String, CacheProvider>(); 
    
    public CacheService() {
    }

    public Object get(String name) {
        String namespace = "default";
        String objname = name;
        if( name.contains(":")) {
            namespace = name.substring(0, name.indexOf(":"));
            objname = name.substring( name.indexOf(":")+1);
        }
        
        if( !map.containsKey(namespace)) {
            map.put( namespace, new CacheMap(namespace) );
        }
        
        return map.get(namespace).get(objname);
    }

    public void flush(String namespace) {
        CacheMap m = map.get(namespace);
        if( m != null ) {
            m.clear();
        }
    }

    public void flushAll() {
        for( Object o : map.values() ) {
            CacheMap m = (CacheMap)o;
            m.clear();
        }
    }

    //when adding a provider it automatically flushes out existing cache maps
    public void addProvider(CacheProvider p) {
        flush(p.getNamespace());
        providers.put( p.getNamespace(), p );
    }

    public void start() {
        System.out.println("STARTING CACHE SERVICE");
        providers.put( "default", new DefaultCacheProvider() );
        providers.put( "txtfile", new TextFileCacheProvider() );
    }

    public void stop() {
        flushAll();
        providers.clear();
    }

    public void removeProvider(String name) {
        providers.remove(name);
        flush( name );
    }

    public class CacheMap extends HashMap {
        
        private String namespace;
        
        public CacheMap(String n) {
            namespace = n;
        }
        
        public Object get(Object obj) {
            Object val = super.get(obj);
            if( val == null ) {
                CacheProvider provider = providers.get(namespace);
                if( provider == null ) {
                    provider = providers.get("default");
                }
                val = provider.getValue( obj );
                if( val == null ) {
                    throw new IllegalStateException( "Cache key " + namespace + ":" + obj + " not found!" );
                }
                super.put( obj, val );
            }
            return val;
        }
    }
    
    //The default cache provider returns a URL anything under META-INF
    public class DefaultCacheProvider implements CacheProvider {
        
        public String getNamespace() {
            return "default";
        }

        public Object getValue(Object key) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            return loader.getResource("META-INF/" + key );
        }
        
    }
    
    //The default cache provider returns a URL anything under META-INF
    public class TextFileCacheProvider implements CacheProvider {
        
        public String getNamespace() {
            return "txtfile";
        }

        public Object getValue(Object key) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL u = loader.getResource("META-INF/" + key );
            try {
                return StreamUtil.toString(u.openStream());
            }
            catch(Exception ex) {
                return null;
            }
        }
        
        
        
    }
    
}
