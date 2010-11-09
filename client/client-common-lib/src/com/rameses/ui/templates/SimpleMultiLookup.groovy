package com.rameses.ui.templates;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

public abstract class SimpleMultiLookup {

    def queryOpener = new Opener(outcome:"query");
    def search;
    def query = null;
    def formActions; 
    def formTitle = "No Title specfied";
    def selectHandler;
    def included = [];

    public void showFilter() {
        if(queryOpener==null) queryOpener = getQueryOpener();
        query = queryOpener;
    }
            
    public void hideFilter() {
        query = null;
    }

    def listHandler = [
        getColumns : {
            def cols = [ new Column(name: "selected", caption: "", maxWidth:25, type: boolean, editable:true) ];
            cols.addAll( getColumns() );
            return cols;
         },
         fetchList :  { o->
            return fetchList( o );
         },
         checkItem : { item,selected->
            if(selected) 
                included.add( item )
            else
            included.remove( item );
         },
         checkSelected : { o->
            return included.contains( o );
         }
    ] as SubListModel;

    public abstract def getColumns();
    public abstract def fetchList( def o );

    public def getQueryOpener() { 
        return queryOpener; 
    }

    public def getSelectedItem() {
        return listHandler.selectedItem?.item;
    }

    public def select() {
        if(selectedItem==null) throw new Exception("Please choose at least one item");
        if(selectHandler==null) throw new Exception("Please provide a selectHandler");
        selectHandler(included.unique());
        return "_close";
    }

}