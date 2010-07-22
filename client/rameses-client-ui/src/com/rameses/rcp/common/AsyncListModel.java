package com.rameses.rcp.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/***
 * This is a PageListModel similar to subitem model, however
 * the list model builds its list from a background thread.
 */
public abstract class AsyncListModel extends PageListModel implements Runnable, ProgressInfo {
    
    private Thread thread;
    private boolean cancelled;
    private int batchNo;
    
    private ProgressModel progress = new ProgressModel(this);
    
    public AsyncListModel() {
    }
    
    public void init() {
        if(isAutoStart()) {
            start();
        }
        refresh();
    }
    
    public void start() {
        if(thread!=null) return;
        toprow = 0;
        pageIndex = 1;
        pageCount = 1;
        dataList = null;
        thread = new Thread(this);
        thread.start();
    }
    
    
    
    protected int getDelay() {
        return 2000;
    }
  
    
    /**
     * The fetch portion occurs when the user navigates like
     * nextPage, nextRecord, etc.
     */
    protected void fetch() {
        if( dataList ==null ) return;
        if(toprow > dataList.size() ) return;
        
        synchronized(dataList) {
            List subList = null;
            if( maxRows > 0 ) {
                int tail = toprow + getRows();
                if( tail > dataList.size() ) tail = dataList.size();
                subList = dataList.subList(toprow, tail);
            } else {
                subList = new ArrayList();
            }
            fillListItems(subList,toprow);
            
            if(selectedItem!=null) {
                pageIndex = (selectedItem.getRownum()/ getRows())+1;
            }
            else {
                super.setSelectedItem(0);
            }
        }
        
        //determine if last page or not
        /*
        if(pageIndex < pageCount)
            lastPage = false;
        else
            lastPage = true;
         */
        
    }
    
    public void cancel() {
        cancelled = true;
    }
    
    
    public void run() {
        toprow = 0;
        getProgress().notifyStart();
        batchNo = 1;
        items.clear();
        
        cancelled = false;
        
        int rowsize = getRows();
        while(!cancelled) {
            int startrow = (batchNo-1) * rowsize;
            Map map = new HashMap();
            map.put("_start", startrow);
            map.put( "_rowsize", rowsize+1);
            map.put( "_batch", batchNo);
            
            List subList = fetchList(map);
            if(subList == null) subList = new ArrayList();
            
            boolean firstTime = false;
            if(dataList==null) {
                firstTime = true;
                dataList = new ArrayList();
            }
            
            synchronized(dataList) {
                //if this is the first time, we need to referesh the items
                for(Object o: subList) {
                    if(dataList.indexOf(o)<0) dataList.add(o);
                }
                
                //recalculate the new size of the list and the page count.
                maxRows = dataList.size() -1 ;
                
                //the extra row should be added only once during the first pass.
                if( super.isAllocNewRow()  ) maxRows = maxRows + 1;
                pageCount = ((maxRows+1) / getRows()) + (((maxRows+1) % getRows()>0)?1:0);
            }
            
            
            if(firstTime) {
                refresh();
            } 
            else {
                super.refreshSelectedItem();
            }
            
            //check also if sub list size is 0. If 0 then exit
            //this can be overridden by the cancel feedback from the client
            //during fetchList event.
            
            if( subList.size() == 0 ) {
                cancelled = true;
            }
            
            //the newlist is added to the main list.
            getProgress().addCompleted(subList.size());
            
            //if a cancel signal exists, it will override the default
            //cancelled behavior
            if(map.get("cancel")!=null) {
                try {
                    cancelled =  Boolean.valueOf(map.get("cancel")+"");
                } catch(Exception ign){;}
            }
           
            //exit if cancelled
            if(cancelled) break;
            
            try {
                Thread.sleep(getDelay());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            batchNo++;
        }
        System.out.println("TOTAL MAX ROWS IN ASYNC LIST->" + maxRows);
        getProgress().notifyStop();
        thread = null;
    }
    
    
    
    public int getEstimatedMaxSize() {
        return maxRows;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public boolean isStarted() {
        return (thread!=null);
    }
    
    protected boolean isAutoStart() {
        return false;
    }

    public ProgressModel getProgress() {
        return progress;
    }

    public void destroy() {
        cancelled = true;
        super.destroy();
    }


}
