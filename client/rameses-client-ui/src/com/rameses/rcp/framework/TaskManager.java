/*
 * This facility provides threadin tasks for the client.
 * Clients/Tasks are registered to this thread
 * and the execute method is called if the task is not
 * processing.
 */
package com.rameses.rcp.framework;

import com.rameses.rcp.common.Task;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskManager {
    
    private boolean started;
    
    private List<Task> taskPool = Collections.synchronizedList( new ArrayList<Task>());
    private MainThread mainThread;
    private List<TaskThread> activeThreads = Collections.synchronizedList(new ArrayList<TaskThread>());
    
    public TaskManager() {
    }

    public void start() {
        if(started) return;
        started = true;
        mainThread = new MainThread();
        mainThread.start();
    }
    
    public void stop() {
        mainThread.cancel();
        synchronized(activeThreads) {
            for(TaskThread tt: activeThreads) {
                tt.cancel();
            }        
        }
        activeThreads.clear(); 
        mainThread = null;
        started = false;
    }

        
    public class MainThread extends Thread {
        
        private boolean cancelled;
        
        public void run() {
            while(!cancelled) {
                synchronized(taskPool) {
                    List<Task> tasksForRemoval = new ArrayList<Task>();
                    for(Task t: taskPool) {
                        if(t.accept()) {
                            tasksForRemoval.add(t);
                            TaskThread tt = new TaskThread(t);
                            activeThreads.add(tt);
                            tt.start();
                        }
                    }
                    taskPool.removeAll(tasksForRemoval);
                }
                if(cancelled) break;
                try {
                    sleep(1000);
                } 
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        public void cancel() {
            cancelled = true;
        }
    }
    

    private class TaskThread extends Thread {
        private Task task;
        public void cancel() {
            task.setCancelled(true);
        }
        public TaskThread(Task task) {
            this.task = task;
        }
        public void run() {
            task.start();
            task.execute();
            //after executing, do we need to send this back to the pool? check if ended
            if(!task.isEnded()) {
                addTask(task);
            }
            else {
                task.end();
            }
        }
    }

    public void addTask(Task t) {
        taskPool.add(t);
    }
    
    public void removeTask(Task t) {
        taskPool.remove(t);
    }

    public boolean isStarted() {
        return started;
    }
    
}
