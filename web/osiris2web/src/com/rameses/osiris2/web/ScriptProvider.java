/*
 * ScriptProvider.java
 *
 * Created on August 11, 2010, 2:20 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.classutils.AnnotationFieldHandler;
import com.rameses.classutils.ClassDefUtil;
import groovy.lang.GroovyClassLoader;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;


public final class ScriptProvider {
    
    private AnnotationFieldHandler fieldHandler = new FieldInjectionHandler();
    private GroovyClassLoader loader;
    
    private Map<String,Class> scriptMap = new Hashtable();
    
    public ScriptProvider() {
        loader = new GroovyClassLoader(Thread.currentThread().getContextClassLoader());
    }
    
    public Object create(String name ) throws Exception {
        String nameSpace = null;
        String scriptName = name;
        //check if it has name space.
        if(name.indexOf(":")>0) {
            nameSpace = name.substring(0, name.indexOf(":"));
            scriptName = name.substring( name.indexOf(":")+1 );
        }
        scriptName = scriptName.replaceAll("\\.", "/");
        
        if( !scriptMap.containsKey(name) ) { 
            //parse new class
            String fileName = "META-INF/scripts/" + scriptName;
            InputStream is = null;
            try {
                if(nameSpace==null) {
                    is = loader.getResourceAsStream( fileName );
                }    
                else {
                    is = WebContext.getInstance().getSessionContext().getModule(nameSpace).getResourceAsStream(fileName);
                }
                
                //clazz = loader
                Class clazz = loader.parseClass( is );
                scriptMap.put( name, clazz );
            }
            catch(Exception e) {
                throw new IllegalStateException("Error loading " + name + "." + e.getMessage(), e); 
            } 
            finally { 
                try { is.close(); } catch(Exception ign){;} 
            }
        }
        
        //instantiate the script
        try {
            Class clazz = scriptMap.get(name);
            Object obj = clazz.newInstance();
            ClassDefUtil.getInstance().injectFields(obj, fieldHandler);
            return obj;
        }
        catch(Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        
    }
    
    
    
}
