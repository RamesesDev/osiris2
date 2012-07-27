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
import com.rameses.cms.PageInstance;
import com.rameses.cms.WidgetPackage;
import java.io.InputStream;



/**
 *
 * @author Elmo
 */
public class WidgetContentHandler extends ContentHandler {
    
    protected String getName() {
        return ContentTypes.WIDGET;
    }
    
    public InputStream getSource(String id, Object data) {
        String namespace = null;
        String widgetName = id;
        if( id.indexOf(":")> 0 ) {
            String[] arr = id.split(":");
            namespace = arr[0];
            widgetName = arr[1];
        }
        
        PageInstance pageInstance = (PageInstance)data;
        InputStream is = null;
        if( namespace == null ) {
            is = findResource( project.getUrl()+"/widgets/"+widgetName+".wgt/code" );
            if(is == null) {
                WidgetPackage wp = project.getWidgetPackages().get("default");
                if( wp != null ) {
                    is = findResource( wp.getUrl()+"/"+widgetName+".wgt/code" );
                }
            }
        } else {
            WidgetPackage wp = project.getWidgetPackages().get(namespace);
            if(wp!=null ) {
                is = findResource( wp.getUrl() +"/"+ widgetName+".wgt/code" );
            }
        }
        return is;
    }
    
    
}
