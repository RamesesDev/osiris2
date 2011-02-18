/*
 * Osiris2NavigationHandler.java
 *
 * Created on May 21, 2010, 12:52 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.osiris2.web.util.WebUtil;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Osiris2NavigationHandler extends NavigationHandler {
    
    private NavigationHandler baseHandler;
    
    public Osiris2NavigationHandler(NavigationHandler base) {
        this.baseHandler = base;
    }
    
    public void handleNavigation(FacesContext ctx, String fromAction, String outcome) {
        WebContext webCtx = WebContext.getInstance();
        WorkUnitInstance wi = webCtx.getCurrentWorkUnitInstance();
        
        if ( wi != null ) {
            if ( outcome != null && outcome.trim().length() > 0 ) {
                String redirectPath = null;
                if ( WebUtil.LOGOUT_OUTCOME.equals(outcome) ) {
                    webCtx.getRequest().getSession().invalidate();
                    redirectPath = webCtx.createActionUrl(wi);
                } else if ( WebUtil.CLOSE_OUTCOME.equals(outcome) && Loader.isLoaderWorkUnit(wi) ) {
                    Loader loader = webCtx.getLoader();
                    loader.removeCurrentInvoker();
                    redirectPath = webCtx.getKeepedUri();
                } else if ( WebUtil.OPENER_OUTCOME.equals(outcome) ) {
                    HttpSession sess = webCtx.getRequest().getSession();
                    Opener op = (Opener) sess.getAttribute(Opener.class.getName());
                    if ( op == null ) return;
                    redirectPath = webCtx.createActionUrl(op.getViewId());
                } else {
                    wi.setCurrentPage(outcome);
                    StringBuffer sb = new StringBuffer();
                    sb.append("/" + wi.getWorkunit().getModule().getName());
                    sb.append("/" + wi.getWorkunit().getName() + "/");
                    sb.append(outcome);
                    sb.append(WebContext.PAGE_SUFFIX);
                    redirectPath = webCtx.createActionUrl(sb.toString());
                }
                
                webCtx.redirect(redirectPath);
            }
        } else {
            baseHandler.handleNavigation(ctx, fromAction, outcome);
        }
    }
    
}
