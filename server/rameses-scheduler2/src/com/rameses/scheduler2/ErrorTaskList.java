/*
 * ErrorHandler.java
 * Created on October 27, 2011, 9:37 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class ErrorTaskList implements Serializable {
    
    private ScheduleManager manager;
    private Map<String,TaskBean> tasks = new HashMap();
    
    ErrorTaskList(ScheduleManager s) {
        this.manager = s;
    }
    
    public void addError(TaskBean tb, String msg) throws Exception{
        TaskBean t = null;
        SqlContext sqlContext = SqlManager.getInstance().createContext(manager.getDataSource());
        SqlExecutor sqle = sqlContext.createNamedExecutor("scheduler:log-error");
        Map map = new HashMap();
        map.put("id", tb.getId());
        map.put("message", msg);
        sqle.setParameters(map).execute();
        tasks.put(tb.getId(), tb);
        manager.notify(tb.getId(), "error");
    }
    
    public void resume(String id) throws Exception{
        SqlContext sqlContext = SqlManager.getInstance().createContext(manager.getDataSource());
        SqlExecutor sqle = sqlContext.createNamedExecutor("scheduler:resume-error");
        sqle.setParameter(1, id).execute();
        TaskBean tb = tasks.remove( id );
        this.manager.getActiveTasks().push(tb);
        this.manager.notify( id, "recovered" );
    }
    
    public boolean hasError(String id ) {
        return tasks.containsKey(id);
    }

    public Map<String,TaskBean> getTasks() {
        return tasks;
    }
    
}
