/*
 * CustomScriptLoader.java
 *
 * Created on October 16, 2010, 3:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.server.common.AppContext;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import com.rameses.sql.SqlUnitResourceProvider;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author ms
 */
public class DBSqlResourceProvider  implements SqlUnitResourceProvider {
    
     public  InputStream getResource(String name) {
        try {
            DataSource ds = AppContext.getSystemDs();
            SqlContext sqlc = SqlManager.getInstance().createContext(ds);
            
            //we have to hard code this to prevent an endless loop.
            SqlQuery sq = sqlc.createQuery("select content from sys_sql where name=?");
            
            Map map = (Map)sq.setParameter(1, name).getSingleResult();
            if(map!=null) {
                String s = (String)map.get("content");
                return  new ByteArrayInputStream(s.getBytes());
            }
            return null;
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    
}
