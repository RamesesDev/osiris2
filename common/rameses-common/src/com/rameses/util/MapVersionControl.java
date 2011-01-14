/*
 * MapDiff.java
 *
 * Created on December 10, 2010, 1:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author ms
 */
public final class MapVersionControl {
    
    private static MapVersionControl instance;
    
    public static final synchronized MapVersionControl getInstance() {
        if ( instance == null ) instance = new MapVersionControl();
        return instance;
    }
    
    
    private void processDiff( Map map1,  Map map2, String prefix, DiffHandler handler ) {
        for( Object o : map1.entrySet() ) {
            Map.Entry me = (Map.Entry)o;
            Object value = me.getValue();
            String name = me.getKey().toString();
            Object x = map2.get( name );
            if(prefix ==null ) prefix = "";
            String fieldName = prefix + name;
            
            //check first if there is a diff
            if(!ValueUtil.isStringValueEqual(value, x )) {
                if(value!=null &&  (value instanceof Map) && (x instanceof Map)  ) {
                    processDiff( (Map)value, (Map)x, fieldName + "." , handler );
                } else if( value!=null &&  (value instanceof List) && (x instanceof List)) {
                    List list1 = (List)value;
                    List list2 = (List)x;
                    if(list1.size()>0) {
                        int sz2 = list2.size();
                        for(int i=0;i<list1.size();i++) {
                            Object listVal = list1.get(i);
                            if(i >= sz2) {
                                handler.addItem( fieldName,i,listVal );
                            } else {
                                Object listVal2 = list2.get( i );
                                if(listVal!=listVal2) {
                                    if((listVal instanceof Map) && (listVal2 instanceof Map)) {
                                        processDiff( (Map)listVal, (Map)listVal2, fieldName +"["+i+"].", handler);
                                    } else {
                                        handler.update( fieldName+"["+i+"]", listVal );
                                    }
                                }
                            }
                        }
                    }
                } else {
                    handler.update(fieldName, value);
                }
            }
        }
    }
    
    
    private interface DiffHandler {
        void addItem( String name, int pos, Object value );
        void update( String name, Object value );
    }
    
    private class OldAgainstNewDiffHandler implements DiffHandler {
        private Map diff;
        public OldAgainstNewDiffHandler(Map l) {
            diff = l;
        }
        public void update(String name, Object value) {
            Map m = new HashMap();
            m.put("action", "update");
            m.put("oldValue", value );
            diff.put( name, m   );
        }
        
        public void addItem(String name, int pos, Object value) {
            Map m = new HashMap();
            m.put("action", "remove");
            m.put("value", value );
            m.put("pos", pos );
            m.put("name", name );
            diff.put(name + "["+pos+"]", m );
        }
    }
    
    private class NewAgainstOldDiffHandler implements DiffHandler {
        private Map diff;
        public NewAgainstOldDiffHandler(Map l) {
            diff = l;
        }
        public void update(String name, Object value) {
            Map m = (Map)diff.get(name);
            if( m == null ) {
                m = new HashMap();
                m.put("action", "update");
                diff.put(name, m);
            }
            m.put("newValue", value );
        }
        
        public void addItem(String name, int pos, Object value) {
            Map m = new HashMap();
            m.put("action", "add");
            m.put("value", value );
            m.put("pos", pos );
            m.put("name", name );
            diff.put( name+"["+pos+"]", m  );
        }
    }
    
    /**
     * This method returns a diff map. A diff is a Map of change maps
     * using the field name and position as the key
     * A change map includes the type of action: add,update,remove
     * @param comp1 is the old value
     * @param comp2 is the new value
     */
    public Map diff( Map comp1, Map comp2 ) {
        Map diff = new HashMap();
        processDiff(comp1, comp2, null, new OldAgainstNewDiffHandler(diff));
        processDiff(comp2, comp1, null, new NewAgainstOldDiffHandler(diff));
        
        return diff;
    }
    
    /**
     * This method creates a merged map or new copy. You need to run first the
     * a diff operation.
     * @param oldVersion old map version
     * @param diff result of running a diff operation
     * @return
     *      returns a Map of the merged values.
     */
    public Map merge( Map oldVersion, Map diff ) {
        TreeMap sortedDiff = new TreeMap(diff);
        Map copy = new HashMap();
        scanCopy( copy, oldVersion, null, sortedDiff );
        applyChanges(copy, sortedDiff);
        
        return copy;
    }
    
