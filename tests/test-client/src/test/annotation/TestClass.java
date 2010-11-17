/*
 * TestClass.java
 *
 * Created on November 16, 2010, 4:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.annotation;

/**
 *
 * @author rameses
 */
public class TestClass {
    
    @ClassName
    private String className;
    
    public TestClass() {
    }
    
    public void displayClassName() {
        System.out.println( className );
    }
    
}
