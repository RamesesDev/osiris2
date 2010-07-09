/*
 * DataDefManager.java
 *
 * Created on December 28, 2009, 9:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.dms;

import com.sun.jmx.remote.util.Service;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author elmo
 */
public class SchemaManager {
    
    private static Map<String, Schema> map = new Hashtable<String,Schema>();
    
    public static Schema getSchema(String name) throws Exception {
        if(!map.containsKey(name)) {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/schema/" + name + ".xml");
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            SchemaParser sp = new SchemaParser();
            parser.parse(is,sp);
            map.put( name, sp.getSchema() );
        }
        return map.get(name);
    }

    
    public static SqlDialect getDialect(String name) {
        Iterator iter = Service.providers(SqlDialect.class, Thread.currentThread().getContextClassLoader());
        while(iter.hasNext()) {
            SqlDialect d = (SqlDialect)iter.next();
            if(d.getName().equalsIgnoreCase(name)) return d;
        }
        return null;
    }
    
    
}
