/*
 * BeanResolver.java
 *
 * Created on August 12, 2010, 1:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.schema;

import com.rameses.classutils.ClassDefMap;
import java.math.BigDecimal;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author elmo
 */
public class ClassDefMapTest extends TestCase {
    
    /** Creates a new instance of BeanResolver */
    public ClassDefMapTest() {
    }
    
    public void test1() {
        Map map = ClassDefMap.toMap(Test.class);
        System.out.println(map);
    }
    
    public static class Test {
        
        public Object getTest() {
            return "test";
        }
        
        public String getName(String name) {
            return null;
        }
        
        public BigDecimal calculateTest(BigDecimal d, String a, Integer c) {
            return null;
        }
        
    }
    
}
