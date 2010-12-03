/*
 * SqlTest.java
 * JUnit based test
 *
 * Created on July 22, 2010, 9:59 AM
 */

package test.sql;

import com.rameses.util.ExprUtil;
import com.rameses.util.SysMap;
import java.util.HashMap;
import java.util.Map;
import junit.framework.*;

/**
 *
 * @author elmo
 */
public class SubstituteTest extends TestCase {
    
    public SubstituteTest(String testName) {
        super(testName);
    }
    
    protected void setUp() throws Exception {
    }
    
    protected void tearDown() throws Exception {
    }
    
    public void testSubstitute() throws Exception {
        String c = " and name = ${lastname}";
        Map map = new HashMap();
        map.put("condition", c);
        //String sql = "${xcondition} select from o ${xcondition} ${fields}";
        String sql  ="select * from data where p=$P{data} ${condition}";
        System.out.println( ExprUtil.substituteValues( sql, map ));
    }
    
    public void xtestAlias() throws Exception {
        String c = "mlkp/customer";
        System.out.println( c.replaceAll("/", "_") );
    }
    
    
    public void testSubstitute1() throws Exception {
        String c = "$P{Q11}, $P{Q2}";
        Map map = new HashMap();
        map.put("condition", c);
        //String sql = "${xcondition} select from o ${xcondition} ${fields}";
        String sql  ="select * from data where p in  (#{condition})";
        System.out.println( ExprUtil.substituteValues( sql, map ));
    }
    
    public void testSubstitute2() throws Exception {
        SysMap map = new SysMap();
        System.out.println(map.get("user.dir"));
        String c = "${user.dir}/hello.html";
        System.out.println(ExprUtil.substituteValues(c, map ));
    }
    
    
}
