package com.rameses.osiris2.client;

import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.rameses.osiris2.SessionContext;
import com.rameses.rcp.common.Action;
import com.rameses.rcp.framework.ActionProvider;
import java.util.ArrayList;
import java.util.List;

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
    
    public static class InvokerAction extends Action {
        private Invoker invoker;
        private Object context;
        
        public InvokerAction(Invoker invoker,Object context) {
            super(invoker.getName()==null? invoker.getCaption()+"":invoker.getName());
            super.setCaption(invoker.getCaption());
            super.setName( invoker.getAction() );
            if(invoker.getIndex()!=null) {
                super.setIndex(invoker.getIndex());
            }
            
            super.setIcon((String)invoker.getProperties().get("icon"));
            this.invoker = invoker;
            this.context = context;
        }
        
        public Object doAction(Object context) {
            InvokerUtil.invoke(invoker, null);
            return null;
        }
        
    }
    
}
