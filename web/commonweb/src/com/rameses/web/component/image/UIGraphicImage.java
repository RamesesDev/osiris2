
package com.rameses.web.component.image;

import com.rameses.web.common.FileObjectRenderer;
import java.io.IOException;
import java.net.URLEncoder;
import java.rmi.server.UID;
import java.util.ArrayList;
import java.util.List;
import javax.faces.component.ActionSource;
import javax.faces.component.html.HtmlGraphicImage;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.servlet.http.HttpServletRequest;

public class UIGraphicImage extends HtmlGraphicImage implements ActionSource {
    
    private List<ActionListener> actionListeners = new ArrayList<ActionListener>();
    
    public UIGraphicImage() {
    }
        
    public void encodeBegin(FacesContext context) throws IOException {
        //fire actions before getting the object value
        ActionEvent e = new ActionEvent(this);
        for (ActionListener a : getActionListeners())
            a.processAction(e);
        
        Object object = getAttributes().get("data");
        String fileId = "IMG" + new UID();
        
        HttpServletRequest req = (HttpServletRequest) context.getExternalContext().getRequest();
        req.getSession().setAttribute(fileId, object);
                        
        StringBuffer surl = new StringBuffer();
        surl.append(req.getRequestURI());
        surl.append("?" + FileObjectRenderer.FILE_ID + "=" + URLEncoder.encode(fileId));
        setValue(surl.toString());
        
        super.encodeBegin(context);
    }
    
    public MethodBinding getAction() {
        return null;
    }
    
    public void setAction(MethodBinding methodBinding) {
    }
    
    public MethodBinding getActionListener() {
        return null;
    }
    
    public void setActionListener(MethodBinding methodBinding) {
    }
    
    public boolean isImmediate() {
        return false;
    }
    
    public void setImmediate(boolean b) {
    }
    
    public void addActionListener(ActionListener actionListener) {
        actionListeners.add(actionListener);
    }
    
    public ActionListener[] getActionListeners() {
        return actionListeners.toArray(new ActionListener[]{});
    }
    
    public void removeActionListener(ActionListener actionListener) {
        actionListeners.remove(actionListener);
    }
}
