/*
 * Osiris2RequestWrapper.java
 *
 * Created on May 19, 2010, 3:28 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


public class Osiris2RequestWrapper extends HttpServletRequestWrapper {
    
    private Principal principal;
    
    public Osiris2RequestWrapper(Principal principal, HttpServletRequest request) {
        super(request);
        this.principal = principal;
    }
    
    public Principal getUserPrincipal() {
        return principal;
    }
}
