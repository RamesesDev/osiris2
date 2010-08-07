package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.util.ValueUtil;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author elmo
 */
public class UILoaderStack extends Stack {
    
    private List loaders;
    private Object peek;
    
    public void setLoaders( List loaders ) {
        this.loaders = loaders;
    }
    
    public boolean empty() {
        if(loaders!=null && loaders.size()>0)
            return false;
        else
            return super.empty();
    }
    
    public boolean isEmpty() {
        return empty();
    }
    
    public int size() {
        return loaders.size();
    }
    
    public Object push(Object item) { return item; }
    
    public Object pop() {
        Object item = this.peek();
        if ( item != null && loaders.size() > 1 ) {
            loaders.remove(0);
            peek = null;
        }
        return item;
    }
    
    public Object peek() {
        if( !loaders.isEmpty() ) {
            while(loaders.size()>0 && peek == null) {
                Invoker i = (Invoker) loaders.get(0);
                String action = i.getAction();
                String target = (String)i.getProperties().get("target");
                
                ClientContext ctx = ClientContext.getCurrentContext();
                
                //check permission
                OsirisSessionContext sessCtx = (OsirisSessionContext) OsirisContext.getSession();
                if ( !sessCtx.checkPermission(i.getWorkunitid(), i.getPermission()) ) {
                    loaders.remove(0);
                    continue;
                }
                
                ControllerProvider cp = ctx.getControllerProvider();
                UIController c = cp.getController( i.getWorkunitid() );
                
                if( target!=null && target.matches(".*process")) {
                    try {
                        loaders.remove(0);
                        c.init( null, action );
                        continue;
                    } catch(Exception e) {
                        throw new IllegalStateException("ERROR IN LOADER " + i.getWorkunitid() + ": " + e.getMessage(), e);
                    }
                } else {
                    UIControllerContext uic = new UIControllerContext( c );
                    Object outcome = c.init( null, action );
                    if ( !ValueUtil.isEmpty(outcome) && outcome instanceof String ) {
                        uic.setCurrentView(outcome+"");
                    }
                    peek = uic;
                    
                    //if size is 1, this is the last loader usually the home page
                    //reload the menus/folders
                    if ( loaders.size() <= 1 ) {
                        sessCtx.reload();
                    } 
                }
            }
        }
        return peek;
    }
    
}
