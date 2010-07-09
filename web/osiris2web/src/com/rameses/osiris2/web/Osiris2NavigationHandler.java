/*
 * Osiris2NavigationHandler.java
 *
 * Created on May 21, 2010, 12:52 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.WorkUnitInstance;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

public class Osiris2NavigationHandler extends NavigationHandler {
    
    private NavigationHandler baseHandler;
    
    public Osiris2NavigationHandler(NavigationHandler base) {
        this.baseHandler = base;
    }
    
    public void handleNavigation(FacesContext ctx, String fromAction, String outcome) {
        WorkUnitInstance wi = WebContext.getCurrentWorkUnitInstance();
        
        if ( wi != null ) {
            if ( outcome != null && outcome.trim().length() > 0 ) {
                String redirectPath = null;
                if ( WebUtil.LOGOUT_OUTCOME.equals(outcome) ) {
                    WebContext.getRequest().getSession().invalidate();
                    redirectPath = WebContext.createActionUrl(wi);
                } else if ( WebUtil.CLOSE_OUTCOME.equals(outcome) && Loader.isLoaderWorkUnit(wi) ) {
                    Loader loader = WebContext.getLoader();
                    loader.removeCurrentInvoker();
                    redirectPath = WebContext.getKeepedUri();
                } else if ( WebUtil.OPENER_OUTCOME.equals(outcome) ) {
                    HttpSession sess = WebContext.getRequest().getSession();
                    Opener op = (Opener) sess.getAttribute(Opener.class.getName());
                    if ( op == null ) return;
                    redirectPath = WebContext.createActionUrl(op.getViewId());
                } else {
                    wi.setCurrentPage(outcome);
                    StringBuffer sb = new StringBuffer();
                    sb.append("/" + wi.getWorkunit().getModule().getName());
                    sb.append("/" + wi.getWorkunit().getName() + "/");
                    sb.append(outcome);
                    sb.append(WebContext.PAGE_SUFFIX);
                    redirectPath = WebContext.createActionUrl(sb.toString());
                }
                
                WebContext.redirect(redirectPath);
            }
        } else {
            baseHandler.handleNavigation(ctx, fromAction, outcome);
        }
    }
    
}
