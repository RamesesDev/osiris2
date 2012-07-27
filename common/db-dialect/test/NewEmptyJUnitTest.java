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
        String sql = "  select * from data where id not in (select * from table2)";
        int i = sql.toLowerCase().indexOf( "select" );
        String v = "select top 20 " + sql.substring(i+"select".length()+1);
        System.out.println(v);
    }

}
