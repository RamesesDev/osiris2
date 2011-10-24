/*
 * RequestFormatter.java
 *
 * Created on September 21, 2011, 2:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.web.common;


public interface RequestParameterFilter {
    Object filter(String val);
}
