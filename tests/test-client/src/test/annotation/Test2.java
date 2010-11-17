/*
 * Test2.java
 * JUnit based test
 *
 * Created on November 8, 2010, 2:38 PM
 */

package test.annotation;

import java.lang.reflect.Field;

/**
 *
 * @author rameses
 */
public class Test2 {
    
    
    public static void main(String[] args) {
        new Test2().testHello();
    }
    
    public void testHello() {
        TestClass s = (TestClass) createClass("test.annotation.TestClass");
        s.displayClassName();
    }
    
    private Object createClass(String name) {
        try {
            Object s = getClass().getClassLoader().loadClass(name).newInstance();
            Class cl = s.getClass();
            for(Field f : cl.getDeclaredFields()) {                
                if ( !f.isAnnotationPresent(ClassName.class) ) continue;
                
                boolean acc = f.isAccessible();
                f.setAccessible(true);
                
                try {
                    f.set(s, s.getClass().getName());
                } catch(Exception e) {
                    e.printStackTrace();
                }
                
                f.setAccessible(acc);
            }
            
            return s;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
