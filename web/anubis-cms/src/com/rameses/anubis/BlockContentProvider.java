/*
 * BlockPageContentProvider.java
 *
 * Created on July 17, 2012, 10:08 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

/**
 *
 * @author Elmo
 * to be overridden by the custom page
 */
public abstract class BlockContentProvider extends PageContentCache {
    
    public String getContent(String name, PageFileInstance pi, PageContentMap map)  {
        try {
            return super.getContent(name,pi,map);
        } catch(Exception e) {
            return "";
        }
    }
    
}
