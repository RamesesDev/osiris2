
/*
 * InvokerUtil.java
 *
 * Created on October 27, 2009, 4:14 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.web;

import com.rameses.osiris2.ExpressionProvider;
import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 *
 * @author elmo
 */
public final class InvokerUtil {
    
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
        SessionContext app = WebContext.getInstance().getSessionContext();
        List list = app.getInvokers(type);
        if( obj == null) {
            return list;
        } else {
            List data = new ArrayList();
            ExpressionProvider er = WebContext.getInstance().getExpressionProvider();
            
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
                            Map p = new HashMap();
                            p.put("bean", params);
                            b = ((Boolean)er.eval(cond, p)).booleanValue();
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
     * returns a list of invokers from a folder path.
     */
    public static List lookupFolder( String name ) {
        if(name == null ) return null;
        if( !name.startsWith("/")) name = "/" + name;
        List invokers = new ArrayList();
        SessionContext app = WebContext.getInstance().getSessionContext();
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
    
    /**
     * returns a the first opener based on the invoker type
     */
    public static Opener lookupOpener( String invType, Map params ) {
        List<Invoker> list = lookup( invType );
        if ( list.size() == 0 ) {
            throw new RuntimeException("No invokers found for type [" + invType + "]");
        }
        return createOpener(list.get(0), params);
    }
    
    public static List lookupOpeners( String invType) {
        return lookupOpeners( invType, null );
    }
    
    public static List lookupOpeners( String invType, Map params ) {
        List<Invoker> list = lookup( invType );
        if ( list.size() == 0 ) {
            throw new RuntimeException("No invokers found for type [" + invType + "]");
        }
        List openers = new ArrayList();
        for(Invoker inv: list) {
            Opener opener = createOpener(inv, params);
            openers.add(opener);
        }
        return openers;
    }

    public static Opener createOpener(Invoker inv) {
        return createOpener(inv, null);
    }
    
    public static Opener createOpener(Invoker inv, Map params) {
        return createOpener(inv, params, null);
    }
    
    public static Opener createOpener(Invoker inv, Map params, String caption ) {
        String target = (String)inv.getProperties().get("target");
//        Opener opener = new Opener(inv.getWorkunitid());
//        if(caption==null) caption = inv.getCaption();
//        if(caption==null) caption = inv.getWorkunitid();
//        opener.setId(inv.getWorkunitid() + "_" + caption );
//        opener.setCaption(caption);
//        opener.setAction(inv.getAction());
//        
//        if ( target !=null ) target = target.replaceAll("^([^_])", "_$1");
//        opener.setTarget( target );
//        if(params!=null) opener.setParams( params );
//        
//        if( inv.getProperties().size() > 0 )
//            opener.getProperties().putAll( inv.getProperties() );
        
//        return opener;
        
        return new Opener();
    }
    
}