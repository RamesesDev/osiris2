/*
 * CustomInterceptorLoader.java
 *
 * Created on October 16, 2010, 5:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.scripting.DefaultInterceptorLoader;
import com.rameses.scripting.ScriptManager;
import com.rameses.server.common.AppContext;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author ms
 */
public class CustomInterceptorLoader extends DefaultInterceptorLoader {
    
    public CustomInterceptorLoader(ScriptManager sm) {
        super(sm);
    }

    public void buildInterceptorList(final List interceptorNames) {
         try {
            DataSource ds = AppContext.getSystemDs();
            SqlContext sqlc = SqlManager.getInstance().createContext(ds);
            SqlQuery sq = sqlc.createNamedQuery("scripting:interceptors");
            List<Map> list = sq.getResultList();
            for(Map m: list ) {
                String s = (String)m.get("name");
                if(interceptorNames.indexOf(s)<0) interceptorNames.add(s);
            }
        }
        catch(Exception e) {
            System.out.println("Error. CustomInterceptorLoader ->"+e.getMessage());
        }
        super.buildInterceptorList(interceptorNames);
    }
    
    
    
}
