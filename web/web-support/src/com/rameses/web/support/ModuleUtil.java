/*
 * ModuleUtil.java
 * Created on April 30, 2011, 7:53 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.support;

import com.rameses.io.StreamUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;

public final class ModuleUtil {
    
    public static String MODULE_PATH = "/modules";
    public static String DEFAULT_ATTR_NAME = "modules";
    
    
    //builds the modules and stores it in the servlet context
    public static void initModules(ServletContext ctx) {
        initModules(ctx,DEFAULT_ATTR_NAME,"META-INF/module.conf");
    }
    
    public static void initModules(ServletContext ctx, String attrName) {
        initModules(ctx,attrName,"META-INF/module.conf");
    }
    
    public static void initModules(ServletContext ctx, String attrName,String confPath) {
        Map map = new HashMap();
        Iterator iter=ctx.getResourcePaths(MODULE_PATH).iterator();
        InputStream is = null;
        while(iter.hasNext() ) {
            String s = (String)iter.next();
            String _name = s.substring(0, s.lastIndexOf("/") );
            _name = _name.substring(_name.lastIndexOf("/")+1);
            try {
                is = ctx.getResourceAsStream(s+confPath);
                if(is!=null) {
                    String stream = StreamUtil.toString(is);
                    Object ox = JsonUtil.toObject(stream);
                    map.put(_name, ox);
                }
            } catch(Exception ex) {
                //do nothing
            } finally{
                try{is.close();} catch(Exception ign){;}
            }
        }
        ctx.setAttribute(attrName, map);
    }
    
    public static List getEntries( ServletContext ctx, String name ) {
        return getEntries(ctx,name,DEFAULT_ATTR_NAME);
    }
    
    private static Sorter DEFAULT_SORTER = new Sorter();
    
    public static List getEntries( ServletContext ctx, String name, String attrName ) {
        Map map = (Map)ctx.getAttribute(attrName);
        if(map==null) {
            initModules(ctx,attrName);
            map = (Map)ctx.getAttribute(attrName);
        }
        List list = new ArrayList();
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry me = (Map.Entry)iter.next();
            Map mo = (Map)me.getValue();
            if(mo.containsKey(name)) {
                Map _comp = new HashMap();
                _comp.putAll((Map)mo.get(name));
                _comp.put("_parent", mo);
                _comp.put("_name", me.getKey());
                list.add(_comp);
            }
        }
        Collections.sort(list, DEFAULT_SORTER);
        return list;
    }
    
    private static class Sorter implements Comparator {
        
        public int compare(Object o1, Object o2) {
            if(!(o1 instanceof Map)) return 0;
            if(!(o2 instanceof Map)) return 0;
            Map m1 = (Map)o1;
            Map m2 = (Map)o2;
            Integer i1 = 0;
            if(m1.containsKey("index")) i1 = new Integer(m1.get("index")+"");
            Integer i2 = 0;
            if(m2.containsKey("index")) i2 = new Integer(m2.get("index")+"");
            return i1.compareTo(i2);
        }
        
    }
    
    
    
    
}
