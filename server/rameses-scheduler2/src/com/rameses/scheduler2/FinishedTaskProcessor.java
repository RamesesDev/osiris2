/*
 * ActiveTaskRunnable.java
 * Created on October 27, 2011, 9:11 AM
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author emn
 *
 * This processor updates the next expiry state in the database.
 *
 */
public class FinishedTaskProcessor extends AbstractTaskProcessor {
    
    FinishedTaskProcessor(ScheduleManager s) {
        super(s);
    }
    
    public void run() {
        
        TaskBean t = null;
        List<TaskBean> returnTasks = new ArrayList();
        List<TaskBean> ended = new ArrayList();
        SqlContext sqlContext = SqlManager.getInstance().createContext(super.getManager().getDataSource());
        sqlContext.setDialect(AppContext.getDialect("system", null));
        
        SqlExecutor sqle = sqlContext.createNamedExecutor("scheduler:update-next-date");
        while((t=queue.poll())!=null) {
            Map map = new HashMap();
            map.put("currentdate", t.getCurrentdate());
            map.put("id", t.getId());
            sqle.setParameters(map);
            sqle.addBatch();
            if(!t.isEnded()) {
                returnTasks.add( t );
            } else {
                ended.add(t);
            }
        }
        
        //update and return to active tasks
        try {
            sqle.execute();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        for(TaskBean tb : returnTasks) {
            this.getManager().getPendingTasks().push(tb);
        }
        returnTasks.clear();
        
        //remove all ended tasks
        SqlExecutor ender = sqlContext.createNamedExecutor("scheduler:remove-active");
        for(TaskBean tb : ended) {
            ender.setParameter("id", tb.getId());
            ender.addBatch();
        }
        try {
            ender.execute();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
