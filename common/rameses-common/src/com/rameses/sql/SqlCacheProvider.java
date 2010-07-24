/*
 * SqlQueryCache.java
 *
 * Created on July 24, 2010, 11:03 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author elmo
 */
public abstract class SqlCacheProvider {
    
    public abstract SqlCache getSqlCache(String statment) ;
    public abstract SqlCache getNamedSqlCache(String name) ;
    
    protected SqlCache createSqlCache(String statement) {
        List paramNames = new ArrayList();
        String parsedStatement = SqlUtil.parseStatement(statement,paramNames);
        return new SqlCache(parsedStatement, paramNames );
    }
    
    /***
     * .sqlx has a special parsing for insert statements.
     * This is used because of the difficulty of producing
     * sql text with several columns.
     * <br>
     * Basic format as follows:
     * <br>
     * insert into table values (firstname=$P{firstname}, lastname=$P{lastname})
     *
     */
    protected String formatText( String name, String sql ) {
        if(!name.endsWith(".sqlx")) {
            return sql;
        } else {
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
    
    protected String getInputStreamToString( InputStream is ) {
        try {
            StringBuffer sb = new StringBuffer();
            int i = 0;
            while((i=is.read())!=-1) {
                sb.append((char)i);
            }
            return sb.toString();
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
}
