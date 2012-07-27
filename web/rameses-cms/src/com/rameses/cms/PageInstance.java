/*
 * PageInstance.java
 *
 * Created on June 21, 2012, 5:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import com.rameses.io.StreamUtil;
import java.io.InputStream;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public class PageInstance {
    
    private Map page;
    private Map params;
    
    //store the values here if there are responses.
    private Page _page;
    private String content;
    
    /** Creates a new instance of PageInstance */
    public PageInstance(Page page, Map params) {
        this.page = page.toMap();
        this.page.put("imports", new LinkedHashSet());
        this.page.put("meta", new LinkedHashSet());
        
        this._page = page;
        this.params = params;
    }

    public Map getPage() {
        return page;
    }

    public Map getParams() {
        return params;
    }

    public InputStream getInputStream() {
        return _page.getFileManager().getFileHandler( _page.getExt() ).getContent(this);
    }

    public void buildContent() {
        content = StreamUtil.toString( getInputStream() );
    }
    
    public String getContent() {
        if(content==null) buildContent();
        return content;
    }
    
    public String getPagepath() {
        return _page.getPagepath();
    }

}
