package com.rameses.osiris2.web;
/*
 * Osiris2RestorePhase.java
 *
 * Created on May 17, 2010, 5:47 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import com.rameses.osiris2.AppContext;
import com.rameses.osiris2.SecurityProvider;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.WorkUnit;
import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.osiris2.web.util.PathParser;
import com.rameses.osiris2.web.util.ResourceUtil;
import com.rameses.osiris2.web.util.WebUtil;
import java.io.IOException;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author elmo
 */

public class Osiris2RestorePhase implements PhaseListener {
    
    
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
    
    //<editor-fold defaultstate="collapsed" desc="  BEFORE PHASE  ">
    public void beforePhase(PhaseEvent event) {
        FacesContext fContext = event.getFacesContext();
        WebContext webCtx = WebContext.getInstance();
        HttpServletRequest request = webCtx.getRequest();
        String reqMethod = request.getMethod().toLowerCase();
        if ( request.getParameter(WebUtil.LOGOUT_OUTCOME) != null ) {
            doLogout( WebUtil.removeHash(request.getRequestURI()) );
        }
        
        if ( fContext.getResponseComplete() ) return;
        
        //check if session context exists
        if( webCtx.getSessionContext() == null ) {
            AppContext ac = webCtx.getAppContext();
            
            SessionContext sessCtx = ac.createSession();
            sessCtx.setSecurityProvider( new Osiris2WebSecurityProvider() );
            webCtx.setSessionContext(sessCtx);
            
            webCtx.initWorkUnitInstanceMap();
            webCtx.initLoader();
        }
        
        //create the parser
        PathParser parser = new PathParser(request);
        
        // do nothing if parser.workunitId is null
        if(parser.getWorkunitId() == null) return;
        
        OsirisWebSessionContext sessCtx = (OsirisWebSessionContext) webCtx.getSessionContext();
        SecurityProvider secProvider = sessCtx.getSecurityProvider();
        Map wInstances = webCtx.getWorkUnitInstanceMap();
        WorkUnitInstance wi = (WorkUnitInstance) wInstances.get( parser.getWorkunitId() );
        
        //create workunit if null
        if(wi == null) {
            WorkUnit wu = sessCtx.getWorkUnit(parser.getWorkunitId());
            wi = wu.newInstance();
            wi.setId(parser.getWorkunitId());
            String scope = (String)wu.getProperties().get("scope");
            if(scope==null)scope = "session";
            if(scope.equalsIgnoreCase("session")) {
                wInstances.put( parser.getWorkunitId(), wi);
            }
        }
        
        // do nothing if workunitInstance is null
        if(wi == null) return;
        
        //check if workunit is secured
        String secured = (String) wi.getWorkunit().getProperties().get("secured");
        secured = (secured + "").trim().toLowerCase();
        if ( "true".equals(secured) ) {
            Loader loader = webCtx.getLoader();
            if ( !loader.isDone() ) {
                WorkUnitInstance loaderWi = loader.getWorkUnitInstance();
                if ( loaderWi != null ) {
                    if( webCtx.getKeepedUri() == null )
                        webCtx.keepUri(request);
                    
                    wi = loaderWi;
                }
                //reset session invokers after successfully login
                else {
                    sessCtx.resetInvokers();
                    loader.setDone(true);
                }
            }
            
            //check if has permission to view the page or invoke the action
            if ( "get".equals(reqMethod) && loader.isDone() ) {
                String permKey = parser.getPermissionKey();
                if ( !secProvider.checkPermission(permKey) ) {
                    String uri = request.getRequestURI();
                    try {
                        webCtx.getResponse().sendError(HttpServletResponse.SC_UNAUTHORIZED, uri);
                    } catch(Exception e){;}
                    fContext.responseComplete();
                }
            }
        }
        
        //add current workunit to the request
        webCtx.setCurrentWorkUnitInstance(wi);
        request.setAttribute(PathParser.class.getName(), parser);
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  AFTER PHASE  ">
    public void afterPhase(PhaseEvent event) {
        FacesContext fContext = event.getFacesContext();
        if ( fContext.getResponseComplete() ) return;
        
        WebContext webCtx = WebContext.getInstance();
        WorkUnitInstance wi = webCtx.getCurrentWorkUnitInstance();
        if ( wi == null ) return;
        
        HttpServletRequest req = webCtx.getRequest();
        String reqMethod = req.getMethod().toLowerCase();
        PathParser parser = (PathParser) req.getAttribute(PathParser.class.getName());
        
        if ( !Loader.isLoaderWorkUnit(wi) ) {
            if ( "get".equals(reqMethod) ) {
                processGetRequest(parser, wi);
            } else {
                processPostRequest(parser, wi);
            }
        }
        
        //check if not response complete if is fired
        //routines above may abort processing
        if( !fContext.getResponseComplete() ) {
            
            //add AppContext hashcode in the viewId so that the caching of templates
            //would happen for every instance of AppContext
            SessionContext sessCtx = webCtx.getSessionContext();
            String viewId = webCtx.getViewId(wi);
            viewId = WebUtil.addHash(viewId, sessCtx.hashCode());
            String actualViewId = fContext.getViewRoot().getViewId();
            if ( !actualViewId.equals(viewId)) {
                fContext.getViewRoot().setViewId(viewId);
            }
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="  helper methods  ">
    private void processGetRequest(PathParser parser, WorkUnitInstance wi) {
        WebContext webCtx = WebContext.getInstance();
        HttpServletRequest req = webCtx.getRequest();
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpSession sess = webCtx.getRequest().getSession();
        Opener opener = (Opener) sess.getAttribute(Opener.class.getName());
        if ( opener != null ) {
            sess.removeAttribute(Opener.class.getName());
        }
        // inject values if request has parameters
        for ( Object key: req.getParameterMap().keySet() ) {
            try {
                wi.setProperty(key+"", req.getParameter(key+""));
            }catch(Exception ign) {;}
        }
        // inject opener parameters
        // this could happen if this request is a result of a navigation redirect
        if ( opener != null ) {
            wi.setProperties(opener.getParams());
        }
        
        // invoke method if has action
        if ( parser.getAction() != null ) {
            String action = parser.getAction();
            String mbExpr = "#{Controller." + action + "}";
            try {
                MethodBinding mb = ctx.getApplication().createMethodBinding(mbExpr, null);
                Object outcome = mb.invoke(ctx, null);
                
                if ( outcome instanceof Opener ) {
                    Opener opr = (Opener) outcome;
                    opr.setModuleName(wi.getModule().getName());
                    webCtx.redirect(opr);
                    ctx.responseComplete();
                    
                } else if ( outcome instanceof String && !isEmpty(outcome.toString()) ) {
                    String str = outcome.toString();
                    if( WebUtil.LOGOUT_OUTCOME.equals(str) ) {
                        StringBuffer path = new StringBuffer( webCtx.getViewId(wi) );
                        if( req.getContextPath() != null )
                            path.insert(0, req.getContextPath() );
                        
                        doLogout( path.toString() );
                    } else {
                        wi.setCurrentPage(outcome+"");
                    }
                    
                } else if ( opener != null && opener.getOutcome() != null ) {
                    wi.setCurrentPage(opener.getOutcome());
                    
                } else if ( outcome instanceof WebResource ) {
                    ResourceUtil.renderResource( (WebResource) outcome );
                    ctx.responseComplete();
                    
                } else {
                    wi.setCurrentPage(wi.getDefaultPage().getName());
                }
            } catch(Exception ign) {
                //check if the excepton is caused by a page request
                if ( ign.getCause() instanceof NoSuchMethodException || ign instanceof ReferenceSyntaxException ) {
                    Object p = wi.getWorkunit().getPages().get(action);
                    if ( p != null ) {
                        wi.setCurrentPage(action);
                    } else {
                        String path = req.getRequestURI();
                        int errorCode = HttpServletResponse.SC_NOT_FOUND;
                        try {
                            webCtx.getResponse().sendError(errorCode, path);
                        } catch (IOException ex) {;}
                        ctx.responseComplete();
                    }
                } else {
                    throw new AbortProcessingException(ign);
                }
            }
        } else {
            wi.setCurrentPage(wi.getDefaultPage().getName());
        }
    }
    
    private boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
    
    private void processPostRequest(PathParser parser, WorkUnitInstance wi) {
        String scope = (String) wi.getWorkunit().getProperties().get("scope");
        scope = (scope + "").trim().toLowerCase();
        if ( scope.equals("request") ) {
            wi.setCurrentPage(parser.getAction());
        }
    }
    
    private void doLogout(String redirectPath) {
        WebContext webCtx = WebContext.getInstance();
        webCtx.getRequest().getSession().invalidate();
        webCtx.redirect( redirectPath );
        FacesContext.getCurrentInstance().responseComplete();
    }
    //</editor-fold>
    
}



