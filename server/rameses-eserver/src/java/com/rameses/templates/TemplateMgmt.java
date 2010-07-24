package com.rameses.templates;

import com.rameses.eserver.JndiUtil;
import com.rameses.eserver.CacheServiceMBean;
import com.rameses.eserver.ResourceServiceMBean;
import com.sun.jmx.remote.util.Service;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;

/***
 * This class should be able to transform templates. 
 * Use cases are:
 *      GroovyTemplate
 *      XSLT
 * sample code
 *      
 * GroovyTemplate example:
 *     Template t = TemplateService.getTemplate( "http://customer1/customerLetter.groovy" ); 
 *     Map data = new HashMap();
 *     data.put("firstname", "elmo" ); 
 *     String s = (String)t.transform( data ); 
 *
 * XSLTTemplate example:
 *     Template t = TemplateService.getTemplate( "http://customer1/customerLetter.xslt" ); 
 *     String data = "<firstname>elmo</firstname>;
 *     String s = (String)t.transform( data ); 
 */

public class TemplateMgmt implements TemplateMgmtMBean, Serializable {
    
    private final static String JNDI_NAME = "TemplateMgmt";
    private final static String TEMPLATE_CACHE = "TemplateCache";
    private List<TemplateProvider> providers;
    private CacheServiceMBean cacheService;
    private ResourceServiceMBean resourceService;
    
    public void start() throws Exception{
        System.out.println("START TEMPLATE SERVICE");
        InitialContext ctx = new InitialContext();
        JndiUtil.bind(ctx, JNDI_NAME, this);
        providers = new ArrayList();
        Iterator<TemplateProvider> iter = Service.providers(TemplateProvider.class,Thread.currentThread().getContextClassLoader());
        while(iter.hasNext()) {
            TemplateProvider tp = iter.next();
            System.out.println("   Loading Template Provider " + tp.toString());
            providers.add(tp);
        }
        cacheService = (CacheServiceMBean)ctx.lookup("CacheService");
        resourceService = (ResourceServiceMBean)ctx.lookup("ResourceService");
    }

    public void stop() throws Exception{
        System.out.println("STOPPING TEMPLATE SERVICE");
        JndiUtil.unbind(new InitialContext(), JNDI_NAME);
        providers.clear();
        providers = null;
        flushAll();
        cacheService = null;
        resourceService = null;
    }

    public void flushAll() {
        cacheService.getContext(TEMPLATE_CACHE).clear();
    }

    public void flush(String name) {
        String ext = name.substring(name.lastIndexOf(".")+1);
        Map map = cacheService.getContext(TEMPLATE_CACHE);
        if(map.containsKey(ext)) {
            ((Map)map.get(ext)).remove(name);
        }
    }

    
    /***
     * This returns a cached template.
     * The type of template is identified by its extension
     */
    public Template getTemplate(String name) throws Exception {
        String ext = name.substring(name.lastIndexOf(".")+1);
        Map cacheContext = cacheService.getContext(TEMPLATE_CACHE);
        
        //try to allocate a 
        if(!cacheContext.containsKey(ext)) {
            Map<String,Template> map = new Hashtable();
            cacheContext.put( ext, map );
        }
        
        Map<String,Template> templates = (Map)cacheContext.get(ext);
        if(! templates.containsKey(name)) {
            TemplateProvider tp = null;
            for(TemplateProvider _tp: providers) {
                if(_tp.accept(name)) {
                    tp = _tp;
                    break;
                }
            }
            if(tp==null)
                throw new IllegalStateException("No template handler for ." + ext + "  extension");
            
            //find the resource.
            InputStream is = resourceService.getResource(name);
            if(is==null)
                throw new IllegalStateException("Resource not found for " + name);
            Template t = tp.createTemplate(is);
            templates.put(name, t);
        }
        
        return templates.get(name);
        
    }
    
}
