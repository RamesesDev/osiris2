/*
 * RuleProvider.java
 *
 * Created on July 30, 2010, 9:16 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.interfaces;

import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public interface RuleProvider {
    
    Object createFact(String name) throws Exception;
    void execute(List facts, Map globals, String agenda) throws Exception;
    
    
    void load() throws Exception;

    String getName();
    void setName(String name);

    void setPath(String path);
    String getPath();

    void setClassLoader(ClassLoader loader);
    ClassLoader getClassLoader();
}
