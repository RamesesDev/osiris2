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
        String fileName = "META-INF/sql/" + name + ".sql";
        StringBuffer sb = new StringBuffer();
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            int i = 0;
            while((i=is.read())!=-1) {
                sb.append((char)i);
            }
            return sb.toString();
        }
        catch(Exception e) {
            throw new IllegalStateException(e);
        }
        finally {
            try {is.close();} catch(Exception ign){;}
        }
    }
    
}
