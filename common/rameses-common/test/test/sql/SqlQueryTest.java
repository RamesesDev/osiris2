/*
 * SqlTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 9:59 AM
 */

package test.sql;

import com.rameses.sql.SimpleDataSource;
import com.rameses.sql.SqlContext;
import com.rameses.sql.SqlManager;
import com.rameses.sql.SqlQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SqlQueryTest extends TestCase {
    
    private DataSource ds;
    private SqlManager factory = SqlManager.getInstance();
    
    public SqlQueryTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
        ds = new SimpleDataSource("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/taxpayer", "root", null);
    }
    
    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testQuery() throws Exception {
        SqlContext sm = factory.createContext( ds  );
        String sql = "select name from taxpayer where name like $P{name}";
        
        System.out.println("first query--------------");
        SqlQuery q = sm.createQuery(sql);
        List list = q.setParameter("name", "ABECIA%").getResultList();
        for(Object o :list) System.out.println(o);
        
        System.out.println("second query --- test clear -------------");
        q = sm.createQuery(sql);
        list = q.setParameter("name", "ABALLE%").getResultList();
        for(Object o :list) System.out.println(o);
        
        System.out.println("third query - using map -------------");
        q = sm.createQuery(sql);
        Map map = new HashMap();
        map.put("name", "ABALLE%");
        list = q.setParameters(map).getResultList();
        for(Object o :list) System.out.println(o);
        
        System.out.println("third query - using list -------------");
        q = sm.createQuery(sql);
        List parms = new ArrayList();
        parms.add("ABALLE%");
        list = q.setParameters(parms).getResultList();
        for(Object o :list) System.out.println(o);
        
    }
    
    public void testAnotherQuery() throws Exception {
        SqlContext sm = factory.createContext( ds  );
        SqlQuery q = sm.createQuery("select name,taxpayerno from taxpayer where name like ?");
        List list = new ArrayList();
        list.add( "ABALLE%");
        for(Object o : q.setParameters( list ).getResultList() ) System.out.println(o);
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testFetch() throws Exception {
        SqlContext sm = factory.createContext( ds  );
        System.out.println("test fetch");
        SqlQuery q = sm.createQuery("select name from taxpayer");
        q.setFirstResult(0);
        q.setMaxResults(5);
        System.out.println("PAGE 1----------------------");
        for(Object o: q.getResultList()) {
            System.out.println(o);
        }
        System.out.println("PAGE 2-----------------------");
        for(Object o: q.setFirstResult(5).getResultList()) {
            System.out.println(o);
        }
        System.out.println("PAGE 3-----------------------");
        for(Object o: q.setFirstResult(10).getResultList()) {
            System.out.println(o);
        }
    }
    
    public void testFetchQueryByName() throws Exception {
        SqlContext sm = factory.createContext( ds  );
        System.out.println("test fetch by name ---- customerlist --------");
        SqlQuery q = sm.createNamedQuery("customerlist");
        q.setMaxResults(10);
        for(Object o : q.getResultList()) System.out.println(o);
    }
    
    
}
