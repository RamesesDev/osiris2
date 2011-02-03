/*
 * Sqlx.java
 *
 * Created on August 3, 2010, 2:53 PM
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
public class SqlxSqlUnitProvider extends SqlUnitProvider {
    
    public String getType() {
        return "sqlx";
    }

    public SqlUnit getSqlUnit(String name) {
        InputStream is = null;
        try {
            is = getConf().getResourceProvider().getResource(name);
            String txt = StreamUtil.toString(is);
            String statement = formatText( txt );
            return new SqlUnit(statement);
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
        
    }

    private String formatText( String sql ) {
        //parse the insert statement.
        StringBuffer sb = new StringBuffer();
        String arr[] = sql.split("\\s(V|v)(a|A)(l|L)(u|U)(e|E)(s|S)\\s");
        if(arr.length!=2)
            throw new RuntimeException("Malformed sqlx statement. Values is a keyword");
        sb.append( arr[0] + " (");
        StringBuffer tail = new StringBuffer(") values (");
        
        //scan the properties fields
        String s = null;
        boolean firstPass = true;
        String[] brr = arr[1].split(",");
        for(int i=0;i < brr.length;i++) {
            s = brr[i];
            if( s != null && s.trim().length() > 0  ) {
                if(firstPass)
                    firstPass = false;
                else {
                    sb.append(",");
                    tail.append(",");
                }
                String[] x = new String[2];
                x[0] = s.substring( 0, s.indexOf("=")  ).trim();
                x[1] = s.substring( s.indexOf("=")+1 ).trim();
                sb.append(x[0]);
                tail.append(x[1]);
            }
        }
        return sb.append(tail).append(")").toString();
    }

    
}