    public void applyChanges( Map model, Map diff ) {
        for(Object o: diff.entrySet()) {
            Map.Entry me = (Map.Entry)o;
            String name = (String) me.getKey();
            Map changeEntry = (Map)me.getValue();
            Map context = model;
            if(name.contains(".")) {
                //split the names. get the parent
                String arr[] = name.substring(0, name.lastIndexOf(".")).split("\\.");
                name = name.substring(name.lastIndexOf(".")+1);
                //we should get the last name
                for(String s: arr ) {
                    if(s.contains("[")) {
                        String xname = s.substring(0, s.indexOf("["));
                        int idx = Integer.valueOf( s.substring(s.indexOf("[")+1,s.indexOf("]"))).intValue();
                        List list = (List)context.get(xname);
                        context = (Map)list.get(idx);
                    } else {
                        context = (Map)context.get(s);
                    }
                }
            }
            context.put(name, changeEntry.get("newValue"));
        }
    }
    
    private void scanCopy( Map context, Map comp2, String prefix, Map diff ) {
        for( Object o : comp2.entrySet() ) {
            Map.Entry me = (Map.Entry)o;
            Object value = me.getValue();
            String name = me.getKey().toString();
            
            String fieldName = name;
            if(prefix!=null) fieldName = prefix + "." + name;
            
            if( value!=null &&  (value instanceof Map) ) {
                Map change = (Map) diff.remove( fieldName );
                Map oldContext = (Map)value;
                Map subContext = new HashMap();
                context.put( name, subContext );
                
                //if there is a change then replace the whole node. otherwise scan it
                if(change!=null) {
                    subContext.put( name, change.get("newValue") );
                } else {
                    scanCopy( subContext, oldContext, fieldName, diff );
                }
            } else if( value!=null && (value instanceof List)) {
                List list = (List)value;
                List newList = new ArrayList();
                context.put( name, newList );
                int i = 0;
                
                //check first if all items in array have changes
                for(i=0; i<list.size();i++) {
                    String testName = fieldName + "["+i+"]";
                    Map change = (Map)diff.remove(testName);
                    //do not do anything if there is a change
                    //bec. if there is a change it is only for removal
                    if(change==null) {
                        Object ox = list.get( i );
                        if ( ox instanceof Map ) {
                            Map oldItem = (Map) ox;
                            Map newItem = new HashMap();
                            newList.add( newItem );
                            scanCopy( newItem, oldItem, testName, diff );
                        } else {
                            newList.add( ox );
                        }
                    } else if (change.get("action").equals("update")) {
                        Object ox = change.get("newValue");
                        newList.add( ox );
                    }
                }
                
                //if there is a change map here it is only for adding items
                while(true) {
                    String testName = fieldName + "["+i+"]";
                    Map change = (Map) diff.remove( testName );
                    if(change==null) break;
                    
                    newList.add( change.get("value") );
                }
            } else {
                Object nv = value;
                Map change = (Map)diff.remove( fieldName );
                if(change!=null) {
                    nv = change.get("newValue");
                }
                context.put(name, nv);
            }
        }
    }
    
    /***
     * This procedure checks the contents of two diffs
     * It returns a new Map of conflict maps which contain the maps of
     * two conflicts.If there is no conflict a null value is returned.
     */
    public Map checkConflict(  Map diff1, Map diff2 ) {
        Map conflict = new HashMap();
        for(Object o: diff1.entrySet() ){
            Map.Entry me = (Map.Entry)o;
            //check if diff1 value exists in diff2
            String k = (String)me.getKey();
            Object v = me.getValue();
            Object obj = diff2.get(k);
            if(obj!=null && !ValueUtil.isStringValueEqual(v, obj)) {
                Map entry = new HashMap();
                entry.put("diff1", v );
                entry.put("diff2", obj );
                conflict.put(k, entry);
            }
        }
        if(conflict.size()==0)
            return null;
        else
            return conflict;
    }
    
    
    
}
