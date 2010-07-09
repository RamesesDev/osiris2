
package com.rameses.web.component.captcha;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpSession;

public class CaptchaValidator implements Validator{
    
    private final static String captchaId = "CaptchaId";
    
    public CaptchaValidator() {
    }
    
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        FacesMessage message = new FacesMessage();
        if(value == null){
          return;
        }
        
        String sessionID = ((HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true)).getId();
        boolean correct = CaptchaServiceSingleton.getInstance().validateResponseForID(sessionID, value).booleanValue();        
        if(!correct){            
            message.setSummary("The value entered did not match on the given image.");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}

