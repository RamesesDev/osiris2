/*
 * RuleDomainProvider.java
 *
 * Created on August 1, 2010, 6:57 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.rules;

/**
 * provides facility for serving rule domains
 */
public interface RuleDomainProvider {
    
    RuleDomain[] getRuleDomains();
    
}
