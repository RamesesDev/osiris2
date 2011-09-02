/*
 * ScriptService.java
 *
 * Created on October 16, 2010, 8:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.common.AsyncResponse;
import com.rameses.scripting.ScriptExecutor;
import com.rameses.scripting.ScriptManager;
import com.rameses.scripting.ScriptServiceLocal;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.server.UID;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

/**
 *
 * @author ms
 *
 */
@Stateless
@Local(ScriptServiceLocal.class)
public class ScriptService implements ScriptServiceLocal {
    
    @Resource
    private SessionContext context;
    
    public byte[] getScriptInfo(String name) {
        return ScriptManager.getInstance().getProxyIntfBytes(name);
    }
    
    public Object invoke(String name, String method, Object[] params, Map env) {
        try {
            CustomResourceInjector si = new CustomResourceInjector(name,context,env,this);
            ScriptExecutor se = ScriptManager.getInstance().createExecutor( name, method,params, si );
            return se.execute(this,params,env);
        } catch(Exception e) {
            throw new EJBException(e);
        }
    }
    
    public Object invokeAsync(String name, String method, Object[] params, Map env, Map asyncInfo ) {
        System.out.println("invoking async now...");
        String requestId = "ASYNC:"+new UID();
        ExecutorService svc = Executors.newSingleThreadExecutor();
        svc.execute( new AsyncExecutor(requestId, name,method,params, env, asyncInfo) );
        AsyncResponse response = new AsyncResponse();
        response.put("id", requestId);
        response.put("classname", response.getClass().getName() );
        return response;
    }
    

    public void pushResponse(String requestId, Object data) {
        ObjectOutputStream oos = null;
        ByteArrayOutputStream bos = null;
        try {
            int idx = 0;
            if((data instanceof String) && ((String)data).equalsIgnoreCase("EOF") ) {
                idx = 100;
            }
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject( data );
            SqlContext ctx  = SqlManager.getInstance().createContext(AppContext.getSystemDs());
            SqlExecutor qe = ctx.createNamedExecutor("scripting:async-push");
            qe.setParameter(1,"RESP:"+new UID());
            qe.setParameter(2, requestId );
            qe.setParameter(3, bos.toByteArray());
            qe.setParameter(4, idx);
            qe.execute();
        }
        catch(Exception e) {
            throw new EJBException(e);
        }
        finally {
            try {oos.close();} catch(Exception ign){;}
            try {bos.close();} catch(Exception ign){;}
        }
    }

    public Object getPollData(String requestId) {
        ObjectInputStream ois = null;
        try {
            SqlContext ctx  = SqlManager.getInstance().createContext(AppContext.getSystemDs());
            Map map = (Map)ctx.createNamedQuery("scripting:async-poll").setParameter(1,requestId).getSingleResult();
            if(map==null) return null;
            String objid = (String)map.get( "objid" );
            byte[] bytes = (byte[])map.get("data"); 
            ois = new ObjectInputStream( new ByteArrayInputStream(bytes) );
            Object returnObject =  ois.readObject();
            ctx.createNamedExecutor("scripting:async-remove").setParameter(1, objid).execute();
            return returnObject;
        }
        catch(Exception e) {
            throw new EJBException(e);
        }
        finally {
            try {ois.close();} catch(Exception ign){;}
        }
    }

    
    
}
