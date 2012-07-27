/*
 * PageFileHandler.java
 *
 * Created on June 19, 2012, 10:42 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class PageFileHandler extends FileHandler {
    
    public PageFileHandler( ) {
    }
    
    public String getExt() {
        return "pg";
    }
    
    public AbstractFile createFile(String id, Map props) {
        return  new Page(props);
    }
    
    public InputStream getContent(Object o) {
        //return new ByteArrayInputStream("the content here".getBytes());
        //1. locate the master layout
        //2. create a content handler to retrieve the blocks and widget
        //return null;
        PageInstance pi = (PageInstance)o;
        Map page = pi.getPage();
        if(page.get("theme")==null)  page.put("theme", project.getDefaultTheme());
        String master = (String)page.get("master");
        if(master==null) {
            master = "default";
            page.put("master", master);
        }
        
        ContentTemplate ct = project.getContentManager().getHandler(ContentTypes.MASTER).getTemplate( master, pi  );
        if( ct == null ) {
            //in case the master is not found try the default. It should be applicable.
            ct = project.getContentManager().getHandler(ContentTypes.MASTER).getTemplate( "default", pi  );
        }
        if(ct ==null) 
            throw new RuntimeException("Master layout not found");
        PageContentMap map = new PageContentMap(pi, project);
        String result = ct.render( map );
        return new ByteArrayInputStream(result.getBytes());
    }
    
    
}
