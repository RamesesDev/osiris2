package com.rameses.ui.templates;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

public abstract class DataForm {

    @ChangeLog(prefix=['entity'])
    def changeLog;

    @Binding
    def binding;

    @Caller
    def caller;

    def closeMessage = 'Changes have been made. Discard changes?';
    def promptMessage = 'You are about to save this record. Continue?';

    def entity;
    def saveHandler;
    def editmode = 'read';
    def formTitle = "No title specified";

    //switches in conjunction with the DataForm.xml. set to true if you want this displayed.
    boolean _promptBeforeSave = true;
    boolean _saveAndContinue = false;
    boolean _saveAndClose = true;
    boolean _save = true;

    public def createEntity( o ) { 
        throw new Exception('Please override createEntity( o ).'); 
    }

    public def openEntity( o ) { 
        throw new Exception('Please override openEntity( o ).'); 
    }

    public def saveNewEntity( o ) { 
        throw new Exception('Please override saveNewEntity( o ).'); 
    }

    public def updateEntity( o ) { 
        throw new Exception('Please override updateEntity( o ).'); 
    }

    public def create() {
        editmode = 'create';
        return createEntity( entity );
    }

    public def open() {
        return openEntity( entity );
    }

    public def edit() {
        editmode = 'edit';
        return null;
    }

    public def saveNew() {
        if ( _promptBeforeSave && !MsgBox.confirm(promptMessage) ) return;
        def outcome = saveNewEntity( entity );
        if(saveHandler) saveHandler(entity);
        editmode = 'read';
        return outcome;
    }

    public def saveAndClose() {
        if ( _promptBeforeSave && !MsgBox.confirm(promptMessage) ) return;
        updateEntity( entity );
        if(saveHandler) saveHandler(entity);
        return "_close";
    }

    public def saveAndRead() {
        if ( _promptBeforeSave && !MsgBox.confirm(promptMessage) ) return;
        def outcome = updateEntity( entity );
        if(saveHandler) saveHandler(entity);
        editmode = 'read';
        return outcome;
    }

    public def saveAndContinue() {
        if ( _promptBeforeSave && !MsgBox.confirm(promptMessage) ) return;
        updateEntity( entity );
        if(saveHandler) saveHandler(entity);
        return null;
    }
    
    public def save() {
        if ( _promptBeforeSave && !MsgBox.confirm(promptMessage) ) return;
        def outcome = (editmode == 'edit')? updateEntity( entity ) : saveNewEntity( entity );
        if(saveHandler) saveHandler(entity);
        editmode = 'read';
        return outcome;
    }

    public def cancelEdit() {
        changeLog.undoAll()
        editmode = 'read';
        return null;
    }

    @Close
    public boolean onClose() {
        println "---> closing....";
        if ( changeLog.hasChanges() ) {
            return MsgBox.confirm(closeMessage);
        }
        return true;
    }

}