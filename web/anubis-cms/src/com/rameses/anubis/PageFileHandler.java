/*
 * PageFileHandler.java
 *
 * Created on July 4, 2012, 10:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

/**
 *
 * @author Elmo
 */
public abstract class PageFileHandler implements FileHandler {
    
    public abstract MasterContentProvider getMasterContentProvider();
    public abstract BlockContentProvider getBlockContentProvider();
    public abstract WidgetContentProvider getWidgetContentProvider();
    public abstract TemplateContentProvider getTemplateContentProvider();
    public abstract LayoutContentProvider getLayoutContentProvider();
    
    public String getExt() {
        return "pg";
    }

    public FileInstance createInstance(File file) {
        return  new PageFileInstance(file,  this );
    }
    
}
