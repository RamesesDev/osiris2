
package com.rameses.web.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;


public class ConfirmPasswordValidator implements Validator {
    
    public ConfirmPasswordValidator() {
    }
    
    public void validate(FacesContext ctx, UIComponent comp, Object value) throws ValidatorException {
        String passwordId = (String) comp.getAttributes().get("passwordId");
        if (passwordId == null) return;
        
        UIInput input = (UIInput) ctx.getViewRoot().findComponent(passwordId);
        String password = (String) input.getValue();
        String confirmPass = (String) value;
        
        if (password.equals(confirmPass)) return;
        
        FacesMessage message = new FacesMessage();
        message.setDetail("wrong confirm password entered.");
        message.setSummary("wrong confirm password entered.");
        message.setSeverity(FacesMessage.SEVERITY_ERROR);
        throw new ValidatorException(message);
    }
    
}
