/*
 * ImageCache.java
 *
 * Created on December 4, 2010, 1:19 PM
 * @author jaycverg
 */

package com.rameses.rcp.control.webbrowser;

import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import javax.swing.ImageIcon;


class ImageCache<K,V> extends Hashtable<K,V> {
    
    private String context;
    
    ImageCache(String context) {
        this.context = context;
    }
    
    public V get(Object key) {
        URL u = (URL) key;
        try {
            u = BrowserUtil.getCache(u, context, "IMG");          
        } catch (Exception ex) {;}
        
        return (V) new ImageIcon(u).getImage();
    }
    
    public V put(K key, V value) { return null; }
    public void putAll(Map<? extends K, ? extends V> t) {;}
    
    
}
