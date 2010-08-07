package com.rameses.osiris2.client;

import com.rameses.osiris2.Invoker;
import com.rameses.platform.interfaces.MainWindow;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.ClientContext;
import com.rameses.rcp.framework.ControllerProvider;
import com.rameses.rcp.framework.UIControllerContext;
import com.rameses.rcp.framework.UIControllerPanel;
import com.rameses.util.ValueUtil;


public final class MainWindowUtil {
    
    public static void loadComponents(MainWindow w) {
        
        w.setComponent( MenuUtil.getMenuBar("menu"), MainWindow.MENUBAR);
        
        ClientContext ctx = ClientContext.getCurrentContext();
        ControllerProvider cp = ctx.getControllerProvider();
        for(Object o: InvokerUtil.lookup("mainform_.*",null)) {
            Invoker i = (Invoker)o;
            UIController c = cp.getController( i.getWorkunitid() );
            
            String action = i.getAction();
            String page = null;
            if(action!=null) {
                page = c.init(null, action)+"";
            }
            UIControllerContext uic = new UIControllerContext( c );
            if ( !ValueUtil.isEmpty(page) ) {
                uic.setCurrentView(page);
            }
            UIControllerPanel up = new UIControllerPanel( uic );
            String loc = i.getType().substring(i.getType().indexOf("_")+1);
            //w.add(up, loc);
            w.setComponent(up, MainWindow.CONTENT);
        }
    }
    
    
    
}
