/*
 * Tester.java
 * JUnit based test
 *
 * Created on June 2, 2010, 2:26 PM
 */

package test;

import com.rameses.sql.SqlUtil;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class Tester extends TestCase {
    
    public Tester(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
    }

    protected void tearDown() throws Exception {
    }
    
    // TODO add test methods here. The name must begin with 'test'. For example:
    public void testSubstitute() throws Exception {
        String c = "where cond = $P{cond}";
        Map map = new HashMap();
        map.put("condition", c);
        String sql = "select from o ${condition}";
        System.out.println( SqlUtil.substituteValues( sql, map ));
    }

    
}
