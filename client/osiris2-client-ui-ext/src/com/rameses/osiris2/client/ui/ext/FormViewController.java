package com.rameses.osiris2.client.ui.ext;

import com.rameses.rcp.annotations.Close;
import com.rameses.common.annotations.Controller;
import com.rameses.rcp.common.MsgBox;
import com.rameses.rcp.common.StyleRule;
import com.rameses.rcp.common.UIController;
import com.rameses.rcp.framework.ChangeLog;
import java.util.List;

public abstract class FormViewController {
    
    @Controller
    protected UIController controller;
    
    @com.rameses.rcp.annotations.ChangeLog(prefix="entity")
    protected ChangeLog changeLog;
    
    
    private Object entity;
    private String editmode = "edit";  //editmode mode values: create, read, edit
    
    public String getFormTitle() {
        if ( editmode.equals("create") ) {
            String title = getCreateTitle();
            if ( title != null && title.trim().length() > 0 ) {
                controller.setTitle(title);
                return title;
            }
        } else if ( getOpenTitle() != null ) {
            String title = getOpenTitle();
            if ( title.trim().length() > 0) {
                controller.setTitle(title);
                return title;
            }
        }
        return controller.getTitle();
    }
    
    public FormViewController() {
    }
    
    public Object getEntity() {
        return entity;
    }
    
    public void setEntity(Object entity) {
        this.entity = entity;
    }
    
    public String getEditmode() {
        return editmode;
    }
    
    public void setEditmode(String editmode) {
        this.editmode = editmode;
    }
    
    public List getFormActions() {
        return CommonUtil.FORM_ACTIONS(controller.getName());
    }
    
    public String addNew() {
        return create();
    }
    
    public String create() {
        editmode = "create";
        entity = createEntity();
        if ( CommonUtil.isViewExist( controller, "create") )
            return "create";
        
        return "form";
    }
    
    public String edit() {
        editmode = "edit";
        if ( CommonUtil.isViewExist( controller, "edit") )
            return "edit";
        
        return "form";
    }
    
    public String cancelEdit() {
        changeLog.undoAll();
        editmode = "read";
        if ( CommonUtil.isViewExist( controller, "read") )
            return "read";
        
        return "form";
    }
    
    public String open() {
        editmode = "read";
        if ( CommonUtil.isViewExist( controller, "read") )
            return "read";
        
        return "form";
    }
    
    
    public String save() {
        if(editmode.equals("create")) {
            saveNew();
            return afterSave();
        } else if(editmode.equals("edit")) {
            saveUpdate();
            return afterSave();
        } else
            throw new IllegalStateException("Edit mode must be create or edit");
    }
    
    private String afterSave() {
        editmode = "read";
        changeLog.clear();
        if ( CommonUtil.isViewExist( controller, "read") )
            return "read";
        
        return null;
    }
    
    @Close
    public boolean closeForm() {
        if( changeLog.hasChanges() ) {
            boolean t = MsgBox.confirm("Changes have been made. Discard changes?");
            if ( t ) changeLog.undoAll();
            return t;
        }
        return true;
    }
    
    public String close() {
        if( closeForm()) return "_close";
        return null;
    }
    
    
    public abstract void saveNew();
    public abstract void saveUpdate();
    public abstract Object createEntity();
    
    public final StyleRule[] getFormStyleRules() {
        return (StyleRule[])getFormStyles().toArray(new StyleRule[]{});
    }
    
    public List getFormStyles() {
        return CommonUtil.getFormStyleRules();
    }
    
    public UIController getController() {
        return controller;
    }
    
    //optional
    public String getOpenTitle() { return null; }
    public String getCreateTitle() { return null; }
    
    
}
