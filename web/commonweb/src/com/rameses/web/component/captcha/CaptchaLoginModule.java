package com.rameses.web.component.captcha;

import java.security.Principal;
import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import javax.security.jacc.PolicyContext;
import javax.security.jacc.PolicyContextException;
import javax.servlet.http.HttpServletRequest;

public class CaptchaLoginModule implements LoginModule {
    
    private Subject subject;
    private CallbackHandler callbackHandler;
    private Map<String, ?> sharedState;
    private Map<String, ?> options;
    
    private boolean commitSucceeded = false;
    private boolean loginSucceeded = false;
    
    private String username;
    private Principal[] principals;
    
    public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;
    }
    
    public boolean login() throws LoginException {
        String captcha = null;
        try {
            HttpServletRequest hreq = (HttpServletRequest) PolicyContext.getContext(HttpServletRequest.class.getName());
            captcha = hreq.getParameter("captcha");
            if(captcha!=null ) {
                String sessionID = hreq.getSession().getId();
                boolean test = CaptchaServiceSingleton.getInstance().validateResponseForID(sessionID, captcha).booleanValue();
                if(!test) throw new LoginException("The value entered did not match on the given image.");
            }
        } 
        catch (PolicyContextException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public boolean commit() throws LoginException {
       return true;
    }
    
    public boolean abort() throws LoginException {
        if (commitSucceeded == false) {
            cleanState();
            principals = null;
        } else {
            // overall authentication succeeded and commit succeeded,
            // but someone else's commit failed
            logout();
        }
        return true;
    }
    
    
    public boolean logout() throws LoginException {
        if (subject.isReadOnly()) {
            cleanState();
            throw new LoginException("Subject is read-only");
        }
        for(Principal p : principals) {
            subject.getPrincipals().remove(p);
        }
        // clean out state
        cleanState();
        commitSucceeded = false;
        principals = null;
        return true;
    }
    
    private void cleanState() {
        //do nothing
    }
    
}
