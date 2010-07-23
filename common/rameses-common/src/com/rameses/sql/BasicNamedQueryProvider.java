/*
 * BasicNamedQueryProvider.java
 *
 * Created on July 22, 2010, 3:24 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.sql;

import java.io.InputStream;

/**
 * The basic query will locate files under the META-INF/sql folder
 * Files must end with .sql extension.
 */
public class BasicNamedQueryProvider implements NamedQueryProvider {
    
    public String getStatement(String name) {
        String fileName = "META-INF/sql/" + name;
        if( name.indexOf(".")<0 ) fileName = fileName + ".sql";
        
        
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            int i = 0;
            while((i=is.read())!=-1) {
                sb.append((char)i);
            }
            
            return formatText(fileName, sb.toString());
        } catch(Exception e) {
            throw new IllegalStateException(e);
        } finally {
            try {is.close();} catch(Exception ign){;}
        }
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
    public String formatText( String name, String sql ) {
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
    
    
    
}
