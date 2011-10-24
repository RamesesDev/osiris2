/*
 * RequestWrapper.java
 * Created on September 20, 2011, 1:01 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jzamss
 */
public class RequestWrapper implements HttpServletRequest {
    
    private HttpServletRequest req;
    
    /** Creates a new instance of RequestWrapper */
    public RequestWrapper(HttpServletRequest r) {
        this.req = r;
    }

    public String getAuthType() {
        return req.getAuthType();
    }

    public Cookie[] getCookies() {
        return req.getCookies();
    }

    public long getDateHeader(String name) {
        return req.getDateHeader(name);
    }

    public String getHeader(String name) {
        return req.getHeader(name);
    }

    public Enumeration getHeaders(String name) {
        return req.getHeaders(name);
    }

    public Enumeration getHeaderNames() {
        return req.getHeaderNames();
    }

    public int getIntHeader(String name) {
        return req.getIntHeader(name);
    }

    public String getMethod() {
        return req.getMethod();
    }

    public String getPathInfo() {
        return req.getPathInfo();
    }

    public String getPathTranslated() {
        return req.getPathTranslated();
    }

    public String getContextPath() {
        return req.getContextPath();
    }

    public String getQueryString() {
        return req.getQueryString();
    }

    public String getRemoteUser() {
        return req.getRemoteUser();
    }

    public boolean isUserInRole(String role) {
        return req.isUserInRole(role);
    }

    public Principal getUserPrincipal() {
        return req.getUserPrincipal();
    }

    public String getRequestedSessionId() {
        return req.getRequestedSessionId();
    }

    public String getRequestURI() {
        return req.getRequestURI();
    }

    public StringBuffer getRequestURL() {
        return req.getRequestURL();
    }

    public String getServletPath() {
        return req.getServletPath();
    }

    public HttpSession getSession(boolean create) {
        return req.getSession(create);
    }

    public HttpSession getSession() {
        return req.getSession();
    }

    public boolean isRequestedSessionIdValid() {
        return req.isRequestedSessionIdValid();
    }

    public boolean isRequestedSessionIdFromCookie() {
        return req.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromURL() {
        return req.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdFromUrl() {
        return req.isRequestedSessionIdFromUrl();
    }

    public Object getAttribute(String name) {
        return req.getAttribute(name);
    }

    public Enumeration getAttributeNames() {
        return req.getAttributeNames();
    }

    public String getCharacterEncoding() {
        return req.getCharacterEncoding();
    }

    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        req.setCharacterEncoding(env);
    }

    public int getContentLength() {
        return req.getContentLength();
    }

    public String getContentType() {
        return req.getContentType();
    }

    public ServletInputStream getInputStream() throws IOException {
        return req.getInputStream();
    }

    public String getParameter(String name) {
        return req.getParameter(name);
    }

    public Enumeration getParameterNames() {
        return req.getParameterNames();
    }
    
    public String[] getParameterValues(String name) {
        return req.getParameterValues(name); 
    }

    public Map getParameterMap() {
        return req.getParameterMap();
    }

    public String getProtocol() {
        return req.getProtocol();
    }

    public String getScheme() {
        return req.getScheme();
    }

    public String getServerName() {
        return req.getServerName();
    }

    public int getServerPort() {
        return req.getServerPort();
    }

    public BufferedReader getReader() throws IOException {
        return req.getReader();
    }

    public String getRemoteAddr() {
        return req.getRemoteAddr();
    }

    public String getRemoteHost() {
        return req.getRemoteHost();
    }

    public void setAttribute(String name, Object o) {
        req.setAttribute(name, o);
    }

    public void removeAttribute(String name) {
        req.removeAttribute(name);
    }

    public Locale getLocale() {
        return req.getLocale();
    }

    public Enumeration getLocales() {
        return req.getLocales();
    }

    public boolean isSecure() {
        return req.isSecure();
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return req.getRequestDispatcher(path);
    }

    public String getRealPath(String path) {
        return req.getRealPath(path);
    }

    public int getRemotePort() {
        return req.getRemotePort();
    }

    public String getLocalName() {
        return req.getLocalName();
    }

    public String getLocalAddr() {
        return req.getLocalAddr();
    }

    public int getLocalPort() {
        return req.getLocalPort();
    }
    
    
    
}
