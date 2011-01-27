/*
 * FormSupport.java
 *
 * Created on January 25, 2011, 7:17 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rcp.support;

import com.rameses.rcp.common.FormControl;
import com.rameses.rcp.util.FormControlUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ms
 */
public final class FormSupport {
    
    /**
     * Builds the form controls from a map.
     */
    public static List<FormControl> buildFormControls(List<Map> infos) {
        return buildFormControls(infos, null);
    }
    
    public static List<FormControl> buildFormControls(List<Map> infos, String entityVarName) {
        List<FormControl> list = new ArrayList();
        for( Map m : infos ) {
            Map nm = new HashMap();
            nm.putAll( m );
            String mode = (String)nm.remove("mode");
            if( mode!=null && mode.equals("hidden") ) continue;
            
            String name = (String)nm.remove("name");
            String type = (String)nm.remove("type");
            if( type==null ) type="text";
            if( entityVarName!=null && entityVarName.trim().length()>0 ) {
                name = entityVarName + "." + name;
            }
            
            nm.put("name", name);
            FormControl fc = new FormControl();
            fc.setType(type);
            fc.setProperties(nm);
            list.add(fc);
        }
        return list;
    }
    
    public static List<FormControl> buildFormControls(List<Map> infos, String entityVarName, Map entityMap) {
        List<FormControl> controls = buildFormControls(infos, entityVarName);
        return filterFormControls(controls, entityMap, entityVarName);
    }
    
    public static List<FormControl> buildFormControls(List<Map> infos, String entityVarName, Map entityMap, Map extProps) {
        List<FormControl> controls = buildFormControls(infos, entityVarName);
        return filterFormControls(controls, entityMap, entityVarName, extProps);
    }
    
    public static List<FormControl> filterFormControls(List<FormControl> controls, Map entityMap) {
        return filterFormControls(controls, entityMap, null, null);
    }
    
    public static List<FormControl> filterFormControls(List<FormControl> controls, Map entityMap, String entityVarName) {
        return filterFormControls(controls, entityMap, entityVarName, null);
    }
    
    public static List<FormControl> filterFormControls(List<FormControl> controls, Map entityMap, String entityVarName, Map extProps) {
        List<FormControl> list = new ArrayList();
        for(FormControl fc: controls) {
            String name = (String) fc.getProperties().get("name");
            if(entityVarName !=null && entityVarName.trim().length()>0) {
                name = name.replaceAll( entityVarName+"\\.","" );
            }
            if(entityMap.containsKey(name)) {
                FormControl f = fc;
                if(extProps!=null) {
                    Map newProps = new HashMap();
                    newProps.putAll( f.getProperties() );
                    newProps.putAll( extProps );
                    f = new FormControl();
                    f.setType( fc.getType() );
                    f.setProperties( newProps );
                }
                list.add( f );
            }
        }
        return list;
    }
    
    public static Map buildModel(Map entity, List<Map> fields, List<String> includeFields ) {
        for(Map field : fields) {
            if( includeFields.indexOf(field.get("name")) >= 0 ) {
                if(!entity.containsKey(field.get("name")) ) {
                    if("subform".equals(field.get("type"))) {
                        entity.put( field.get("name"), new HashMap() );
                    } else {
                        entity.put( field.get("name"), null );
                    }
                }
            } else {
                if( !"hidden".equals(field.get("mode")) && !"fixed".equals(field.get("mode")) ) {
                    entity.remove( field.get("name") );
                }
            }
        }
        return entity;
    }
    
    /**
     * returns a Map of key value pair entity values
     * wherein the values are in html format
     */
    public static Map getHtmlValueFormat(List<FormControl> controls, Object entity) {
        return FormControlUtil.getInstance().buildHtmlValueFormat(controls, entity);
    }
    
    /**
     * returns a Map of key value pair of entity values
     * wherein the values are in (text)printable format
     */
    public static Map getPrintValueFormat(List<FormControl> controls, Object entity) {
        return FormControlUtil.getInstance().buildPrintValueFormat(controls, entity);
    }
    
    /**
     * returns a new Map filtered based on the list of fields passed
     */
    public static Map filterMap(Map entity, List fields) {
        Map newMap = new LinkedHashMap();
        if( fields != null ) {
            for(Object o : fields) {
                newMap.put(o, entity.get(o));
            }
        }
        return newMap;
    }
    
    /**
     * throws RuntimeException if the entity map does not contain
     * the required fields specified by the metaFields
     */
    public static void checkRequired(Map entity, List<Map> metaFields ) {
        StringBuffer errs = new StringBuffer();
        for(Map.Entry<String, Object> me : (Set<Map.Entry>)entity.entrySet()) {
            if( me.getValue() == null ) {
                final String key = me.getKey();
                Map z = (Map) find(metaFields, new Filter<Map>(){
                    public boolean accept(Map o) {
                        return o.get("name").equals(key);
                    }
                });
                errs.append( z.get("caption") + " is required\n" );
            }
        }
        
        if(errs.length()>0) {
            throw new RuntimeException(errs.toString());
        }
    }
    
    //List finder method
    private static Object find(List items, Filter filter) {
        for(Object o : items) {
            if( filter.accept(o) ) return o;
        }
        return null;
    }
    
    private interface Filter<T> {
        boolean accept(T obj);
    }
    
}
