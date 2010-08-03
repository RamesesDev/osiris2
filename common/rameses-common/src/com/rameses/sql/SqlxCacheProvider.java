/*
 * SqlxCacheProvider.java
 *
 * Created on August 3, 2010, 2:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public class SqlxCacheProvider extends SqlCacheProvider {
    
    /** Creates a new instance of SqlxCacheProvider */
    public SqlxCacheProvider() {
    }

    public boolean accept(String name) {
        return  name.endsWith(".sqlx");
    }

    public SqlCache createSqlCache(String name) {
        InputStream is = this.getSqlCacheResourceHandler().getResource( name );
        if(is==null)
            throw new IllegalStateException("Resource for " + name + " not found");

        String txt = getInputStreamToString(is);
        String statement = formatText( txt );
        return new SqlCache(statement);
    }

    private String formatText( String sql ) {
        //parse the insert statement.
        StringBuffer sb = new StringBuffer();
        String arr[] = sql.split("\\s(V|v)(a|A)(l|L)(u|U)(e|E)(s|S)\\s");
        if(arr.length!=2)
            throw new IllegalStateException("Malformed sqlx statement. Values is a keyword");
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
