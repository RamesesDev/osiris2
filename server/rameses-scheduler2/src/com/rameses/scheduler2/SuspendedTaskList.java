/*
 * SuspendedTaskProcessor.java
 * Created on October 27, 2011, 11:15 AM
 *
 * Rameses Systems Inc
 * www.ramesesinc.com
 *
 */

package com.rameses.scheduler2;

import com.rameses.server.common.AppContext;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlExecutor;
import com.rameses.sql.SqlManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author jzamss
 */
public class SuspendedTaskList implements Serializable  {
    
    private ScheduleManager manager;
    private Map<String,TaskBean> suspended = new HashMap();
    private List<String> forSuspension = new ArrayList();
    
    SuspendedTaskList(ScheduleManager s) {
        this.manager = s;
    }
    
    public void suspend(String id, TaskBean tb)  {
        suspended.put( id, tb );
        manager.notify( id, "suspended" );
    }
    
    public void resume(String id) throws Exception {
        TaskBean tb = suspended.remove( id );
        this.manager.getActiveTasks().push(tb);
        SqlContext sqlContext = SqlManager.getInstance().createContext(manager.getDataSource());
        sqlContext.setDialect(AppContext.getDialect("system", null));
        
        SqlExecutor sqle = sqlContext.createNamedExecutor("scheduler:resume");
        sqle.setParameter(1, id).execute();
        manager.notify( id, "resumed" );
    }
    
    public Map<String,TaskBean> getSuspended() {
        return suspended;
    }
    
    public boolean isSuspended(String id) {
        return suspended.containsKey(id);
    }
    
    public void addForSuspension(String id) throws Exception {
        forSuspension.add(id);
        SqlContext sqlContext = SqlManager.getInstance().createContext(manager.getDataSource());
        sqlContext.setDialect(AppContext.getDialect("system", null));
        
        SqlExecutor sqle = sqlContext.createNamedExecutor("scheduler:suspend");
        sqle.setParameter(1, id).execute();
    }
    
    public List<String> getForSuspension() {
        return forSuspension;
    }
    
    public boolean checkForSuspesion(String id) {
        return this.forSuspension.remove(id);
    }
}
