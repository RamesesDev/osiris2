/*
 * SqlTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 9:59 AM
 */

package test.sql;

import com.rameses.sql.BasicNamedQueryProvider;
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
        BasicNamedQueryProvider np = new BasicNamedQueryProvider();
        String s = np.getStatement("customer_insert.sqlx");
        System.out.println(s);
    }
    
    
}
