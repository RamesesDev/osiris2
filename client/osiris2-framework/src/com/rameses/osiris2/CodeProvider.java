/*
 * ICodeProvider.java
 *
 * Created on February 17, 2009, 8:21 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2;

/**
 *
 * @author elmo
 */
public interface CodeProvider {
    Class createClass(String source);
    Object createObject(Class clazz);
}
