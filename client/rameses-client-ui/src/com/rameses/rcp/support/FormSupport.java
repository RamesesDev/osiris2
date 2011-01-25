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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if(mode!=null && mode.equals("hidden")) continue;
            String name = (String)nm.remove("name");
            String type = (String)nm.remove("type");
            if(type==null) type="text";
            if(entityVarName!=null && entityVarName.trim().length()>0) name = entityVarName + "." + name;
            nm.put("name", name);
            FormControl fc = new FormControl();
            fc.setType(type);
            fc.setProperties(nm);
            list.add(fc);
        }
        return list;
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
    
    public static Map buildMap( ) {
        Map map = new HashMap();
        
        return map;
    }
    
}
