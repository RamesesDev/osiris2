/*
 * JsonFilter.java
 * Created on September 20, 2011, 11:53 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.invoker.server;

import com.rameses.server.common.JsonUtil;
import com.rameses.util.ExceptionManager;
import com.rameses.util.SealedMessage;
import com.rameses.web.common.RequestParser;
import com.rameses.web.common.RequestParameterFilter;
import com.rameses.web.common.ResponseWrapper;
import com.rameses.web.common.ServletUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author emn
 */
public class JsonFilter implements Filter {
    
    private FilterConfig filter;
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filter = filterConfig;
    }
    
    public void destroy() {
        this.filter = null;
    }
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        PrintWriter pw = null;
        HttpServletResponse hres = (HttpServletResponse) response;
        request.setAttribute(RequestParser.APP_FILTER, "/json/");
        request.setAttribute( RequestParameterFilter.class.getName(), new JsonParamFilter() );
        try {
            bos = new ByteArrayOutputStream();
            chain.doFilter(request, new ResponseWrapper(hres, bos));
            byte[] bytes = bos.toByteArray();
            if(bytes.length>0) {
                bis = new ByteArrayInputStream(bytes);
                ois = new ObjectInputStream(bis);
                Object o = ois.readObject();
                boolean encrypted = false;
                if( o instanceof SealedMessage ) {
                    encrypted = true;
                    o = ((SealedMessage)o).getMessage();
                }
                String s = JsonUtil.toString(o);
                if(!encrypted ) {
                    ServletUtils.writeText( hres, s);
                } else {
                    ServletUtils.writeObject( hres, new SealedMessage(s));
                }
            }
        } catch(Exception e) {
            Exception orig = ExceptionManager.getOriginal(e);
            hres.setHeader("Error-Message", orig.getMessage());
            hres.sendError(500, orig.getMessage());
        } finally {
            try {bos.close();}catch(Exception ign){;}
            try {pw.close();}catch(Exception ign){;}
            try {bis.close();}catch(Exception ign){;}
            try {ois.close();}catch(Exception ign){;}
        }
        
    }
    
    public static class JsonParamFilter implements RequestParameterFilter {
        
        public Object filter(String val) {
            if(val.trim().startsWith("{") || val.trim().startsWith("[") ) {
                return JsonUtil.toObject(val);
            }
            else {
                return val;
            }
        }
        
    }
    
}
