package com.rameses.web.fileupload;
import java.io.IOException;
import java.io.Writer;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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
    
    private static final String UPLOAD_STATUS = "fileupload.status";
    
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        
        if( req.getParameter(UPLOAD_STATUS) != null ) {
            String fieldId = req.getParameter(UPLOAD_STATUS);
            Writer w = resp.getWriter();
            ProgressStatus status = (ProgressStatus) req.getSession().getAttribute(fieldId);
            if( status != null ) {
                w.write( status.toJSON() );
            } else {
                w.write("{}");
            }
        } else {
            boolean isMultipart = ServletFileUpload.isMultipartContent(req);
            if( isMultipart ) {
                req = new MultipartRequest( req );
                String fieldId = req.getParameter("file_id");
                Object file = ((MultipartRequest)req).getFileParameter(fieldId);
                req.setAttribute("FILE", file);
            }
            
            chain.doFilter(req, resp);
            
            if( isMultipart ) {
                String fieldId = req.getParameter("file_id");
                if( fieldId != null ) {
                    req.getSession().removeAttribute(fieldId);
                }
            }
        }
    }
    
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    public void destroy() {
    }
    
}
