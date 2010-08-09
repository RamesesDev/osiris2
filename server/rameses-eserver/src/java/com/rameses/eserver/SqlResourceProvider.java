/*
 * SqlResourceProvider.java
 *
 * Created on July 24, 2010, 9:19 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rameses.eserver;

import java.io.InputStream;

/**
 *
 * @author elmo
 */
public class SqlResourceProvider extends AbstractResourceProvider {
    
    public String getName() {
        return "sql";
    }

    public String getDescription() {
        return "Sql Resource Provider [sql://]";
    }

    public int getPriority() {
        return 0;
    }

    public boolean accept(String nameSpace) {
        return nameSpace.equals("sql");
    }

    public InputStream getResource(String name) throws Exception {
        String fileName = "META-INF/sql/" + name;
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
    
}
