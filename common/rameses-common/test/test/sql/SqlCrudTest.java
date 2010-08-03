/*
 * SqlTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 9:59 AM
 */

package test.sql;

import com.rameses.sql.CrudSqlCacheProvider;
import com.rameses.sql.DefaultSqlCacheResourceHandler;
import com.rameses.sql.SqlCache;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SqlCrudTest extends TestCase {
    
    public SqlCrudTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testCrud() throws Exception {
        //BasicSqlCacheProvider np = new BasicSqlCacheProvider();
        //SqlCache sq = np.getNamedSqlCache("customer_insert.sqlx");
        //System.out.println(sq.getStatement());
        
        CrudSqlCacheProvider cp = new CrudSqlCacheProvider();
        cp.setSqlCacheResourceHandler( new DefaultSqlCacheResourceHandler() );
        SqlCache sq = cp.createSqlCache( "branch_user_read.crud" );
        System.out.println(sq.getStatement());
        for(Object o: sq.getParamNames()) {
            System.out.println(o+"");
        }
        
    }
    
    
}
