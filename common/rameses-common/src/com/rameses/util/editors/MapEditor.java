package com.rameses.util.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * this class acts as a wrapper for a Map object
 * Changes are tracked similar to a PropertyChangeListener.
 * the tracked changes are prefixed with _. for example
 * firstname will be marked as _firstname.
 *
 * editmode = flag to indicate if this is new editing or not.
 * false means this is a new editing model.
 * if editmode is false, meaning new, we will not track the changes
 * because there is no old values to track changes from.
 */
public class MapEditor extends HashMap {
    
    private boolean editmode = true;
    private Map data;
    
    private Map<String, ListEditor> listEditors = new Hashtable();
    private Map<String, MapEditor> mapEditors = new Hashtable();
    
    public MapEditor(Map data , boolean editmode) {
        this.data = data;
        this.editmode = editmode;
        if(!editmode) this.data.put( "_new", true );
    }
    
    public MapEditor(Map data) {
        this.data = data;
    }
    
    public Object get(Object key) {
        String skey = key + "";
        Object val = data.get(skey);
        if( val instanceof List ) {
            List list = (List)val;
            if(! listEditors.containsKey(skey) ) {
                listEditors.put(skey, new ListEditor(this, skey, list) );
            }
            val = listEditors.get( skey );
        }
        else if( val instanceof Map ) {
            Map map = (Map)val;
            if(! mapEditors.containsKey(skey) ) {
                mapEditors.put(skey, new MapEditor(map) );
            }
            val = mapEditors.get( skey );
        }
        return val;
    }
    
    public Object put(Object key, Object value) {
        String marker = "_" + key;
        
        if( !data.containsKey(key)) {
            data.put(key,value);
            if(editmode) data.put(marker, value);
        } else {
            if( editmode ) {
                Object oldValue = data.get(key);
                
                //check if there are no immediate changes.
                if(oldValue==null && value==null) return value;
                if(oldValue!=null && oldValue.equals(value) ) return value;
                
                //check if there is no change between new value and the original value
                //if value is same as original remove the marker attribute
                if( data.containsKey(marker) ) {
                    Object origValue = data.get(marker);
                    if( (origValue == null && value == null)||
                            (origValue!=null && origValue.equals(value))   ) {
                        data.remove(marker);
                    }
                } else {
                    data.put(marker,oldValue);
                }
            }
            data.put(key, value);
        }
        return value;
    }
    
    /***
     * this returns a new map containing the changed values
     */
    public Map changes() {
         boolean newData = false;
         try {
            newData = Boolean.parseBoolean(data.get("_new")+"");
         }catch(Exception ign){;}
         
         //if this is new data, return all map values.
         if(newData) {
             return data;
         }
         
         List<String> changes = new ArrayList();
         
         for(Object o: data.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            String skey = me.getKey()+"";
            if(skey.startsWith("_")){
                changes.add(skey);
            }
            
         }
         Map map = new HashMap();
         List deletedItems = new ArrayList();
         for(String s: changes) {
            String n = s.substring(1);
            if( data.containsKey(n) ) {
                map.put(n, data.get(n));
            }
         }
         return map;
    }
    
    
     
}
