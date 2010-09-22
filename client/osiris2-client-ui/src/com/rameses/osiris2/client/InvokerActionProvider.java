package com.rameses.osiris2.client;

import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import com.rameses.rcp.framework.UIController;
import com.rameses.util.ValueUtil;
import java.util.ArrayList;
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
                String wuId = controller.getName();
                if ( !wuId.equals(inv.getWorkunitid()) ) {
                    continue;
                }
            }
            actions.add(createAction(inv, context));
        }
        return actions;
    }
    
    private Action createAction(Invoker inv,Object context) {
        Action a = new Action( inv.getName()==null? inv.getCaption()+"":inv.getName() );
        
        a.setName( inv.getAction() );
        a.setCaption(inv.getCaption());
        if(inv.getIndex()!=null) {
            a.setIndex(inv.getIndex());
        }
        
        Map invProps = inv.getProperties();
        a.setIcon((String)invProps.get("icon"));
        a.setImmediate( "true".equals(invProps.get("immediate")+"") );
        a.setUpdate( "true".equals(invProps.get("update")+"") );
        a.setVisibleWhen( (String) invProps.get("visibleWhen") );
        
        String mnemonic = (String) invProps.get("mnemonic");
        if ( !ValueUtil.isEmpty(mnemonic) ) {
            a.setMnemonic(mnemonic.charAt(0));
        }
        
        Object tooltip = invProps.get("tooltip");
        if ( !ValueUtil.isEmpty(tooltip) ) {
            a.setTooltip(tooltip+"");
        }
        
        a.getProperties().putAll( invProps );
        
        return a;
    }
    
}
