/*
 * SimpleSqlUnitProvider.java
 *
 * Created on August 13, 2010, 2:37 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import com.rameses.io.StreamUtil;
import java.io.InputStream;

/**
 *
 * @author elmo
 */
public class SimpleSqlUnitProvider extends SqlUnitProvider {
    
    public String getType() {
        return "sql";
    }
    
    public SqlUnit getSqlUnit(String name) {
        InputStream is = null;
        try {
            is = getConf().getResourceProvider().getResource(name);
            String txt = StreamUtil.toString(is);
            return new SqlUnit(txt);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
}
