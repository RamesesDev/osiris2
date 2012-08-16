/*
 * PageContentMap.java
 *
 * Created on July 1, 2012, 7:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;


import com.rameses.util.ExceptionManager;
import java.rmi.server.UID;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class PageContentMap extends HashMap {
    
    private PageFileInstance page;
    private Map params;
    private CMSHelper cmsHelper;
    private Map pageWrapper;
    private boolean editable = true;
    
    private PageFileHandler fileHandler;
    
    public PageContentMap(PageFileInstance page, Map params, PageFileHandler h) {
        this.page = page;
        this.params = params;
        this.cmsHelper =  new CMSHelper();
        this.pageWrapper = page.toMap();
        this.fileHandler = h;
    }
    
    public Object get(Object key) {
        String skey = key.toString();
        if(skey.startsWith("_")) {
            skey = skey.substring(1);
            if( super.containsKey(skey)) return super.get(skey);
            
            //GET THE BLOCK
            return getBlockContent(skey);
        } else if( skey.equals("PARAMS")) {
            return params;
        } else if (skey.equals("PAGE")) {
            return pageWrapper;
        } else if( skey.equals("ANUBIS")) {
            return cmsHelper;
        } else if( skey.equals("VARS")) {
            return page.getVars();
        } else if( skey.equals("SERVICE")) {
            return  page.getProject().getServiceManager();
        } else if( skey.equals("MODULE")) {
            if(page.getModule()==null) return new HashMap();
            return page.getModule();
        } else if( skey.equals("THEME")) {
            return page.getTheme();
        } else if( skey.equals("PROJECT")) {
            return  page.getProject();
        } else if( skey.equals("SESSION")) {
            if( AnubisContext.getCurrentContext()!=null ) {
                return AnubisContext.getCurrentContext().getSession();
            } else {
                return new HashMap();
            }
        } else if(skey.equals("anubisContext")) {
            return AnubisContext.getCurrentContext();
        } else {
            return super.get(key);
        }
    }
    
    private String getBlockContent(String skey) {
        try {
            String n = page.getId();
            n = n.substring(0, n.lastIndexOf("."));
            String blockName = n +"/"+ skey;
            PageContentMap map = new PageContentMap(page, params,  fileHandler);
            
            String block = "";
            //override if there is a language specified
            if(AnubisContext.getCurrentContext()!=null && AnubisContext.getCurrentContext().getCurrentLocale()!=null) {
                block = AnubisContext.getCurrentContext().getCurrentLocale().getBlockContentProvider().getContent(blockName,page,map);
                if( block!=null && block.trim().length()>0) return block;
            }
            //check the normal block
            StringBuilder sb = new StringBuilder();
            if(editable) sb.append( "<div class='anubis-block-editable'>");
            sb.append(fileHandler.getBlockContentProvider().getContent(blockName, page, map));
            if(editable) sb.append("</div>");
            return sb.toString();
        } catch(Exception ign) {
            return "";
        }
    }
    
    
    public class CMSHelper {
        
        public String getBlock( String name ) {
            return getBlockContent(name);
        }
        
        public String getWidget( String name, Map options ) {
            PageContentMap map = new PageContentMap( page, params,  getFileHandler()  );
            map.put("OPTIONS", options);
            return fileHandler.getWidgetContentProvider().getContent(name, page, map );
        }
        
        public Object call(String action, Map params) throws Exception {
            return page.getProject().getActionManager().getActionCommand(action).execute(params, new HashMap());
        }
        
        public String getTemplate( String id, Object data ) {
            return getTemplate(id, data, "DATA",null,null);
        }
        
        public String getTemplate( String id, Object data, String varName ) {
            return getTemplate( id, data, varName, null,null);
        }
        
        public String getTemplate( String id, Object data, String varName, Map options ) {
            return getTemplate( id, data, varName, options,null);
        }
        
        public String getTemplate( String id, Object data, String varName, Map options, Map status ) {
            PageContentMap map = new PageContentMap( page, params,  getFileHandler() );
            if(varName==null) varName = "DATA";
            map.put(varName, data );
            if(options!=null) map.put("OPTIONS", options);
            try {
                return fileHandler.getTemplateContentProvider().getContent(id, page, map );
            } catch(Exception e) {
                if(status!=null ){
                    e = ExceptionManager.getInstance().getOriginal(e);
                    status.put("exception",e);
                    status.put("errmsg", e.getMessage());
                    return "";
                } else {
                    return "<font color=red>template " + id + " not found</font>";
                }
            }
        }
        
        public Folder getFolder( String name ) {
            return page.getProject().getFileManager().getFolder(name);
        }
        
        public File getFileInfo(String name) {
            return page.getProject().getFileManager().getFileInfo(name);
        }
        
        public String queue(String name, Map options ) {
            String id = "_" + name + "_" + (new UID()).hashCode() + "_";
            page.getProcessQueue().put( id, new Object[]{name, options}  );
            return id;
        }
        
        public String translate(String key, String value) {
            if(AnubisContext.getCurrentContext()==null || AnubisContext.getCurrentContext().getCurrentLocale()==null) return null;
            return AnubisContext.getCurrentContext().getCurrentLocale().translate(key, value);
        }
        
        public String translate(String key, String value, String lang) {
            LocaleSupport support = page.getProject().getLocaleSupport( lang );
            if(support==null) return null;
            return support.translate( key, value );
        }
    }
    
    public PageFileHandler getFileHandler() {
        return fileHandler;
    }
    
    
    
    
}
