/*
 * CustomScriptLoader.java
 *
 * Created on October 16, 2010, 3:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import com.rameses.scripting.DefaultScriptLoader;
import com.rameses.scripting.ScriptManager;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import javax.sql.DataSource;

/**
 *
 * @author ms
 */
public class CustomScriptLoader extends DefaultScriptLoader {
    
    public CustomScriptLoader(ScriptManager sm) {
        super(sm);
    }

    public InputStream findResource(String name) {
        try {
            DataSource ds = AppContext.getSystemDs();
            SqlContext sqlc = SqlManager.getInstance().createContext(ds);
            SqlQuery sq = sqlc.createNamedQuery("eserver:script");
            Map map = (Map)sq.setParameter(1, name).getSingleResult();
            if(map!=null) {
                String s = (String)map.get("content");
                return  new ByteArrayInputStream(s.getBytes());
            }
        }
        catch(Exception e) {
            System.out.println("CustomScriptLoader.findResource. Error->"+e.getMessage());
        }
        return super.findResource( name );
    }
    
}
