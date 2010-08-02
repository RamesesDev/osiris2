/*
 * SqlTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 9:59 AM
 */

package test.sql;

import junit.framework.*;

/**
 *
 * @author elmo
 */
public class NamedQueryTest extends TestCase {
    
    public NamedQueryTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testQuery() throws Exception {
        //BasicSqlCacheProvider np = new BasicSqlCacheProvider();
        //SqlCache sq = np.getNamedSqlCache("customer_insert.sqlx");
        //System.out.println(sq.getStatement());
        
        StringBuffer c = new StringBuffer(" fields");
        c.insert(0, "select");
        System.out.println(c.toString());
    }
    
    
}
