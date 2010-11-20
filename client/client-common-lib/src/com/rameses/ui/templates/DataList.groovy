package com.rameses.ui.templates;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

public abstract class DataList {

    def search;
    def queryOpener = new Opener(outcome:"query");
    def query = null;
    def _formActions;
    def formTitle = "No title specified";
    def _entityName;

    @Controller
    def controller;

    public def getEntityName() {
        if(!_entityName) {
            _entityName = controller.workunit.workunit.name;
            _entityName = _entityName[0..(_entityName.lastIndexOf('_list')-1)];
        }
        return _entityName;
    }


    public def getFormActions() {
        if(_formActions==null && entityName !=null) {
            def p = { inv -> return getInvokerParams( inv ) } as InvokerParameter;
            _formActions = InvokerUtil.lookupActions( "listFormActions", p ).findAll {
                it.invoker.module.name == controller.workunit.module.name && 
                it.invoker.workunitname == entityName
            }
        }
        return _formActions;
    }        

    public void showFilter() {
        if(queryOpener==null) queryOpener = getQueryOpener();
        query = queryOpener;
    }
            
    public void hideFilter() {
        query = null;
    }

    def listHandler = [
        getRows : { return 25; },
        getColumns : { return getColumns(); }, 
        fetchList :  { o-> return fetchList( o ) }, 
        onOpenItem : { o,col-> 
            def params = getInvokerParams( [action:"open"] );
            InvokerUtil.lookupOpeners( "listOpen", params  ).find{ it.name == controller.workunit.module.name+":"+entityName } 
        }
    ] as PageListModel;


    public abstract def getColumns();
    public abstract def fetchList( def o );

    public def getQueryOpener() { 
        return queryOpener; 
    }

    def saveHandler = { o->
        listHandler.load();
    }

    public def getSelectedItem() {
        return listHandler.selectedItem?.item;
    }
 
    public def getParams( inv )  { return [:] }
    public def getCreateParams() { return [:] }
    public def getOpenParams()   { return [:] }

    public def getInvokerParams(def inv) {
        def map = [saveHandler: saveHandler ];
        def p;
        if( inv.action == "create" ) 
            p = getCreateParams();
        else if(inv.action == "open" )
            p = getOpenParams();
        else
            p = getParams(inv);
        
        if ( p ) map.putAll( p );
        map.put( "entityName", entityName );
        return map;
    }


}