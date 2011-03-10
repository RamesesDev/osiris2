/*
 * Cache.java
 *
 * Created on January 31, 2011, 7:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package bak;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author ms
 */
public abstract class ManagedCache {
    
    private LinkedHashMap map = new LinkedHashMap();
    
    private boolean cleaning;
    
    public LinkedHashMap getMap() {
        return map;
    }
    
    public abstract Object fetch(Object key);
    
    public void setCleaning(boolean c) {
        this.cleaning = c;
    }
    
    public Object get( Object key ) {
        return get(key, 60*60);
    }
    
    public Object get( Object key, int expirySeconds ) {
        return get(key, expirySeconds, 50);
    }
    
    public Object get( Object key, int expiry, int maxSize ) {
        Calendar cal = Calendar.getInstance();
        Date testDate = cal.getTime();
        CacheData cacheData = (CacheData)getMap().get(key);
        if( cacheData==null || cacheData.getExpiry().before(testDate) || expiry < 0 ) {
            if( cacheData==null ) {
                cal.setTime(testDate);
                cal.add( Calendar.SECOND, expiry );
                Date expiryDate = cal.getTime();
                if(cacheData==null) {
                    Object d = fetch( key );
                    cacheData = new CacheData(d,expiryDate);
                } else {
                    cacheData.setExpiry( expiryDate );
                    try {
                        getMap().remove(key);
                    } catch(Exception ign){;}
                }
                //always remove so it can be transferred at the bottom.
                getMap().put(key, cacheData);
            }
            //check if we should run the cleaner.
            if(!cleaning && getMap().size()>maxSize) {
                cleaning = true;
                CacheCleaner cleaner = new CacheCleaner(this,maxSize);
                new Thread(cleaner).start();
            }
        }
        return cacheData.getData();
    }
    
    private static class CacheCleaner implements Runnable {
        private ManagedCache cache;
        private int maxSize;
        
        public CacheCleaner(ManagedCache c, int ms) {
            cache = c;
            maxSize = ms;
        }
        
        public void run() {
            try {
                Date now = new Date();
                //correct the size first irregardless of size.
                Map map = cache.getMap();
                int extraSize = map.size() - maxSize;
                List<String> forRemoval = new ArrayList();
                for(Object o: map.entrySet() ) {
                    Map.Entry me = (Map.Entry)o;
                    CacheData cacheData = (CacheData)me.getValue();
                    extraSize--;
                    if( extraSize >= 0  || cacheData.getExpiry().after(now)) {
                        forRemoval.add( me.getKey()+"" );
                        if( extraSize < 0 && cacheData.getExpiry().before(now)) break;
                    }
                }
                //finally remove the items
                for(String key: forRemoval) {
                    try {
                        map.remove(key);
                    } catch(Exception ign){;}
                }
            } catch(Exception e) {
                System.out.println("Cache cleanup error. " + e.getMessage());
            } finally {
                cache.setCleaning(false);
            }
        }
    }
    
    private static final class CacheData {
        
        private Date expiry;
        private Object data;
        
        public CacheData(Object d, Date e) {
            data = d;
            expiry = e;
        }
        public Object getData() {
            return data;
        }
        public void setExpiry(Date dt) {
            expiry = dt;
        }
        public Date getExpiry() {
            return expiry;
        }
    }
    
}
