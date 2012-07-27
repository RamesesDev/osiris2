/*
 * ProjectHandler.java
 *
 * Created on June 20, 2012, 8:23 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.cms;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Elmo
 */
public interface ProjectSourceProvider {
    Map<String,Theme> getThemes();
    Map<String,Module> getModules();
    Map<String,WidgetPackage> getWidgetPackages();
}
