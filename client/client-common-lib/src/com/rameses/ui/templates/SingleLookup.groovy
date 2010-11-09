package com.rameses.ui.templates;

import com.rameses.rcp.common.*;
import com.rameses.rcp.annotations.*;
import com.rameses.osiris2.client.*;

public abstract class SingleLookup {

    def search;
    def queryOpener = new Opener(outcome:"query");
    def query = null;
    def formActions; 
    def selectHandler;
    def formTitle;

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
        fetchList :  { o-> return fetchList( o ) }
    ] as PageListModel;

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
        selectHandler(selectedItem);
        return "_close";
    }

}