/*
 * TableDelayedActionMgr.java
 *
 * Created on April 5, 2011, 10:18 AM
 * @author jaycverg
 */

package com.rameses.rcp.control.table;

import java.awt.EventQueue;

public class TableDelayedActionMgr implements Runnable {
    
    private static final int MAX_COUNT = 500; //0.5 seconds
    
    private Action action;
    private int counter;
    private boolean running;
    
    private Thread thread;
    
    
    public TableDelayedActionMgr(Action action) {
        this.action = action;
    }
    
    public void run() 
    {
        while( running && counter < MAX_COUNT ) 
        {
            try {
                Thread.sleep(50);
            } 
            catch (InterruptedException ex) {
                break;
            }
            
            counter += 50;
            if( counter >= MAX_COUNT ) 
            {
                if( action != null ) 
                {
                    EventQueue.invokeLater(new Runnable() 
                    {
                        public void run() {
                            action.execute();
                        }
                    });
                }
                break;
            }
        }
        
        stop();
    }
    
    public void start() {
        counter = 0;
        if( thread != null && thread.isAlive() ) return;
        
        running = true;        
        thread = new Thread(this);
        thread.start();
    }
    
    public void stop() {
        running = false;
        thread = null;
    }
    
    //--- inner classes
    
    public static interface Action {
        void execute();
    }
    
    
}
