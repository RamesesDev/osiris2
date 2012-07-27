/*
 * ParseMap.java
 *
 * Created on June 13, 2012, 9:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class PageContentMap extends HashMap {
    
    private PageInstance pageInstance;
    private Project project;
    
    public PageContentMap(PageInstance page, Project proj) {
        this.pageInstance = page;
        super.put("WIDGET", new WidgetHandler());
        super.put("PAGE", page.getPage());
        super.put("PARAMS", page.getParams());
        super.put("WEBROOT", new WebRootHandler() );
        super.put("SERVICE", new ServiceHandler() );
        super.put("TEMPLATE", new TemplateHandler() );
        super.put("PROJECT", proj );
        this.project = proj;
    }
    
    public Object get(Object key) {
        if(key.toString().startsWith("_")) {
            //GET THE BLOCK.
            try {
                String blockName = pageInstance.getPagepath() +"/"+ key.toString().substring(1);
                ContentTemplate ct = project.getContentManager().getHandler(ContentTypes.BLOCK).getTemplate( blockName, pageInstance  );
                PageContentMap map = new PageContentMap(pageInstance, project);
                return ct.render( map );
            } catch(Exception ign) {
                return "";
            }
        }
        return super.get(key);
    }
    
    //getting the widget.
    private class WidgetHandler {
        public String load( String name, Map options ) throws Exception {
            ContentTemplate ct = project.getContentManager().getHandler(ContentTypes.WIDGET).getTemplate( name, pageInstance  );
            PageContentMap map = new PageContentMap(pageInstance, project);
            map.put("OPTIONS", options);
            return ct.render( map );
        }
    }
    
    private class TemplateHandler {
        public String render(String id, Object data ) {
            return render(id, data, "data");
        }
        public String render(String id, Object data, String varName ) {
            ContentTemplate ct = project.getContentManager().getHandler(ContentTypes.TEMPLATE).getTemplate( id, null  );
            PageContentMap map = new PageContentMap(pageInstance, project);
            map.put(varName, data);
            return ct.render( map );
        }
    }
    
    private class WebRootHandler {
        public Folder getFolder(String name) {
            return (Folder) project.getFileManager().getPageManager().getFolder( name );
        }
    }
    
    private class ServiceHandler {
        public Object create( String name ) throws Exception {
            try {
                Object o = project.getServiceManager().create(name);
                return o;
            } catch(Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
    
    
    
}
