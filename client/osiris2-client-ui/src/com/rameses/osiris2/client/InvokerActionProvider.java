package com.rameses.osiris2.client;

import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import com.rameses.rcp.framework.UIController;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvokerActionProvider implements ActionProvider {
    
    public InvokerActionProvider() {}
    
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
                            Action a = createAction(f.getInvoker(), context);
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
                        Action a = createAction(f.getInvoker(), context);
                        if(f.getParent()!=null ) a.setCategory(f.getParent().getCaption());
                        actions.add(a);
                    }
                }
            }
        }
        return actions;
    }
    
    public List<Action> getActionsByType(String type, UIController controller) {
        Object context = null;
        if ( controller != null ) {
            context = controller.getCodeBean();
        }
        
        List<Invoker> invList = InvokerUtil.lookup(type, context);
        List<Action> actions = new ArrayList();

        for(Invoker inv: invList) {
            if ( controller instanceof WorkUnitUIController ) {
                WorkUnitUIController wuc = (WorkUnitUIController) controller;
                String wucid = wuc.getWorkunit().getId();
                
                if ( wucid.equals(inv.getWorkunitid()) ) {
                    actions.add(createAction(inv, context));
                }
            }
        }
        return actions;
    }
    
    private Action createAction(Invoker inv,Object context) {
        Action a = new Action( inv.getName()==null? inv.getCaption()+"":inv.getName() );
        
        Map invProps = new HashMap(inv.getProperties());
        a.setName( (String) invProps.remove("action") );
        a.setCaption( (String) invProps.remove("caption") );
        if(inv.getIndex()!=null) {
            a.setIndex(inv.getIndex());
        }
        
        a.setIcon((String)invProps.remove("icon"));
        a.setImmediate( "true".equals(invProps.remove("immediate")+"") );
        a.setUpdate( "true".equals(invProps.remove("update")+"") );
        a.setVisibleWhen( (String) invProps.remove("visibleWhen") );
        
        String mnemonic = (String) invProps.remove("mnemonic");
        if ( !ValueUtil.isEmpty(mnemonic) ) {
            a.setMnemonic(mnemonic.charAt(0));
        }
        
        Object tooltip = invProps.remove("tooltip");
        if ( !ValueUtil.isEmpty(tooltip) ) {
            a.setTooltip(tooltip+"");
        }
        
        if ( !invProps.isEmpty() ) a.getProperties().putAll( invProps );
        
        return a;
    }
    
}
