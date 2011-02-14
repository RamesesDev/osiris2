package com.rameses.osiris2.web;
import com.rameses.osiris2.Module;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.WorkUnitInstance;
import com.sun.facelets.impl.DefaultResourceResolver;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.faces.context.FacesContext;

public class Osiris2ResourceResolver extends DefaultResourceResolver {
    
    public Osiris2ResourceResolver() {
    }
    
    public URL resolveUrl(String path) {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        WebContext webCtx = WebContext.getInstance();
        WorkUnitInstance wi = webCtx.getCurrentWorkUnitInstance();
        
        try {
            if ( wi != null ) {
                URL resource = null;
                if ( path.equals(viewId) ) {
                    StringBuffer template = new StringBuffer(wi.getCurrentPage().getTemplate());
                    if ( template.charAt(0) == '/' )
                        template.deleteCharAt(0);
                    if ( !template.toString().endsWith(".xhtml") )
                        template.append(".xhtml");
                    
                    resource = wi.getModule().getResource(template.toString());
                } else {
                    String regex = "^/([^/]+)/(.+)$";
                    Matcher m = Pattern.compile(regex).matcher(path);
                    if ( m.matches() ) {
                        SessionContext ctx = webCtx.getSessionContext();
                        Module mod = ctx.getModule( m.group(1) );
                        resource = mod.getResource( m.group(2) );
                    }
                }
                
                if ( resource != null ) return resource;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return super.resolveUrl(path);
        
    }
}
