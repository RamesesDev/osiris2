/*
 * CmsPageUtil.java
 *
 * Created on April 28, 2012, 8:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.cms;

import com.rameses.util.TemplateProvider;
import com.rameses.util.TemplateSource;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Elmo
 * To do: 
 * load page this is the body.                  client/pages/
 * for each fragment - load the fragments.      client/fragments/
 * load template                                client/templates master/
 *
 * <%
 *   INFO.template = ":basic_layout";    //if starts with : look in global templates. 
 *                                         else look in client's folder.
 *   INFO._header = "header1"            //if not specified, set as header
 *   INFO._footer = "footer1"            //if not specified, set as footer
 *   INFO._navsection = "section1"       //search in fragments, 
 * %>
 *
 */
public class CmsPageUtil {
    
    private TemplateProvider templateProvider = TemplateProvider.getInstance();
    private TemplateSource pageSource;
    private TemplateSource templateSource;
    private TemplateSource fragmentSource;
    
    public CmsPageUtil() {
        
    }
    
    public TemplateProvider getTemplateProvider() {
        return templateProvider;
    }
    
    public void setTemplateProvider(TemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
    }
    
     public TemplateSource getTemplateSource() {
        return templateSource;
    }
    
    public void setTemplateSource(TemplateSource templateSource) {
        this.templateSource = templateSource;
    }
    
    private String getPage(String pageName, Map params, TemplateSource ts ) {
        if( pageName.indexOf(".")<=0) pageName = pageName + ".groovy";
        if( pageSource == null ) 
            throw new RuntimeException( "Page Source must be provided");
        Map info = new HashMap();
        Map p = new HashMap();
        p.putAll( params );
        p.put( "INFO", info );
        String page = (String)templateProvider.getResult( pageName, p, ts );
        if( page == null || page.trim().length() == 0 )
            return null;
        //check if there is template
        if( info.containsKey("template")) {
            String templateName = (String)info.remove("template");
            if( templateName !=null ) {
                Map newMap = new HashMap();
                newMap.putAll( params );
                newMap.put( "body", page );
                String newPage = getPage( templateName, newMap, templateSource );
                if( newPage !=null ) page = newPage;
            }
        }
        Map m = new HashMap();
        String s = "pagei";
        TemplateProvider.getInstance().getResult(s, m, ts);
        return page;
    }

    public TemplateSource getPageSource() {
        return pageSource;
    }

    public void setPageSource(TemplateSource pageSource) {
        this.pageSource = pageSource;
    }
    
   
    
    
}
