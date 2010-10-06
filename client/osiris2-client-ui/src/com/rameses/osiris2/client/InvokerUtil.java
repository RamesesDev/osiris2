/*
 * InvokerUtil.java
 *
 * Created on October 27, 2009, 4:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client;

import com.rameses.osiris2.SessionContext;
import com.rameses.osiris2.Folder;
import com.rameses.rcp.util.ControlSupport;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.osiris2.Invoker;
import com.rameses.platform.interfaces.Platform;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.common.ExpressionResolver;
import com.rameses.rcp.common.Opener;
import com.rameses.util.ExceptionManager;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class InvokerUtil {
    
    public static void invoke(Invoker invoker) {
        invoke(invoker,null);
    }
    
    public static void showWindow(Invoker invoker, String target, Map winParams) {
        if ( !ValueUtil.isEmpty(target) ) {
            invoker.setType(target);
        }
        if ( winParams != null ) {
            invoker.getProperties().putAll(winParams);
        }
        invoke(invoker, null);
    }
    
    public static void invoke(Invoker invoker, Map params) {
        invoke(invoker, params, null);
    }
    
    public static void invoke(Invoker invoker, Map params, Object caller) {
        try {
            ClientContext ctx = ClientContext.getCurrentContext();
            Platform platform = ctx.getPlatform();
            
            String wuId = invoker.getWorkunitid();
            
            //check if window id already exists
            String windowId = wuId + invoker.getCaption();
            if ( platform.isWindowExists( windowId )) {
                platform.activateWindow( windowId );
                return;
            }
            
            ControllerProvider cp = ctx.getControllerProvider();
            UIController u = cp.getController( wuId );
            
            if ( caller != null ) {
                Object callee = u.getCodeBean();
                ControlSupport.injectCaller(callee, callee.getClass(), caller);
            }
            
            String action = invoker.getAction();
            u.setId( wuId );
            u.setName( wuId );
            u.setTitle( invoker.getCaption());
            
            String outcome = (String) u.init(params, action);
            String target = (String)invoker.getProperties().get("target");
            if( target == null ) target = "_window";
            
            if((target.endsWith("process")||target.endsWith("action"))) {
                //do nothing
            } else {
                UIControllerContext uic = new UIControllerContext( u );
                uic.setId(windowId);
                if ( !ValueUtil.isEmpty(outcome) ) {
                    uic.setCurrentView(outcome);
                }
                UIControllerPanel panel = new UIControllerPanel( uic );
                Map winParams = new HashMap();
                if ( invoker.getProperties() != null ) {
                    winParams.putAll( invoker.getProperties() );
                }
                winParams.put("id", uic.getId());
                winParams.put("title", uic.getTitle());
                
                if ( "_popup".equals(target) || "popup".equals(target) ) {
                    platform.showPopup(null, panel, winParams);
                } else {
                    platform.showWindow(null, panel, winParams);
                }
            }
        } catch(Exception ex) {
            Exception e = ExceptionManager.getOriginal(ex);
            
            if ( !ExceptionManager.getInstance().handleError(e) ) {
                ClientContext.getCurrentContext().getPlatform().showError(null, ex);
            }
        }
    }
    
    public static Object invokeAction(InvokerAction action) {
        try {
            Invoker inv = action.getInvoker();
            InvokerParameter invParam = action.getInvokerParam();
            
            String target = (String)inv.getProperties().get("target");
            if( target == null ) target = "_window";
            
            if((target.endsWith("process")||target.endsWith("action"))) {
                if ( invParam != null )
                    invoke(inv, invParam.getParams());
                else
                    invoke(inv, null);
                
                return null;
                
            } else {
                Opener opener = new Opener(inv.getWorkunitid());
                opener.setId(inv.getWorkunitid() + "_" + inv.getCaption());
                opener.setCaption(inv.getCaption());
                opener.setAction(inv.getAction());
                if ( invParam != null ) {
                    opener.setParams(invParam.getParams());
                }
                
                if ( target.endsWith("popup") ) {
                    opener.setTarget("_popup");
                } else if ( target.endsWith("window") ) {
                    opener.setTarget("_window");
                }
                
                return opener;
            }
        } catch(Exception ex) {
            Exception e = ExceptionManager.getOriginal(ex);
            
            if ( !ExceptionManager.getInstance().handleError(e) ) {
                ClientContext.getCurrentContext().getPlatform().showError(null, ex);
            }
            return null;
        }
    }
    
    public static List lookupActions(String type) {
        return lookupActions(type, null);
    }
    
    public static List lookupActions(String type, InvokerParameter param) {
        List actions = new ArrayList();
        List invList = lookup(type);
        for(Object o: invList) {
            Invoker inv = (Invoker)  o;
            InvokerAction ia = new InvokerAction(inv, param);
            ia.setCaption(inv.getCaption());
            ia.getProperties().putAll(inv.getProperties());
            
            actions.add(ia);
        }
        return actions;
    }
    
    public static List lookup(String type) {
        return lookup(type,null);
    }
    
    
    /*
     * The object passed will be evaluated by the expression.
     * params = refer to Object parameter passed.
     * context = the invocation context.
     * a sample implementation as follows: #{param.name == context.module.name}
     */
    public static List lookup(String type, Object obj) {
        SessionContext app = OsirisContext.getSession();
        List list = app.getInvokers(type);
        if( obj == null) {
            return list;
        } else {
            List data = new ArrayList();
            ExpressionResolver er = ClientContext.getCurrentContext().getExpressionResolver();
            
            for(Object o: list) {
                Invoker inv = (Invoker)o;
                if( inv.getProperties().get("eval")!=null) {
                    Map params = new HashMap();
                    params.put("param", obj);
                    params.put( "context", inv );
                    
                    String cond = (String)inv.getProperties().get("eval");
                    if( cond.trim().length()>0) {
                        boolean b = false;
                        try {
                            b = ((Boolean)er.evaluate(params, cond)).booleanValue();
                        } catch(Exception ign){;}
                        if(b) {
                            data.add(inv);
                        }
                    }
                } else {
                    data.add(inv);
                }
            }
            return data;
        }
    }
    
    /**
     * This method returns a list of invokers from a folder path.
     */
    public static List lookupFolder( String name ) {
        if(name == null ) return null;
        if( !name.startsWith("/")) name = "/" + name;
        List invokers = new ArrayList();
        SessionContext app = OsirisContext.getSession();
        List items = (List) app.getFolders(name);
        if(items!=null) {
            for (Object o : items) {
                Folder f = (Folder) o;
                if (f.getInvoker() != null) {
                    Invoker v = f.getInvoker();
                    invokers.add(v);
                }
            }
        }
        return invokers;
    }
    
}