import com.rameses.sql.dialect.MsSqlDialect;
import junit.framework.*;
/*
 * NewEmptyJUnitTest.java
 * JUnit based test
 *
 * Created on April 30, 2012, 8:33 PM
 */

/**
 *
 * @author Elmo
 */
public class NewEmptyJUnitTest extends TestCase {
    
    public NewEmptyJUnitTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testHello() {
        String sql = "select p.*, case when (select 1 from useraccount u where u.objid=p.objid) is null then 0 else 1 end as canlogin " +
        " from (  " +
        " select objid,staffno,lastname,firstname  " +
        " from personnel " +
        " ) p   order by p.lastname, p.firstname, p.staffno ";
        
        int i = sql.toLowerCase().indexOf( "select" );
        //String v = "select top 20 " + sql.substring(i+"select".length()+1);
        MsSqlDialect d = new MsSqlDialect();
        String res = d.getPagingStatement(sql, 0, 10, new String[]{"objid"});
        System.out.println( res );
    }

}
