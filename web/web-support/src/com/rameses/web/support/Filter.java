package com.rameses.web.support;

import com.rameses.web.fileupload.MultipartRequest;
import com.rameses.web.fileupload.ProgressStatus;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author jaycverg
 */
public class Filter implements javax.servlet.Filter {
    
    public static final String APP_CONF = "APP_CONF";
    
    private static final String UPLOAD_STATUS = "fileupload.status";
    private static final String MOD_DIR = "/modules";
    
    protected FilterConfig filterConfig;
    
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    
    public void destroy() {
    }
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException 
    {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        
        Map conf = (Map) req.getAttribute(APP_CONF);
        if( conf == null ) {
            conf = new HashMap();
            req.setAttribute(APP_CONF, conf);
        }
        
        //load ServletContext's parameters to APP_CONF
        ServletContext sctx = filterConfig.getServletContext();
        Enumeration<String> en = sctx.getInitParameterNames();
        while( en.hasMoreElements() ) {
            String key = en.nextElement();
            if( conf.get(key)==null ) {
                conf.put(key, sctx.getInitParameter(key));
            }
        }
        
        String path = req.getServletPath();
                        
        //-- For module support
        if( path.contains(":") ) 
        {
            path = MOD_DIR + path.replace(":", "/");
            RequestDispatcher reqDisp = req.getRequestDispatcher( path );
            reqDisp.forward(req, resp);
        }        
        //-- File upload status support
        else if( req.getParameter(UPLOAD_STATUS) != null ) 
        {
            String fieldId = req.getParameter(UPLOAD_STATUS);
            Writer w = resp.getWriter();
            ProgressStatus status = (ProgressStatus) req.getSession().getAttribute(fieldId);
            if( status != null ) {
                w.write( status.toJSON() );
            } else {
                w.write("{}");
            }
        }
        //-- For normal request
        else 
        {
            String ae = req.getHeader("accept-encoding");
            if (ae != null && ae.indexOf("gzip") != -1) {
                resp = new ResponseWrapper(resp);
            }
            
            boolean isMultipart = ServletFileUpload.isMultipartContent(req);
            if( isMultipart ) {
                req = new MultipartRequest( req );
                String fieldId = req.getParameter("file_id");
                Object file = ((MultipartRequest)req).getFileParameter(fieldId);
                req.setAttribute("FILE", file);
            }
            
            if( isCacheable(req, resp) ) {
                resp.addHeader("Cache-Control", "max-age=86400");
                resp.addHeader("Cache-Control", "public");
            }

            chain.doFilter(req, resp);
            
            if( isMultipart ) {
                String fieldId = req.getParameter("file_id");
                if( fieldId != null ) {
                    req.getSession().removeAttribute(fieldId);
                }
            }
            
            if ( resp instanceof ResponseWrapper ) {
                ((ResponseWrapper) resp).finishResponse();
            }
        }
    }
    
    
    private static final Pattern CACHEABLE_PATTERN = Pattern.compile(".*\\.(jpg|jpeg|gif|png|css|js|bmp)$");
    
    private boolean isCacheable(HttpServletRequest req, HttpServletResponse resp) 
    {
        String path = req.getRequestURI();
        if( CACHEABLE_PATTERN.matcher(path.toLowerCase()).matches() ) 
            return true;
        
        String mime = resp.getContentType();
        if( mime != null ) {
            if( mime.contains("image") || mime.contains("javascript") || mime.contains("css") ) return true;
        }
        
        return false;
    }
}
