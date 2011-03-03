/*
 * Loader.java
 *
 * Created on May 24, 2010, 9:48 AM
 * @author jaycverg
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.ExpressionProvider;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.WorkUnit;
import com.rameses.osiris2.WorkUnitInstance;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;


public class Loader implements Serializable {
    
    public static final String SECURED_REQ_URL_KEY = "loader.secured.request.url";
    public static final String LOADER_WI_KEY = "_loader.wi";
    
    private List invokers;
    private Map properties = new HashMap();
    private boolean done = false;
    
    public Loader() {
        SessionContext ctx = WebContext.getInstance().getSessionContext();
        invokers = ctx.getInvokers("loader");
        
        Map loadersIndex = new HashMap();
        for(Object i : invokers) {
            Invoker inv = (Invoker) i;
            loadersIndex.put(inv.getWorkunitid(), i);
        }
        ctx.getProperties().put("LOADER_INDEX", loadersIndex);
    }
    
    public WorkUnitInstance getWorkUnitInstance() {
        if ( invokers.isEmpty() ) return null;
        
        WebContext webCtx = WebContext.getInstance();
        HttpSession sess = webCtx.getRequest().getSession();
        SessionContext sessCtx = webCtx.getSessionContext();
        WorkUnitInstance wi = (WorkUnitInstance) sess.getAttribute(LOADER_WI_KEY);
        
        //check loader if applied for this context
        while( wi == null && !invokers.isEmpty() ) {
            boolean allowed = true;
            Invoker inv = (Invoker) invokers.get(0);
            allowed = sessCtx.checkPermission(inv.getWorkunitid(), inv.getPermission());
            if ( allowed ) {
                WorkUnit wu = webCtx.getSessionContext().getWorkUnit(inv.getWorkunitid());
                wi = wu.newInstance(inv.getWorkunitid(), inv.getCaption());
                String condExpr = (String) inv.getProperties().get("cond");
                if ( condExpr != null ) {
                    ExpressionProvider exp = webCtx.getExpressionProvider();
                    Map m = new HashMap();
                    m.put("bean", wi.getController());
                    try {
                        Boolean result = (Boolean) exp.eval(condExpr, m);
                        allowed = result.booleanValue();
                    } catch(Exception e) {
                        String msg = "ERROR IN LOADER: " + wi.getId();
                        throw new IllegalStateException(msg, e);
                    }
                }
            }
            if ( !allowed ) {
                invokers.remove(0);
                wi = null;
                continue;
            }
            
            String target = (String) inv.getProperties().get("target");
            if ( target != null && target.matches(".*process")) {
                String action = inv.getAction();
                if ( action != null ) {
                    wi.invoke(action);
                }
                invokers.remove(0);
                wi = null;
                continue;
            }
            
            sess.setAttribute(LOADER_WI_KEY, wi);
        }
        
        return wi;
    }
    
    public void removeCurrentInvoker() {
        if ( !invokers.isEmpty() ) {
            invokers.remove(0);
        }
        
        WebContext.getInstance().getSession().removeAttribute(LOADER_WI_KEY);
    }
    
    public boolean hasInvokers() {
        return !invokers.isEmpty();
    }
    
    public static boolean isLoaderWorkUnit(WorkUnitInstance wi) {
        SessionContext ctx = WebContext.getInstance().getSessionContext();
        Map index = (Map) ctx.getProperties().get("LOADER_INDEX");
        return index != null && index.containsKey(wi.getWorkunit().getId());
    }
    
    public Map getProperties() {
        return properties;
    }
    
    public boolean isDone() {
        return done;
    }
    
    public void setDone(boolean done) {
        this.done = done;
    }
}
