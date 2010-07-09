/*
 * Constant.java
 *
 * Created on June 8, 2010, 11:15 AM
 * @author jaycverg
 */

package test;


public enum Constant {
    
    ONE("One"),
    TWO("Two"),
    THREE("Three"),
    FOUR("Four"),
    FIVE("Five");
    
    private String name;
    
    Constant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
