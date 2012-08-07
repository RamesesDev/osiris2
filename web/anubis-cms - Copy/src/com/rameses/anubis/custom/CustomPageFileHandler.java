/*
 * CustomPageFileHandler.java
 *
 * Created on July 4, 2012, 2:22 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.custom;

import com.rameses.anubis.BlockContentProvider;

import com.rameses.anubis.MasterContentProvider;

import com.rameses.anubis.PageFileHandler;
import com.rameses.anubis.TemplateContentProvider;
import com.rameses.anubis.WidgetContentProvider;

/**
 *
 * @author Elmo
 */
public class CustomPageFileHandler extends PageFileHandler  {
    
    private BlockContentProvider blockContentProvider;
    private MasterContentProvider masterContentProvider;
    private WidgetContentProvider widgetContentProvider;
    private TemplateContentProvider templateContentProvider;
    
    
    /** Creates a new instance of CustomPageFileHandler */
    public CustomPageFileHandler() {
        this.blockContentProvider = new CustomBlockContentProvider();
        this.masterContentProvider = new CustomMasterContentProvider();
        this.widgetContentProvider = new CustomWidgetContentProvider();
        this.templateContentProvider = new CustomTemplateContentProvider();
    }

    public BlockContentProvider getBlockContentProvider() {
        return blockContentProvider;
    }

    public MasterContentProvider getMasterContentProvider() {
        return masterContentProvider;
    }

    public WidgetContentProvider getWidgetContentProvider() {
        return widgetContentProvider;
    }

    public TemplateContentProvider getTemplateContentProvider() {
        return templateContentProvider;
    }

    
}
