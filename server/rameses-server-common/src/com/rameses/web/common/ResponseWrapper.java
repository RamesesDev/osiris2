/*
 * ResponseWrapper.java
 * Created on September 20, 2011, 1:45 PM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.web.common;

import com.rameses.server.common.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jzamss
 */
public class ResponseWrapper implements HttpServletResponse {
    
    private HttpServletResponse res;
    private ServletOutputStream out;
    private PrintWriter writer;
    
    /** Creates a new instance of ResponseWrapper */
    public ResponseWrapper(HttpServletResponse r) {
        this(r,null,null);
    }
    
    public ResponseWrapper(HttpServletResponse r, OutputStream o) {
        this(r,o,null);
    }
    public ResponseWrapper(HttpServletResponse r, OutputStream o, PrintWriter w ) {
        this.res = r;
        if(o!=null) {
            out = new OutputStreamWrapper(o);
        }
        if(w!=null) {
            writer = w;
        }
    }
    
    public void addCookie(Cookie cookie) {
        this.res.addCookie(cookie);
    }
    
    public boolean containsHeader(String name) {
        return this.res.containsHeader(name);
    }
    
    public String encodeURL(String url) {
        return this.res.encodeURL(url);
    }
    
    public String encodeRedirectURL(String url) {
        return this.res.encodeRedirectURL(url);
    }
    
    public String encodeUrl(String url) {
        return this.res.encodeUrl(url);
    }
    
    public String encodeRedirectUrl(String url) {
        return this.res.encodeRedirectUrl(url);
    }
    
    public void sendError(int sc, String msg) throws IOException {
        this.res.sendError(sc, msg);
    }
    
    public void sendError(int sc) throws IOException {
        this.res.sendError(sc);
    }
    
    public void sendRedirect(String location) throws IOException {
        this.res.sendRedirect(location);
    }
    
    public void setDateHeader(String name, long date) {
        this.res.setDateHeader(name, date);
    }
    
    public void addDateHeader(String name, long date) {
        this.res.addDateHeader(name, date);
    }
    
    public void setHeader(String name, String value) {
        this.res.setHeader( name, value);
    }
    
    public void addHeader(String name, String value) {
        this.res.addHeader(name, value);
    }
    
    public void setIntHeader(String name, int value) {
        this.res.setIntHeader(name,value);
    }
    
    public void addIntHeader(String name, int value) {
        this.res.addIntHeader(name, value);
    }
    
    public void setStatus(int sc) {
        this.res.setStatus(sc);
    }
    
    public void setStatus(int sc, String sm) {
        this.res.setStatus(sc, sm);
    }
    
    public String getCharacterEncoding() {
        return this.res.getCharacterEncoding();
    }
    
    public String getContentType() {
        return this.res.getContentType();
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
        if(this.out!=null) {
            return this.out;
        }    
        else
            return res.getOutputStream();
    }
    
    public PrintWriter getWriter() throws IOException {
        if(this.writer!=null)
            return this.writer;
        else
            return res.getWriter();
    }
    
    public void setCharacterEncoding(String charset) {
        this.res.setCharacterEncoding(charset);
    }
    
    public void setContentLength(int len) {
        this.res.setContentLength(len);
    }
    
    public void setContentType(String type) {
        this.res.setContentType(type);
    }
    
    public void setBufferSize(int size) {
        this.res.setBufferSize(size);
    }
    
    public int getBufferSize() {
        return this.res.getBufferSize();
    }
    
    public void flushBuffer() throws IOException {
        this.res.flushBuffer();
    }
    
    public void resetBuffer() {
        this.res.resetBuffer();
    }
    
    public boolean isCommitted() {
        return this.res.isCommitted();
    }
    
    public void reset() {
        this.res.reset();
    }
    
    public void setLocale(Locale loc) {
        this.res.setLocale(loc);
    }
    
    public Locale getLocale() {
        return this.res.getLocale();
    }
    
    private class OutputStreamWrapper extends ServletOutputStream {
        private OutputStream out;
        
        public OutputStreamWrapper(OutputStream o) {
            this.out = o;
        }
        
        public void write(int b) throws IOException {
            this.out.write( b );
        }
        
    }
    
}
