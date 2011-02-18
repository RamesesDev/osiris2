/*
 * WebContext.java
 *
 * Created on May 24, 2010, 9:55 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.common.CacheProvider;
import com.rameses.invoker.client.HttpClientManager;
import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.ExpressionProvider;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.WorkUnit;
import com.rameses.osiris2.WorkUnitInstance;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.HashMap;
import java.util.Hashtable;
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
    
    private static ThreadLocal<WebContext> local = new ThreadLocal() {
        public Object initialValue() {
            return new WebContext();
        }
    };
    
    public static WebContext getInstance() {
        return local.get();
    }
    
    
    public static final String PAGE_SUFFIX = ".jsf";
    
    //<editor-fold defaultstate="collapsed" desc="  Faces  ">
    public Application getApplication() {
        return FacesContext.getCurrentInstance().getApplication();
    }
    
    public ExternalContext getExternalContext() {
        return FacesContext.getCurrentInstance().getExternalContext();
    }
    
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) getExternalContext().getRequest();
    }
    
    public HttpSession getSession() {
        return getRequest().getSession();
    }
    
    public HttpServletResponse getResponse() {
        return (HttpServletResponse) getExternalContext().getResponse();
    }
    
    public ServletContext getServletContext() {
        return (ServletContext) getExternalContext().getContext();
    }
    
    public void redirect(String path) {
        try {
            getExternalContext().redirect(path);
        } catch (IOException ex) {
            throw new FacesException(ex);
        }
    }
    
    public void redirect(Opener opr) {
        String targetId = opr.getViewId();
        String redirectPath = createActionUrl(targetId);
        redirect(redirectPath);
    }
    
    public String getSessionId() {
        return (String) getRequest().getAttribute(Osiris2WebFilter.SESSION_ID);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  OsirisWeb  ">
    public AppContext getAppContext() {
        ServletContext sc = getServletContext();
        return (AppContext)sc.getAttribute(AppContext.class.getName());
    }
    
    public ExpressionProvider getExpressionProvider() {
        return getAppContext().getExpressionProvider();
    }
    
    public SessionContext getSessionContext() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        return (SessionContext) ctx.get(SessionContext.class.getName());
    }
    
    public void setSessionContext(SessionContext sessCtx) {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(SessionContext.class.getName(), sessCtx);
    }
    
    public CacheProvider getCacheProvider() {
        return (CacheProvider) getServletContext().getAttribute(CacheProvider.class.getName());
    }
    
    public Map getEnv() {
        Map properties = getSessionContext().getProperties();
        if( !properties.containsKey("WEB_SESSION_ENV") ) {
            properties.put("WEB_SESSION_ENV", new Hashtable());
        }
        return (Map) properties.get("WEB_SESSION_ENV");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Security/Loader  ">
    public Loader getLoader() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        return (Loader) ctx.get(Loader.class.getName());
    }
    
    public void initLoader() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(Loader.class.getName(), new Loader());
    }
    
    public void setUserPrincipal(Principal principal) {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(OsirisUserPrincipal.class.getName(), principal);
    }
    
    public void setUserPrincipal(String username) {
        OsirisUserPrincipal p = new OsirisUserPrincipal();
        p.setUsername(username);
        setUserPrincipal(p);
    }
    
    public Principal getUserPrincipal() {
        return (Principal) getRequest().getSession().getAttribute(OsirisUserPrincipal.class.getName());
    }
    
    public void keepUri(String viewId) {
        HttpSession sess = getRequest().getSession();
        sess.setAttribute(Loader.SECURED_REQ_URL_KEY, viewId);
    }
    
    public void keepUri(HttpServletRequest req) {
        StringBuffer sb = new StringBuffer();
        sb.append( req.getRequestURI() );
        if ( req.getQueryString() != null)
            sb.append("?" + req.getQueryString());
        
        keepUri( sb.toString() );
    }
    
    public String getKeepedUri() {
        HttpSession sess = getRequest().getSession();
        return (String) sess.getAttribute(Loader.SECURED_REQ_URL_KEY);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  Workunit  ">
    public Map getWorkUnitInstanceMap() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        return (Map) ctx.get(WorkUnitInstance.class.getName());
    }
    
    public void initWorkUnitInstanceMap() {
        CacheProvider.CacheContext ctx = getCacheProvider().getContext(null);
        ctx.put(WorkUnitInstance.class.getName(), new HashMap());
    }
    
    public WorkUnitInstance getCurrentWorkUnitInstance() {
        HttpServletRequest req = getRequest();
        return (WorkUnitInstance) req.getAttribute(WorkUnit.class.getName());
    }
    
    public void setCurrentWorkUnitInstance(WorkUnitInstance wi) {
        HttpServletRequest req = getRequest();
        req.setAttribute(WorkUnit.class.getName(), wi);
    }
    
    public String getViewId(WorkUnitInstance wi) {
        StringBuffer viewId = new StringBuffer();
        viewId.append("/" + wi.getModule().getName());
        viewId.append("/" + wi.getWorkunit().getName());
        viewId.append("/" + wi.getCurrentPage().getName());
        viewId.append(PAGE_SUFFIX);
        return viewId.toString();
    }
    
    public UIViewRoot createView(String viewId) {
        ViewHandler viewHandler = getApplication().getViewHandler();
        return viewHandler.createView(FacesContext.getCurrentInstance(), viewId);
    }
    
    public String createActionUrl(WorkUnitInstance wi) {
        String viewId = getViewId(wi);
        return createActionUrl(viewId);
    }
    
    public String createActionUrl(String viewId) {
        ViewHandler viewHandler = getApplication().getViewHandler();
        return viewHandler.getActionURL(FacesContext.getCurrentInstance(), viewId);
    }
    
    public WebInvokerProxy getInvokerProxy() {
        OsirisWebSessionContext sessCtx = (OsirisWebSessionContext) getSessionContext();
        Map properties = sessCtx.getProperties();
        
        WebInvokerProxy proxy = (WebInvokerProxy) properties.get(WebInvokerProxy.class.getName());
        if ( proxy == null ) {
            proxy = new WebInvokerProxy();
            properties.put(WebInvokerProxy.class.getName(), proxy);
        }
        return proxy;
    }
    
    public HttpClientManager getHttpClientManager() {
        OsirisWebAppContext appCtx = (OsirisWebAppContext) getAppContext();
        Map properties = appCtx.getProperties();
        
        HttpClientManager mgr = (HttpClientManager) properties.get(HttpClientManager.class.getName());
        if ( mgr == null ) {
            mgr = new HttpClientManager();
            properties.put(HttpClientManager.class.getName(), mgr);
        }
        return mgr;
    }
    
    public ScriptProvider getScriptProvider() {
        OsirisWebSessionContext sessCtx = (OsirisWebSessionContext) getSessionContext();
        Map properties = sessCtx.getProperties();
        
        ScriptProvider provider = (ScriptProvider) properties.get(ScriptProvider.class.getName());
        if ( provider == null ) {
            provider = new ScriptProvider();
            properties.put(ScriptProvider.class.getName(), provider);
        }
        return provider;
    }
    //</editor-fold>
}
