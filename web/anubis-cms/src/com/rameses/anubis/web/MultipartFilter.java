/*
 * MultipartFilter.java
 *
 * Created on July 11, 2012, 2:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.anubis.web;

import com.rameses.anubis.AnubisContext;
import com.rameses.anubis.JsonUtil;
import com.rameses.anubis.Project;
import com.rameses.anubis.fileupload.MultipartRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author Elmo
 */
public class MultipartFilter implements javax.servlet.Filter {
    
    private static final String UPLOAD_STATUS = "fileupload.status";
    
    private FilterConfig config;
    
    /** Creates a new instance of MultipartFilter */
    public MultipartFilter() {
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
    }
    
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        
        //-- File upload status support
        //System.out.println("checking: " + hreq.getParameter(UPLOAD_STATUS));
        if( hreq.getParameter(UPLOAD_STATUS) != null ) {
            
            Project p = AnubisContext.getCurrentContext().getProject();
            
            Map param = new HashMap();
            String requestid = hreq.getParameter(UPLOAD_STATUS);
            System.out.println("requestid " + requestid);
            param.put("requestid", requestid);
            Object status = null;
            try {
                status = p.getActionManager().getActionCommand(CmsWebConstants.FILE_UPLOAD_CHECK_STATUS_CMD).execute(param, null);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            
            Writer w = resp.getWriter();
            if( status != null ) {
                w.write( JsonUtil.toString(status) );
            } else {
                w.write("{}");
            }
        }
        //-- For normal request
        else {
            boolean isMultipart = ServletFileUpload.isMultipartContent(hreq);
            if( isMultipart ) {
                hreq = new MultipartRequest( hreq, config.getServletContext() );
            }
            chain.doFilter(hreq, resp);
        }
    }
    
    public void destroy() {
    }
    
}
