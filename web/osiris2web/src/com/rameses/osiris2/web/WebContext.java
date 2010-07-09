/*
 * WebContext.java
 *
 * Created on May 24, 2010, 9:55 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.invoker.client.HttpClientManager;
import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.ExpressionProvider;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.WorkUnit;
import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.util.CacheProvider;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class WebContext implements Serializable {
    
    public static final String PAGE_SUFFIX = ".jsf";
    
    //<editor-fold defaultstate="collapsed" desc="  Faces  ">
    public static Application getApplication() {
        return FacesContext.getCurrentInstance().getApplication();
    }
    
    public static ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }
    
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }
    
    public static ServletContext getServletContext() {
        return (ServletContext) getExternalContext().getContext();
    }
    
    public static void redirect(String path) {
        try {
            getExternalContext().redirect(path);
        } catch (IOException ex) {
            throw new FacesException(ex);
        }
    }
    
    public static void redirect(Opener opr) {
        String targetId = opr.getViewId();
        String redirectPath = createActionUrl(targetId);
        redirect(redirectPath);
    }
    
    public static String getSessionId() {
        return (String) getRequest().getAttribute(Osiris2WebFilter.SESSION_ID);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  OsirisWeb  ">
    public static AppContext getAppContext() {
        ServletContext sc = getServletContext();
        return (AppContext)sc.getAttribute(AppContext.class.getName());
    }
    
    public static ExpressionProvider getExpressionProvider() {
        return getAppContext().getExpressionProvider();
    }
    
    public static SessionContext getSessionContext() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        return (SessionContext) ctx.get(SessionContext.class.getName());
    }
    
    public static void setSessionContext(SessionContext sessCtx) {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(SessionContext.class.getName(), sessCtx);
    }
    
    public static CacheProvider getCacheProvider() {
        return (CacheProvider) getServletContext().getAttribute(CacheProvider.class.getName());
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Security/Loader  ">
    public static Loader getLoader() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        return (Loader) ctx.get(Loader.class.getName());
    }
    
    public static void initLoader() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(Loader.class.getName(), new Loader());
    }
    
    public static void setUserPrincipal(Principal principal) {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(OsirisUserPrincipal.class.getName(), principal);
    }
    
    public static void setUserPrincipal(String username) {
        OsirisUserPrincipal p = new OsirisUserPrincipal();
        p.setUsername(username);
        setUserPrincipal(p);
    }
    
    public static Principal getUserPrincipal() {
        return (Principal) getRequest().getSession().getAttribute(OsirisUserPrincipal.class.getName());
    }
    
    public static void keepUri(String viewId) {
        HttpSession sess = getRequest().getSession();
        sess.setAttribute(Loader.SECURED_REQ_URL, viewId);
    }
    
    public static void keepUri(HttpServletRequest req) {
        StringBuffer sb = new StringBuffer();
        sb.append( req.getRequestURI() );
        if ( req.getQueryString() != null)
            sb.append("?" + req.getQueryString());
        
        keepUri( sb.toString() );
    }
    
    public static String getKeepedUri() {
        HttpSession sess = getRequest().getSession();
        return (String) sess.getAttribute(Loader.SECURED_REQ_URL);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Workunit  ">
    public static Map getWorkUnitInstanceMap() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        return (Map) ctx.get(WorkUnitInstance.class.getName());
    }
    
    public static void initWorkUnitInstanceMap() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(WorkUnitInstance.class.getName(), new HashMap());
    }
    
    public static WorkUnitInstance getCurrentWorkUnitInstance() {
        HttpServletRequest req = getRequest();
        return (WorkUnitInstance) req.getAttribute(WorkUnit.class.getName());
    }
    
    public static void setCurrentWorkUnitInstance(WorkUnitInstance wi) {
        HttpServletRequest req = getRequest();
        req.setAttribute(WorkUnit.class.getName(), wi);
    }
    
    public static String getViewId(WorkUnitInstance wi) {
        StringBuffer viewId = new StringBuffer();
        viewId.append("/" + wi.getModule().getName());
        viewId.append("/" + wi.getWorkunit().getName());
        viewId.append("/" + wi.getCurrentPage().getName());
        viewId.append(PAGE_SUFFIX);
        return viewId.toString();
    }
    
    public static UIViewRoot createView(String viewId) {
        ViewHandler viewHandler = getApplication().getViewHandler();
        return viewHandler.createView(FacesContext.getCurrentInstance(), viewId);
    }
    
    public static String createActionUrl(WorkUnitInstance wi) {
        String viewId = getViewId(wi);
        return createActionUrl(viewId);
    }
    
    public static String createActionUrl(String viewId) {
        ViewHandler viewHandler = getApplication().getViewHandler();
        return viewHandler.getActionURL(FacesContext.getCurrentInstance(), viewId);
    }
    
    public static WebInvokerProxy getInvokerProxy() {
        ServletContext ctx = getServletContext();
        WebInvokerProxy proxy = (WebInvokerProxy) ctx.getAttribute(WebInvokerProxy.class.getName());
        if ( proxy == null ) {
            proxy = new WebInvokerProxy();
            ctx.setAttribute(WebInvokerProxy.class.getName(), proxy);
        }
        return proxy;
    }
    
    public static HttpClientManager getHttpClientManager() {
        ServletContext ctx = getServletContext();
        HttpClientManager mgr = (HttpClientManager) ctx.getAttribute(HttpClientManager.class.getName());
        if ( mgr == null ) {
            mgr = new HttpClientManager();
            ctx.setAttribute(HttpClientManager.class.getName(), mgr);
        }
        return mgr;
    }
    //</editor-fold>
}
