/*
 * ListViewUtil.java
 *
 * Created on January 24, 2010, 3:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.common.Action;
import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.framework.UIController;
import com.rameses.rcp.framework.UIController.View;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author elmo
 */
public final class CommonUtil {
    
    private static Map crudButtons = new HashMap();
    private static Map crudListButtons = new HashMap();
    
    
    //<editor-fold defaultstate="collapsed" desc="  initialize buttons  ">
    static {
        //--- CRUD form buttons ---
        Action close = new Action("close", "Close", "images/common/16/cancel.gif", 'c');
        close.setImmediate(true);
        crudButtons.put("x", close);
        
        Action addNew =new Action("addNew", "New", "com/rameses/osiris2/images/document.gif", 'n');
        addNew.setVisibleWhen( "#{editmode == 'read'}" );
        crudButtons.put("a", addNew);
        
        Action save =new Action("save", "Save", "com/rameses/osiris2/images/save.gif", 's');
        save.setVisibleWhen( "#{editmode != 'read'}" );
        crudButtons.put("s", save);
        
        Action cancelEdit =new Action("cancelEdit", "Cancel Edit", "images/common/16/cancel.png", 'e');
        cancelEdit.setVisibleWhen( "#{editmode == 'edit'}" );
        cancelEdit.setImmediate(true);
        crudButtons.put("c", cancelEdit);
        
        Action edit =new Action("edit", "Edit", "com/rameses/osiris2/images/edit.gif", 'e');
        edit.setVisibleWhen( "#{editmode == 'read'}" );
        crudButtons.put("e", edit);
        
        Action delete =new Action("delete", "Delete", "images/common/16/delete.png", 'd');
        delete.setVisibleWhen( "#{editmode == 'read'}" );
        crudButtons.put("d", delete);
        
        //--- CRUDList buttons
        crudListButtons.put("x", new Action("_close", "Close", "images/common/16/cancel.gif", 'c'));
        crudListButtons.put("n", new Action("listHandler.create", "New", "com/rameses/osiris2/images/document.gif", 'n'));
        crudListButtons.put("o", new Action("listHandler.open", "Open", "com/rameses/osiris2/images/open.gif", 'o'));
    }
    //</editor-fold>
    
    
    public static List getPageActions() {
        List list = new ArrayList();
        list.add( new Action("listHandler.moveFirstPage", null, "com/rameses/osiris2/images/arrowup.gif"));
        list.add( new Action("listHandler.moveBackPage", null, "com/rameses/osiris2/images/arrowleft.gif"));
        list.add( new Action("listHandler.moveNextPage", null, "com/rameses/osiris2/images/arrowright.gif"));
        return list;
    }
    
    public static List getCloseButton() {
        List list = new ArrayList();
        list.add( new Action("_close", "Close", "com/rameses/osiris2/images/close.png", 'c'));
        return list;
    }
    
    public static List getOkCancelButtons() {
        List list = new ArrayList();
        list.add( new Action("cancel", "Cancel", null));
        list.add( new Action("select", "OK", null));
        return list;
    }
    
    //pass the contextName used for permissions
    public static List getListActions(String contextName) {
        return getCRUDListButtons(null);
    }
    
    public static List getFormActions(String contextName) {
        return getCRUDButtons("xasec");
    }
    
    public static boolean isViewExist(UIController controller, String name ) {
        for(View uv: controller.getViews()) {
            if(uv.getName()!=null && uv.getName().equals(name)) {
                return  true;
            }
        }
        return  false;
    }
    
    public static List getFormStyleRules() {
        List list = new ArrayList();
        list.add( new StyleRule("entity.*", "#{editmode == 'read'}").add("readonly", true));
        list.add(new StyleRule("entity.*", "#{editmode != 'read'}").add("readonly", false));
        return list;
    }
    
    /*********************************************************************************
     * parameter: String buttons<br>
     * usage: getCRUDButtons("xasce"), returns the buttons close, addNew, save, cancelEdit, edit<br>
     * <b>values</b>
     * <ul>
     * <li>x - close</li><li>a - addNew</li><li>s - save</li><li>c - cancelEdit</li>
     * <li>e - edit</li><li>d - delete</li>
     * </ul>
     *********************************************************************************/
    public static List getCRUDButtons(String buttons) {
        List btns = new ArrayList();
        if (buttons == null || buttons.trim().length() == 0) {
            buttons = "xasecd";
        }
        for ( char c : buttons.toLowerCase().toCharArray() ) {
            if ( crudButtons.containsKey(c+"") )
                btns.add( crudButtons.get(c+"") );
        }
        return btns;
    }
    
    /*********************************************************************************
     * parameter: String buttons<br>
     * usage: getCRUDListButtons("xno"), returns the buttons close, new, and open<br>
     * <b>values</b>
     * <ul>
     * <li>x - close</li><li>n - new</li><li>o - open</li>
     * </ul>
     *********************************************************************************/
    public static List getCRUDListButtons(String buttons) {
        List btns = new ArrayList();
        if (buttons == null || buttons.trim().length() == 0) {
            buttons = "xno";
        }
        for ( char c : buttons.toLowerCase().toCharArray() ) {
            if ( crudListButtons.containsKey(c+"") )
                btns.add( crudListButtons.get(c+"") );
        }
        return btns;
    }
    
}
