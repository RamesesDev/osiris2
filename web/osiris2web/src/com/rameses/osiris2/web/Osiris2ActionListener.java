/*
 * Osiris2ActionListener.java
 *
 * Created on May 26, 2010, 2:01 PM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.WorkUnitInstance;
import com.rameses.osiris2.web.util.ResourceUtil;
import com.rameses.osiris2.web.util.WebUtil;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;

public class Osiris2ActionListener implements ActionListener {
    
    private ActionListener listener;
    
    public Osiris2ActionListener(ActionListener listener) {
        this.listener = listener;
    }
    
    public void processAction(ActionEvent actionEvent) throws AbortProcessingException {
        try {
            doProcessAction(actionEvent);
        } catch(Exception ex) {
            Throwable e = ex;
            while( e.getCause()!=null) {
                e = e.getCause();
            }
            FacesContext ctx = FacesContext.getCurrentInstance();
            ctx.addMessage( null, new FacesMessage( e.getMessage() ) );
            throw new AbortProcessingException(e.getMessage());
        }
    }
    
    private void doProcessAction(ActionEvent actionEvent) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Application application = facesContext.getApplication();
        ActionSource actionSource = (ActionSource)actionEvent.getComponent();
        MethodBinding methodBinding = actionSource.getAction();
        String fromAction = null;
        String outcome = null;
        if(methodBinding != null) {
            fromAction = methodBinding.getExpressionString();
            try {
                Object result = methodBinding.invoke(facesContext, null);
                
                if ( result instanceof Opener ) {
                    WebContext webCtx = WebContext.getInstance();
                    WorkUnitInstance wi = webCtx.getCurrentWorkUnitInstance();
                    Opener op = (Opener) result;
                    if ( op.getName() != null ) {
                        op.setModuleName(wi.getModule().getName());
                        webCtx.getRequest().getSession().setAttribute(Opener.class.getName(), op);
                        outcome = WebUtil.OPENER_OUTCOME;
                    }
                    
                } else if (result instanceof String ) {
                    outcome = (String) result;
                    
                } else if (result instanceof WebResource ) {
                    ResourceUtil.renderResource( (WebResource) result );
                    facesContext.responseComplete();
                    return;
                    
                }
            } catch(EvaluationException e) {
                Throwable cause = e.getCause();
                if(cause != null && (cause instanceof AbortProcessingException)) {
                    throw (AbortProcessingException)cause;
                } else {
                    throw new FacesException("Error calling action method of component with id " + actionEvent.getComponent().getClientId(facesContext), e);
                }
            } catch(RuntimeException e) {
                throw new FacesException("Error calling action method of component with id " + actionEvent.getComponent().getClientId(facesContext), e);
            }
        }
        NavigationHandler navigationHandler = application.getNavigationHandler();
        navigationHandler.handleNavigation(facesContext, fromAction, outcome);
        facesContext.renderResponse();
    }
    
}
