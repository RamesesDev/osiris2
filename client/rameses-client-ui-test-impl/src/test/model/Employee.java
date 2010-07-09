/*
 * Employee.java
 *
 * Created on June 28, 2010, 1:21 PM
 * @author jaycverg
 */

package test.model;


public class Employee {
    
    private String name;
    private String address;
    private boolean working;
    private EmployeeType type;
    private String gender = "male";
    
    public Employee() {
    }
    
    public Employee(String n, String a, boolean w, EmployeeType t) {
        this.name = n;
        this.address = a;
        this.working = w;
        this.type = t;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    
}
