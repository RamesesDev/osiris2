/*
 * Filter.java
 *
 * Created on September 5, 2008, 9:13 AM
 *
 */
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jaycverg
 */
public class Filter implements javax.servlet.Filter {

    public static final int BUFFER_SIZE = 10240;
    public static final String RESOURCE_FOLDER = "META-INF";
    public static final String RESOURCE_KEY = "/a4j.res";
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        if( req.getHeader("content-type")!=null ) {
            if( req.getHeader("content-type").startsWith("multipart")) {
                servletRequest = new MultipartRequestWrapper( req );
            }
        }
        
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void destroy() {
    }

}
