/*
 * Osiris2WebFilter.java
 *
 * Created on May 19, 2010, 3:22 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.Module;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.WorkUnit;
import com.rameses.osiris2.web.util.GZIPResponseWrapper;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Osiris2WebFilter implements Filter {
    
    public static final int BUFFER_SIZE = 10240;
    public static final String SESSION_ID = Osiris2WebFilter.class.getName()+"_sessId";
    
    private FilterConfig filterConfig;
    
    
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();
        Principal principal = (Principal) session.getAttribute(OsirisUserPrincipal.class.getName());
        
//        String sessId = getSessionId(request);
//        if ( sessId == null ) {
//            sessId = "SESSID-" + new UID();
//        }
//        Cookie cookie = new Cookie(Osiris2WebFilter.class.getName(), sessId);
//        cookie.setPath("/");
//        cookie.setMaxAge(86400);
//        response.addCookie(cookie);
        
        String ae = request.getHeader("accept-encoding");
        if (ae != null && ae.indexOf("gzip") != -1) {
            response = new GZIPResponseWrapper(response);
        }
        
        String path = request.getServletPath();
        String regex = "^/[^/]*/(.*\\.[^/]+)$";
        Matcher m = Pattern.compile(regex).matcher(path);
        if ( !isJsfPage(path) && m.matches() ) {
            PathParser p = new PathParser(path);
            SessionContext ctx = getSessionContext(request);
            try {
                Module module = ctx.getModule( p.getModule() ); //throws IllegalStateException if module is null
                if ( !checkAllowed( p, principal, ctx ) ) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, request.getRequestURI());
                } else {
                    renderResource( m.group(1), request, response, module);
                }
                return;
            } catch(IllegalStateException e) {;}
        }
        
        if ( principal != null ) {
            chain.doFilter(new Osiris2RequestWrapper(principal, request), response);
        } else {
            chain.doFilter(req, response);
        }
        
        if ( response instanceof GZIPResponseWrapper ) {
            ((GZIPResponseWrapper) response).finishResponse();
        }
    }
    
    public void destroy() {
    }
    
    private String getSessionId(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if ( cookies == null ) return null;
        
        for ( Cookie c : cookies ) {
            if ( c.getName().equals(Osiris2WebFilter.class.getName()) ) {
                return c.getValue();
            }
        }
        return null;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private boolean isJsfPage(String path) {
        return path.endsWith(".jsf") || path.endsWith(".jsp");
    }
    
    private boolean checkAllowed(PathParser p, Principal principal, SessionContext ctx) {
        WorkUnit wu = ctx.getWorkUnit(p.getWorkunitId());
        if ( wu == null ) return true;
        String secured = (String) wu.getProperties().get("secured");
        if ( secured == null ) return true;
        secured = secured.toLowerCase().trim();
        if ( secured.equals("true") && principal != null) return true;
        return false;
    }
    
    private SessionContext getSessionContext(HttpServletRequest req) {
        HttpSession sess = req.getSession(true);
        ServletContext sc = sess.getServletContext();
        AppContext ctx = (AppContext) sc.getAttribute(AppContext.class.getName());;
        SessionContext sessCtx = (SessionContext) sess.getAttribute(SessionContext.class.getName());
        if ( sessCtx != null ) {
            return sessCtx;
        } else {
            return ctx.createSession();
        }
    }
    
    private void renderResource(String path, HttpServletRequest req, HttpServletResponse resp, Module module) {
        InputStream is = module.getResourceAsStream(path);
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        if ( is != null ) {
            try {
                String mimeType = req.getSession().getServletContext().getMimeType(path);
                resp.setContentType(mimeType);
                if ( isCacheable(path) ) {
                    resp.addHeader("Cache-Control", "max-age=86400");
                    resp.addHeader("Cache-Control", "public");
                }
                
                byte []buffer = new byte[BUFFER_SIZE];
                bis = new BufferedInputStream(is, BUFFER_SIZE);
                bos = new BufferedOutputStream(resp.getOutputStream(), BUFFER_SIZE);
                
                int bytesRead = -1;
                while ( (bytesRead = bis.read(buffer)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.flush();
                
            } catch(Exception ex) {
                throw new IllegalStateException(ex);
            } finally {
                try { bis.close(); } catch(Exception e){;}
                try { bos.close(); } catch(Exception e){;}
            }
        } else {
            try {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, req.getRequestURI());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private boolean isCacheable(String path) {
        String exp = ".*\\.(?:js|css|jpg|jpeg|gif|png|bmp|swf)$";
        Matcher m = Pattern.compile(exp, Pattern.CASE_INSENSITIVE).matcher(path);
        return m.matches();
    }
    //</editor-fold>
    
}
