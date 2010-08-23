package com.rameses.osiris2.client;

import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InvokerActionProvider implements ActionProvider {
    
    public InvokerActionProvider() {
        
    }
    
    public boolean hasItems(String category, Object context) {
        List list = OsirisContext.getSession().getInvokers( category );
        return list.size() > 0 ;
    }
    
    
    //this should support wildcards. example /icon/* will display second level items instead
    public List<Action> getActions(String name, Object context) {
        if( !name.startsWith("/")) name = "/" + name;
        SessionContext app = OsirisContext.getSession();
        
        List<Action> actions = new ArrayList<Action>();
        
        if( name.endsWith("/*") ) {
            name = name.substring(0, name.indexOf("/*"));
            
            List items = (List) app.getFolders(name);
            if(items!=null) {
                for(Object o: items) {
                    Folder pf = (Folder)o;
                    for(Object ff: app.getFolders(pf)) {
                        Folder f = (Folder)ff;
                        if(f.getInvoker()!=null) {
                            InvokerAction a = new InvokerAction(f.getInvoker(), context);
                            if(f.getParent()!=null ) a.setCategory(f.getParent().getCaption());
                            actions.add(a);
                        }
                    }
                }
            }
            
        } else {
            List items = (List) app.getFolders(name);
            if(items!=null) {
                for(Object o: items) {
                    Folder f = (Folder)o;
                    if(f.getInvoker()!=null) {
                        InvokerAction a = new InvokerAction(f.getInvoker(), context);
                        if(f.getParent()!=null ) a.setCategory(f.getParent().getCaption());
                        actions.add(a);
                    }
                }
            }
        }
        return actions;
    }
    
    public List<Action> getActionsByType(String type, Object context) {
        List<Invoker> invList = InvokerUtil.lookup(type, context);
        List<Action> actions = new ArrayList();
        for(Invoker inv: invList) {
            actions.add(new InvokerAction(inv, context));
        }
        return actions;
    }
    
    public static class InvokerAction extends Action {
        
        public InvokerAction(Invoker inv,Object context) {
            super(inv.getName()==null? inv.getCaption()+"":inv.getName());
            
            setName( inv.getAction() );
            setCaption(inv.getCaption());
            if(inv.getIndex()!=null) {
                setIndex(inv.getIndex());
            }
            
            Map invProps = inv.getProperties();
            setIcon((String)invProps.get("icon"));
            setImmediate( "true".equals(invProps.get("immediate")+"") );
            setUpdate( "true".equals(invProps.get("update")+"") );
            setVisibleWhen( (String) invProps.get("visibleWhen") );
            
            String mnemonic = (String) invProps.get("mnemonic");
            if ( !ValueUtil.isEmpty(mnemonic) ) {
                setMnemonic(mnemonic.charAt(0));
            }
            
            String tooltip = invProps.get("tooltip")+"";
            if ( !ValueUtil.isEmpty(tooltip) ) {
                setTooltip(tooltip);
            }
        }
    }
    
}
