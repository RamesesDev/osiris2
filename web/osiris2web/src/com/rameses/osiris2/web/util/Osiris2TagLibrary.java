
package com.rameses.osiris2.web.util;

import com.rameses.osiris2.Folder;
import com.rameses.osiris2.Invoker;
import com.sun.facelets.tag.jsf.html.AbstractHtmlLibrary;

public class Osiris2TagLibrary extends AbstractHtmlLibrary {
    
    public static final String NameSpace = "http://com.rameses.osiris2.web/invoker";
    public static final Osiris2TagLibrary Instance = new Osiris2TagLibrary();
    
    public Osiris2TagLibrary() {
        super(NameSpace);
        
        //hasIcon
        try {
            super.addFunction( "resourcePath", InvokerFunctions.class.getDeclaredMethod("resourcePath", new Class[] { String.class } ));
        } catch(Exception ign){System.out.println("error InvokerFunctions.iconPath");}
        
        //invokerPath
        try {
            super.addFunction( "invokerPath", InvokerFunctions.class.getDeclaredMethod("invokerPath", new Class[] { Invoker.class } ));
        } catch(Exception ign){System.out.println("error InvokerFunctions.invokerPath");}
        
        //hasIcon
        try {
            super.addFunction( "hasIcon", InvokerFunctions.class.getDeclaredMethod("hasIcon", new Class[] { Folder.class } ));
        } catch(Exception ign){System.out.println("error InvokerFunctions.hasIcon");}
        
        //hasInvokers
        try {
            super.addFunction( "hasInvokers", InvokerFunctions.class.getDeclaredMethod("hasInvokers", new Class[] { Folder.class } ));
        } catch(Exception ign){System.out.println("error InvokerFunctions.hasInvokers");}
        
        //checkPermission(String)
        try {
            super.addFunction( "checkPermission", InvokerFunctions.class.getDeclaredMethod("checkPermission", new Class[] { String.class } ));
        } catch(Exception ign){System.out.println("error InvokerFunctions.checkPermission(String)");}
        
        //checkPermission(Invoker)
        try {
            super.addFunction( "checkPermission", InvokerFunctions.class.getDeclaredMethod("checkPermission", new Class[] { Invoker.class } ));
        } catch(Exception ign){System.out.println("error InvokerFunctions.checkPermission(Invoker)");}
        
    }
    
}
