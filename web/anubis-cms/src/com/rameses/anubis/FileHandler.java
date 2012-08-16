/*
 * FileContentHandler.java
 *
 * Created on July 3, 2012, 5:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis;

/**
 *
 * @author Elmo
 */
public interface FileHandler {
    String getExt();
    FileInstance createInstance(File file);
}
