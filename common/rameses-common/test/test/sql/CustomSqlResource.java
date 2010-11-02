/*
 * CustomSqlResource.java
 *
 * Created on August 13, 2010, 6:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.sql;

import com.rameses.sql.SqlUnitResourceProvider;
import java.io.InputStream;

/**
 *
 * @author elmo
 */
public class CustomSqlResource implements SqlUnitResourceProvider {
    
    /** Creates a new instance of CustomSqlResource */
    public CustomSqlResource() {
    }
    
    public InputStream getResource(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        //if inputstream cannot be found by the providers, try classpath
        String fileName = "META-INF/sqld/" + name;
        InputStream is = classLoader.getResourceAsStream(fileName);
        if(is==null)
            throw new RuntimeException("Sql resource " + name + " cannot be found!");
        return is;
    }
    
}
