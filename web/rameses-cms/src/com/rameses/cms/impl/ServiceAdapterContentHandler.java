/*
 * BlockContent.java
 *
 * Created on June 23, 2012, 11:33 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms.impl;

import com.rameses.cms.ContentHandler;
import com.rameses.cms.ContentTypes;
import java.io.InputStream;



/**
 *
 * @author Elmo
 */
public class ServiceAdapterContentHandler extends ContentHandler {
    
    protected String getName() {
        return ContentTypes.SERVICE;
    }
    
    public InputStream getSource(String id, Object data) {
        return findResource(project.getUrl()+"/services/"+id);
    }
    
    
    
}
