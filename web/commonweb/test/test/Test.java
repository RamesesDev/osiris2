/*
 * Test.java
 * JUnit based test
 *
 * Created on October 24, 2008, 8:20 AM
 */

package test;

import junit.framework.*;

/**
 *
 * @author elmo
 */
public class Test extends TestCase {
    
    
    public void testHello() {
        Constant c = Constant.FIVE;
        
        if ( c.getClass().isEnum() ) {
            Object[] items = c.getClass().getEnumConstants();
            for ( Object item : items ) {
                try {
                    System.out.println(item.toString());
                } catch (Exception ex) {;}
            }
        }
    }
    
}
